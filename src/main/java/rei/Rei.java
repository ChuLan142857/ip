package rei;

import rei.ui.Ui;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.list.TaskList;
import rei.exceptions.ReiExceptions;
import rei.parser.Parser;
import rei.command.Command;

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
    private final Storage storage;
    private final TaskList tasks;

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
            Command command = parseCommand(userInput);
            executeCommand(command);
            
            return shouldContinueAfterCommand(command);
            
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
