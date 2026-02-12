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

    private final UiInterface ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Constructs a new Rei application instance.
     * Initializes the user interface, storage system, and loads existing tasks from file.
     *
     * @throws ReiExceptions if there's an error loading tasks from storage
     */
    public Rei() throws ReiExceptions {
        ui = new Ui();
        storage = new Storage("./data/Rei.txt");
        tasks = new TaskList(storage.load());
        
        // Assert all components are properly initialized
        assert ui != null : "UI should be initialized";
        assert storage != null : "Storage should be initialized";
        assert tasks != null : "Task list should be initialized";
    }

    /**
     * Starts the main execution loop of the Rei application.
     * Displays the welcome message and continuously processes user commands
     * until the user issues an exit command.
     */
    public void run() {
        assert ui != null : "UI must be initialized before running";
        assert storage != null : "Storage must be initialized before running";
        assert tasks != null : "Task list must be initialized before running";
        
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                assert input != null : "UI should not return null input";
                
                Command command = Parser.parse(input);
                assert command != null : "Parser should not return null command";
                
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    break;
                }
            } catch (ReiExceptions e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Gets the task list for GUI access.
     *
     * @return the task list
     */
    public TaskList getTaskList() {
        return tasks;
    }

    /**
     * Gets the storage for GUI access.
     *
     * @return the storage
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Entry point for the Rei application.
     *
     * @param args command line arguments (not used)
     * @throws ReiExceptions if there's an error initializing the application
     */
    public static void main(String[] args) throws ReiExceptions {
        new Rei().run();
    }
}
