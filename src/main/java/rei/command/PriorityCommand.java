package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to set the priority level of a task.
 * Updates the task's priority and saves the changes to storage.
 */
public class PriorityCommand extends Command {
    private final int index;
    private final Priority priority;

    /**
     * Constructs a new PriorityCommand with the specified task index and priority.
     *
     * @param index the 0-based index of the task to set priority for
     * @param priority the priority level to set
     */
    public PriorityCommand(int index, Priority priority) {
        this.index = index;
        this.priority = priority;
    }

    /**
     * Executes the priority command by setting the priority of the specified task.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList containing the task to update
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if the task index is invalid or there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        Task task = tasks.get(index);
        task.setPriority(priority);
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it! I've set the priority for this task:");
        ui.show(task.toString());
        ui.showLine();
    }
}
