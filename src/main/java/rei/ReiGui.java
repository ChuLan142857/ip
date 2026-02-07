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
 */
public class ReiGui extends Application {

    private Rei rei;
    private VBox dialogContainer;
    private ScrollPane scrollPane;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    @Override
    public void init() throws Exception {
        super.init();
        rei = new Rei();
    }

    @Override
    public void start(Stage stage) {
        // Setting up required components

        // Container for the content of the chat to scroll through
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        dialogContainer.setPadding(new Insets(10));
        dialogContainer.setSpacing(5);
        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        userInput = new TextField();
        userInput.setPromptText("Type your command here...");
        sendButton = new Button("Send");
        sendButton.setPrefWidth(80);

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout, 500, 600);

        stage.setScene(scene);
        stage.show();

        // Configure anchors for responsive layout
        AnchorPane.setTopAnchor(scrollPane, 10.0);
        AnchorPane.setLeftAnchor(scrollPane, 10.0);
        AnchorPane.setRightAnchor(scrollPane, 10.0);
        AnchorPane.setBottomAnchor(scrollPane, 60.0);

        AnchorPane.setBottomAnchor(sendButton, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);

        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(userInput, 100.0);

        // Event handlers
        sendButton.setOnMouseClicked((event) -> {
            handleUserInput();
        });

        userInput.setOnAction((event) -> {
            handleUserInput();
        });

        // Scroll down to the end every time dialogContainer's height changes.
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Display welcome message
        displayMessage("Hello! I'm Rei.\nWhat can I do for you?", false);

        stage.setTitle("Rei Chatbot");
        stage.setResizable(true);
        stage.setMinHeight(400.0);
        stage.setMinWidth(350.0);

        // Optional: Add CSS styling if available
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS file not found, continue without styling
        }
    }

    /**
     * Handles user input by processing the command and displaying the response.
     */
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        // Display user input
        displayMessage(input, true);
        userInput.clear();

        try {
            // Process the command
            Command command = Parser.parse(input);
            GuiUi guiUi = new GuiUi();
            command.execute(rei.getTaskList(), guiUi, rei.getStorage());

            // Display response
            displayMessage(guiUi.getResponse(), false);

            // Check if it's an exit command
            if (command.isExit()) {
                javafx.application.Platform.exit();
            }
        } catch (ReiExceptions e) {
            displayMessage("Error: " + e.getMessage(), false);
        }
    }

    /**
     * Displays a message in the chat interface.
     *
     * @param message  the message to display
     * @param isUser   true if the message is from user, false if from bot
     */
    private void displayMessage(String message, boolean isUser) {
        DialogBox dialog = DialogBox.getUserDialog(message, isUser);
        dialogContainer.getChildren().add(dialog);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}