package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to mark a task as not completed.
 * Updates the task's status and saves the changes to storage.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Constructs a new UnmarkCommand with the specified task index.
     *
     * @param index the 0-based index of the task to mark as not done
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the unmark command by marking the specified task as not completed.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to unmark
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        tasks.markUndone(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("OK, I've marked this task as not done yet:");
        ui.show(tasks.get(index).toString());
        ui.showLine();
    }
}

