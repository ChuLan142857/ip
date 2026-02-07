package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to mark a task as completed.
 * Updates the task's status and saves the changes to storage.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Constructs a new MarkCommand with the specified task index.
     *
     * @param index the 0-based index of the task to mark as done
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the mark command by marking the specified task as completed.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to mark
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        tasks.markDone(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("Nice! I've marked this task as done:");
        ui.show(tasks.get(index).toString());
        ui.showLine();
    }
}
