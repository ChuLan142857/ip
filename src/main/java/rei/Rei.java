package rei;

import rei.ui.Ui;
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

    private final Ui ui;
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
    }

    /**
     * Starts the main execution loop of the Rei application.
     * Displays the welcome message and continuously processes user commands
     * until the user issues an exit command.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parse(input);
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
     * Entry point for the Rei application.
     *
     * @param args command line arguments (not used)
     * @throws ReiExceptions if there's an error initializing the application
     */
    public static void main(String[] args) throws ReiExceptions {
        new Rei().run();
    }
}
