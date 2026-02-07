package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to delete a task from the task list.
 * Removes the specified task and saves the changes to storage.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Constructs a new DeleteCommand with the specified task index.
     *
     * @param index the 0-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command by removing the specified task from the task list.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to delete
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        Task removed = tasks.remove(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("Noted. I've removed this task:");
        ui.show(removed.toString());
        ui.show("Now you have " + tasks.size() + " task(s) in the list.");
        ui.showLine();
    }
}