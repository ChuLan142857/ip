package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to add a tag to a task.
 * Updates the task's tags and saves the changes to storage.
 */
public class TagCommand extends Command {
    private final int index;
    private final String tag;

    /**
     * Constructs a new TagCommand with the specified task index and tag.
     *
     * @param index the 0-based index of the task to tag
     * @param tag the tag to add to the task
     */
    public TagCommand(int index, String tag) {
        this.index = index;
        this.tag = tag;
    }

    /**
     * Executes the tag command by adding the tag to the specified task.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to tag
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        Task task = tasks.get(index);
        task.addTag(tag);
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it! I've added the tag to this task:");
        ui.show(task.toString());
        ui.showLine();
    }
}
