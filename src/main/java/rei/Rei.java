package rei;

import rei.ui.Ui;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.list.TaskList;
import rei.exceptions.ReiExceptions;
import rei.exceptions.ConfirmationRequiredException;
import rei.parser.Parser;
import rei.command.Command;
import rei.command.SourceCommand;

/**
 * The main class for the Rei task management application.
 * Rei is a personal task manager that helps users keep track of various types of tasks
 * including todos, deadlines, and events.
 */
public class Rei {
    
    // Constants for application configuration
    private static final String DEFAULT_DATA_FILE_PATH = "./data/Rei.txt";
    
    // Core application components
    private final UiInterface ui;
    private Storage storage;
    private TaskList tasks;
    
    // State management for duplicate confirmation
    private Command pendingCommand = null;

    /**
     * Constructs a new Rei application instance with default configuration.
     * Initializes the user interface, storage system, and loads existing tasks from file.
     *
     * @throws ReiExceptions if there's an error during initialization
     */
    public Rei() throws ReiExceptions {
        this(DEFAULT_DATA_FILE_PATH);
    }
    
    /**
     * Constructs a new Rei application instance with custom data file path.
     * Allows for dependency injection and testing with different configurations.
     *
     * @param dataFilePath the path to the data file for task storage
     * @throws ReiExceptions if there's an error during initialization
     */
    public Rei(String dataFilePath) throws ReiExceptions {
        ui = initializeUserInterface();
        storage = initializeStorage(dataFilePath);
        tasks = initializeTaskList();
        
        validateInitialization();
    }
    
    /**
     * Initializes the user interface component.
     * 
     * @return the initialized UI interface
     * @throws ReiExceptions if UI initialization fails
     */
    private UiInterface initializeUserInterface() throws ReiExceptions {
        try {
            return new Ui();
        } catch (Exception e) {
            throw new ReiExceptions("Failed to initialize user interface: " + e.getMessage());
        }
    }
    
    /**
     * Initializes the storage component with the specified file path.
     * 
     * @param dataFilePath the path to the data storage file
     * @return the initialized Storage object
     * @throws ReiExceptions if storage initialization fails
     */
    private Storage initializeStorage(String dataFilePath) throws ReiExceptions {
        try {
            return new Storage(dataFilePath);
        } catch (Exception e) {
            throw new ReiExceptions("Failed to initialize storage: " + e.getMessage());
        }
    }
    
    /**
     * Initializes the task list by loading existing tasks from storage.
     * 
     * @return the initialized TaskList object
     * @throws ReiExceptions if task list initialization fails
     */
    private TaskList initializeTaskList() throws ReiExceptions {
        try {
            return new TaskList(storage.load());
        } catch (Exception e) {
            throw new ReiExceptions("Failed to load tasks from storage: " + e.getMessage());
        }
    }
    
    /**
     * Validates that all components were initialized successfully.
     * 
     * @throws ReiExceptions if any component is not properly initialized
     */
    private void validateInitialization() throws ReiExceptions {
        if (ui == null) {
            throw new ReiExceptions("User interface was not properly initialized");
        }
        
        if (storage == null) {
            throw new ReiExceptions("Storage was not properly initialized");
        }
        
        if (tasks == null) {
            throw new ReiExceptions("Task list was not properly initialized");
        }
    }

    /**
     * Starts the main execution loop of the Rei application.
     * Displays the welcome message and continuously processes user commands
     * until the user issues an exit command.
     * 
     * @throws ReiExceptions if there's a critical error during execution
     */
    public void run() throws ReiExceptions {
        validateComponentsBeforeRun();
        
        displayWelcomeMessage();
        executeCommandLoop();
    }
    
    /**
     * Validates that all components are ready before starting the main loop.
     * 
     * @throws ReiExceptions if validation fails
     */
    private void validateComponentsBeforeRun() throws ReiExceptions {
        if (ui == null || storage == null || tasks == null) {
            throw new ReiExceptions("Application components not properly initialized");
        }
    }
    
    /**
     * Displays the welcome message to the user.
     */
    private void displayWelcomeMessage() {
        ui.showWelcome();
    }
    
    /**
     * Executes the main command processing loop.
     * Continues until an exit command is received.
     */
    private void executeCommandLoop() {
        boolean shouldContinue = true;
        
        while (shouldContinue) {
            shouldContinue = processSingleCommand();
        }
    }
    
    /**
     * Processes a single user command and returns whether to continue.
     * 
     * @return true if the application should continue, false if it should exit
     */
    private boolean processSingleCommand() {
        try {
            String userInput = getUserInput();
            
            // Check if we're waiting for confirmation
            if (pendingCommand != null) {
                return handleConfirmation(userInput);
            }
            
            Command command = parseCommand(userInput);
            executeCommand(command);
            
            // Handle data source switching if this is a SourceCommand
            if (command instanceof SourceCommand) {
                handleSourceCommand((SourceCommand) command);
            }
            
            return shouldContinueAfterCommand(command);
            
        } catch (ConfirmationRequiredException e) {
            handleConfirmationRequest(e);
            return true; // Continue and wait for user confirmation
        } catch (ReiExceptions e) {
            handleCommandError(e);
            return true; // Continue after error
        }
    }
    
    /**
     * Gets input from the user through the UI.
     * 
     * @return the user's input string
     * @throws ReiExceptions if input reading fails
     */
    private String getUserInput() throws ReiExceptions {
        String input = ui.readCommand();
        
        if (input == null) {
            throw new ReiExceptions("Received null input from UI");
        }
        
        return input;
    }
    
    /**
     * Parses user input into a command object.
     * 
     * @param userInput the raw user input
     * @return the parsed Command object
     * @throws ReiExceptions if parsing fails
     */
    private Command parseCommand(String userInput) throws ReiExceptions {
        Command command = Parser.parse(userInput);
        
        if (command == null) {
            throw new ReiExceptions("Parser returned null command");
        }
        
        return command;
    }
    
    /**
     * Executes a parsed command.
     * 
     * @param command the command to execute
     * @throws ReiExceptions if command execution fails
     */
    private void executeCommand(Command command) throws ReiExceptions {
        command.execute(tasks, ui, storage);
    }
    
    /**
     * Determines whether the application should continue after a command.
     * 
     * @param command the executed command
     * @return true if should continue, false if should exit
     */
    private boolean shouldContinueAfterCommand(Command command) {
        return !command.isExit();
    }
    
    /**
     * Handles errors that occur during command processing.
     * 
     * @param error the exception that occurred
     */
    private void handleCommandError(ReiExceptions error) {
        ui.showError(error.getMessage());
    }
    
    /**
     * Handles user confirmation when a pending command is waiting.
     * 
     * @param userInput the user's input (should be y/n)
     * @return true if the application should continue
     */
    private boolean handleConfirmation(String userInput) {
        String input = userInput.trim().toLowerCase();
        
        if (input.equals("y") || input.equals("yes")) {
            try {
                pendingCommand.execute(tasks, ui, storage);
            } catch (ReiExceptions e) {
                handleCommandError(e);
            }
        } else if (input.equals("n") || input.equals("no")) {
            ui.showLine();
            ui.show("Task not added.");
            ui.showLine();
        } else {
            ui.showLine();
            ui.show("Invalid confirmation. Please type 'y' or 'n'.");
            ui.show("Task not added.");
            ui.showLine();
        }
        
        pendingCommand = null; // Clear pending state
        return true;
    }
    
    /**
     * Handles a confirmation request from a command.
     * 
     * @param e the confirmation exception containing the message and pending command
     */
    private void handleConfirmationRequest(ConfirmationRequiredException e) {
        ui.showLine();
        ui.show(e.getMessage());
        ui.showLine();
        pendingCommand = e.getPendingCommand();
    }
    
    /**
     * Handles a source command by switching data sources if requested.
     * 
     * @param sourceCommand the source command to handle
     * @throws ReiExceptions if there's an error switching data sources
     */
    private void handleSourceCommand(SourceCommand sourceCommand) throws ReiExceptions {
        if (!sourceCommand.isQuery()) {
            String newFilePath = sourceCommand.getNewFilePath();
            switchDataSource(newFilePath);
            ui.showLine();
            ui.show("Successfully switched to new data source!");
            ui.show("Loaded " + tasks.size() + " tasks from " + newFilePath);
            ui.showLine();
        }
    }
    
    /**
     * Sets a command as pending, waiting for user confirmation.
     * 
     * @param command the command to set as pending
     */
    public void setPendingCommand(Command command) {
        this.pendingCommand = command;
    }

    /**
     * Gets the task list for external access (e.g., GUI components).
     * Provides read access to the current task state.
     *
     * @return the current task list
     */
    public TaskList getTaskList() {
        return tasks;
    }

    /**
     * Gets the storage component for external access (e.g., GUI components).
     * Allows external components to perform storage operations.
     *
     * @return the storage component
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Switches to a new data source file and reloads tasks from it.
     * Saves current tasks to the existing storage before switching.
     *
     * @param newFilePath the path to the new data file
     * @throws ReiExceptions if there's an error switching or loading from the new source
     */
    public void switchDataSource(String newFilePath) throws ReiExceptions {
        // Save current tasks before switching
        if (tasks != null && storage != null) {
            storage.save(tasks);
        }
        
        // Initialize new storage
        storage = initializeStorage(newFilePath);
        
        // Reload tasks from new storage
        tasks = initializeTaskList();
    }

    /**
     * Entry point for the Rei application.
     * Creates and runs a new Rei instance with proper error handling.
     *
     * @param args command line arguments (currently not used)
     */
    public static void main(String[] args) {
        try {
            Rei application = new Rei();
            application.run();
        } catch (ReiExceptions e) {
            System.err.println("Failed to start Rei application: " + e.getMessage());
            System.exit(1);
        }
    }
}
