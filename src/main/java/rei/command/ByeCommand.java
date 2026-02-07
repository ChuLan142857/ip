package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;

/**
 * Command to exit the Rei application.
 * Displays a farewell message and signals the application to terminate.
 */
public class ByeCommand extends Command {

    /**
     * Executes the bye command by displaying a farewell message.
     *
     * @param tasks the TaskList (not used in this command)
     * @param ui the Ui for displaying the farewell message
     * @param storage the Storage (not used in this command)
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage) {
        ui.showLine();
        ui.show("Bye. Have a nice day.");
        ui.showLine();
    }

    /**
     * Indicates that this command should terminate the application.
     *
     * @return true to signal that the application should exit
     */
    @Override
    public boolean isExit() {
        return true;
    }
}

