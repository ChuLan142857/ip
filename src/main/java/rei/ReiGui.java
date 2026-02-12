package rei;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import rei.ui.GuiUi;
import rei.storage.Storage;
import rei.list.TaskList;
import rei.exceptions.ReiExceptions;
import rei.parser.Parser;
import rei.command.Command;

/**
 * A GUI for the Rei chatbot using JavaFX.
 * Provides a clean, responsive interface for user interaction.
 */
public class ReiGui extends Application {
    
    // Constants for window sizing
    private static final double DEFAULT_WINDOW_WIDTH = 500.0;
    private static final double DEFAULT_WINDOW_HEIGHT = 600.0;
    private static final double MINIMUM_WINDOW_WIDTH = 350.0;
    private static final double MINIMUM_WINDOW_HEIGHT = 400.0;
    
    // Constants for layout spacing and padding
    private static final double STANDARD_PADDING = 10.0;
    private static final double DIALOG_SPACING = 5.0;
    private static final double INPUT_AREA_HEIGHT = 60.0;
    private static final double SEND_BUTTON_WIDTH = 80.0;
    private static final double INPUT_BUTTON_SPACING = 100.0;
    
    // Constants for scroll behavior
    private static final double SCROLL_TO_BOTTOM = 1.0;
    
    // UI component references
    private Rei rei;
    private VBox dialogContainer;
    private ScrollPane scrollPane;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;
    
    // Application messages
    private static final String WINDOW_TITLE = "Rei Chatbot";
    private static final String WELCOME_MESSAGE = "Hello! I'm Rei.\nWhat can I do for you?";
    private static final String INPUT_PROMPT = "Type your command here...";
    private static final String SEND_BUTTON_TEXT = "Send";
    private static final String ERROR_PREFIX = "Error: ";
    private static final String CSS_FILE_PATH = "/styles.css";

    @Override
    public void init() throws Exception {
        super.init();
        initializeReiApplication();
    }
    
    /**
     * Initializes the Rei application instance.
     * Separated for better error handling and clarity.
     * 
     * @throws Exception if Rei initialization fails
     */
    private void initializeReiApplication() throws Exception {
        rei = new Rei();
        
        if (rei == null) {
            throw new Exception("Failed to initialize Rei application");
        }
    }

    @Override
    public void start(Stage stage) {
        initializeUserInterface();
        configureWindow(stage);
        setupEventHandlers();
        displayWelcomeMessage();
        applyStyling();
    }
    
    /**
     * Initializes all UI components with proper configuration.
     * Separates UI creation from layout configuration.
     */
    private void initializeUserInterface() {
        createScrollableDialogArea();
        createInputComponents();
        assembleMainLayout();
    }
    
    /**
     * Creates the scrollable dialog container for chat messages.
     */
    private void createScrollableDialogArea() {
        dialogContainer = new VBox();
        dialogContainer.setPadding(new Insets(STANDARD_PADDING));
        dialogContainer.setSpacing(DIALOG_SPACING);
        
        scrollPane = new ScrollPane();
        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
    
    /**
     * Creates input field and send button with proper sizing.
     */
    private void createInputComponents() {
        userInput = new TextField();
        userInput.setPromptText(INPUT_PROMPT);
        
        sendButton = new Button(SEND_BUTTON_TEXT);
        sendButton.setPrefWidth(SEND_BUTTON_WIDTH);
    }
    
    /**
     * Assembles the main layout and configures anchoring.
     */
    private void assembleMainLayout() {
        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);
        
        configureLayoutAnchors(mainLayout);
        
        scene = new Scene(mainLayout, DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    }
    
    /**
     * Configures anchor constraints for responsive layout.
     * 
     * @param mainLayout the layout to configure
     */
    private void configureLayoutAnchors(AnchorPane mainLayout) {
        // Configure scroll pane anchors (main chat area)
        AnchorPane.setTopAnchor(scrollPane, STANDARD_PADDING);
        AnchorPane.setLeftAnchor(scrollPane, STANDARD_PADDING);
        AnchorPane.setRightAnchor(scrollPane, STANDARD_PADDING);
        AnchorPane.setBottomAnchor(scrollPane, INPUT_AREA_HEIGHT);
        
        // Configure send button anchors (bottom right)
        AnchorPane.setBottomAnchor(sendButton, STANDARD_PADDING);
        AnchorPane.setRightAnchor(sendButton, STANDARD_PADDING);
        
        // Configure text input anchors (bottom, spans most of width)
        AnchorPane.setLeftAnchor(userInput, STANDARD_PADDING);
        AnchorPane.setBottomAnchor(userInput, STANDARD_PADDING);
        AnchorPane.setRightAnchor(userInput, INPUT_BUTTON_SPACING);
    }
    
    /**
     * Configures the main window properties and displays it.
     * 
     * @param stage the primary stage to configure
     */
    private void configureWindow(Stage stage) {
        stage.setScene(scene);
        stage.setTitle(WINDOW_TITLE);
        stage.setResizable(true);
        stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
        stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
        stage.show();
    }
    
    /**
     * Sets up event handlers for user interactions.
     */
    private void setupEventHandlers() {
        setupInputHandlers();
        setupScrollBehavior();
    }
    
    /**
     * Sets up input handling for both button click and Enter key press.
     */
    private void setupInputHandlers() {
        sendButton.setOnMouseClicked(event -> processUserInput());
        userInput.setOnAction(event -> processUserInput());
    }
    
    /**
     * Sets up automatic scroll-to-bottom behavior.
     */
    private void setupScrollBehavior() {
        dialogContainer.heightProperty().addListener(
            observable -> scrollPane.setVvalue(SCROLL_TO_BOTTOM)
        );
    }
    
    /**
     * Displays the initial welcome message.
     */
    private void displayWelcomeMessage() {
        displayMessage(WELCOME_MESSAGE, false);
    }
    
    /**
     * Applies CSS styling if available, with graceful fallback.
     */
    private void applyStyling() {
        try {
            String cssResource = getClass().getResource(CSS_FILE_PATH).toExternalForm();
            scene.getStylesheets().add(cssResource);
        } catch (Exception e) {
            // CSS file not found or inaccessible - continue without styling
            // This is expected behavior, not an error condition
        }
    }

    /**
     * Processes user input by parsing commands and displaying responses.
     * Handles the complete input-processing-output cycle.
     */
    private void processUserInput() {
        String userInputText = getUserInputText();
        
        if (isInputEmpty(userInputText)) {
            return; // Do nothing for empty input
        }
        
        displayUserMessage(userInputText);
        clearInputField();
        
        processCommandAndShowResponse(userInputText);
    }
    
    /**
     * Gets the current text from the input field.
     * 
     * @return the user input text
     */
    private String getUserInputText() {
        return userInput.getText();
    }
    
    /**
     * Checks if the input is empty or contains only whitespace.
     * 
     * @param input the input to check
     * @return true if input is effectively empty
     */
    private boolean isInputEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
    
    /**
     * Displays the user's message in the chat interface.
     * 
     * @param message the user's input message
     */
    private void displayUserMessage(String message) {
        displayMessage(message, true);
    }
    
    /**
     * Clears the input text field for the next input.
     */
    private void clearInputField() {
        userInput.clear();
    }
    
    /**
     * Processes the user command and displays the appropriate response.
     * Handles both successful execution and error cases.
     * 
     * @param input the user's command input
     */
    private void processCommandAndShowResponse(String input) {
        try {
            Command command = parseUserCommand(input);
            String response = executeCommand(command);
            displayBotResponse(response);
            
            handlePotentialExit(command);
            
        } catch (ReiExceptions e) {
            displayErrorResponse(e.getMessage());
        }
    }
    
    /**
     * Parses user input into a command object.
     * 
     * @param input the user input to parse
     * @return the parsed Command object
     * @throws ReiExceptions if parsing fails
     */
    private Command parseUserCommand(String input) throws ReiExceptions {
        return Parser.parse(input);
    }
    
    /**
     * Executes a command and returns the response.
     * 
     * @param command the command to execute
     * @return the response string from command execution
     * @throws ReiExceptions if command execution fails
     */
    private String executeCommand(Command command) throws ReiExceptions {
        GuiUi guiUi = new GuiUi();
        command.execute(rei.getTaskList(), guiUi, rei.getStorage());
        return guiUi.getResponse();
    }
    
    /**
     * Displays the bot's response message.
     * 
     * @param response the response to display
     */
    private void displayBotResponse(String response) {
        displayMessage(response, false);
    }
    
    /**
     * Displays an error response with proper formatting.
     * 
     * @param errorMessage the error message to display
     */
    private void displayErrorResponse(String errorMessage) {
        String formattedError = ERROR_PREFIX + errorMessage;
        displayMessage(formattedError, false);
    }
    
    /**
     * Handles application exit if the command is an exit command.
     * 
     * @param command the command to check for exit status
     */
    private void handlePotentialExit(Command command) {
        if (command.isExit()) {
            exitApplication();
        }
    }
    
    /**
     * Exits the JavaFX application gracefully.
     */
    private void exitApplication() {
        javafx.application.Platform.exit();
    }

    /**
     * Displays a message in the chat interface with proper formatting.
     * Creates and adds dialog boxes to the conversation area.
     *
     * @param message the message content to display
     * @param isFromUser true if the message is from user, false if from bot
     */
    private void displayMessage(String message, boolean isFromUser) {
        DialogBox dialogBox = createDialogBox(message, isFromUser);
        addDialogToContainer(dialogBox);
    }
    
    /**
     * Creates a dialog box for the given message.
     * 
     * @param message the message content
     * @param isFromUser true if from user, false if from bot
     * @return the created DialogBox
     */
    private DialogBox createDialogBox(String message, boolean isFromUser) {
        return DialogBox.getUserDialog(message, isFromUser);
    }
    
    /**
     * Adds a dialog box to the conversation container.
     * 
     * @param dialogBox the dialog box to add
     */
    private void addDialogToContainer(DialogBox dialogBox) {
        dialogContainer.getChildren().add(dialogBox);
    }

    /**
     * Main entry point for the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}