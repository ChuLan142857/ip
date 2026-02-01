package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to display all tasks in the task list.
 * Shows each task with its index number, type, status, and description.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks in the task list.
     *
     * @param tasks the TaskList containing all tasks to display
     * @param ui the Ui for displaying output to the user
     * @param storage the Storage (not used in this command)
     * @throws ReiExceptions if there's an error displaying the tasks
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        ui.showLine();
        ui.show("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.show((i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }
}
