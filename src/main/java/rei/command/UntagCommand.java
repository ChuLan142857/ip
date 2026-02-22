package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to remove a tag from a task.
 * Updates the task's tags and saves the changes to storage.
 */
public class UntagCommand extends Command {
    private final int index;
    private final String tag;

    /**
     * Constructs a new UntagCommand with the specified task index and tag.
     *
     * @param index the 0-based index of the task to untag
     * @param tag the tag to remove from the task
     */
    public UntagCommand(int index, String tag) {
        this.index = index;
        this.tag = tag;
    }

    /**
     * Executes the untag command by removing the tag from the specified task.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to untag
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid, tag doesn't exist, or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        Task task = tasks.get(index);
        boolean removed = task.removeTag(tag);
        
        if (!removed) {
            throw new ReiExceptions("OOPS!!! This task doesn't have the tag: " + tag);
        }
        
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it! I've removed the tag from this task:");
        ui.show(task.toString());
        ui.showLine();
    }
}
