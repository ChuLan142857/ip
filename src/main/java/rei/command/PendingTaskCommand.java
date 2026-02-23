package rei.command;

import rei.task.Task;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Represents a command that adds a task but is pending user confirmation.
 * Used when a duplicate task is detected and needs user approval.
 */
public class PendingTaskCommand extends Command {
    private final Task taskToAdd;

    /**
     * Constructs a new PendingTaskCommand with the task to be added.
     *
     * @param taskToAdd the task pending confirmation
     */
    public PendingTaskCommand(Task taskToAdd) {
        this.taskToAdd = taskToAdd;
    }

    /**
     * Executes the pending task by adding it to the task list.
     *
     * @param tasks the TaskList to add the task to
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        tasks.add(taskToAdd);
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it. I've added this task:");
        ui.show(tasks.getLast().toString());
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    /**
     * Gets the pending task.
     *
     * @return the task pending confirmation
     */
    public Task getTask() {
        return taskToAdd;
    }
}
