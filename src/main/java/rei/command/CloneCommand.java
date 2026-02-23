package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;
import rei.task.Todo;
import rei.task.Deadline;
import rei.task.Event;

/**
 * Command to clone an existing task.
 * Creates a duplicate of the specified task with a new insertion order.
 */
public class CloneCommand extends Command {
    private final int targetIndex;

    /**
     * Constructs a new CloneCommand to clone a task.
     *
     * @param targetIndex the 0-based index of the task to clone
     */
    public CloneCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
     * Executes the clone command by creating a copy of the specified task.
     *
     * @param tasks the TaskList to add the cloned task to
     * @param ui the Ui for displaying messages
     * @param storage the Storage system for saving the new task
     * @throws ReiExceptions if the index is invalid
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        
        Task originalTask = tasks.get(targetIndex);
        Task clonedTask = cloneTask(originalTask);
        
        tasks.add(clonedTask);
        storage.save(tasks);
        
        ui.showLine();
        ui.show("Got it! I've cloned this task:");
        ui.show("  " + clonedTask);
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }

    /**
     * Creates a clone of the given task.
     * The clone will have the same description and dates but a new insertion order.
     *
     * @param task the task to clone
     * @return a new task with the same properties
     */
    private Task cloneTask(Task task) {
        Task cloned;
        
        if (task instanceof Deadline) {
            Deadline deadlineTask = (Deadline) task;
            cloned = new Deadline(deadlineTask.getDescription(), deadlineTask.getDeadline());
        } else if (task instanceof Event) {
            Event eventTask = (Event) task;
            cloned = new Event(eventTask.getDescription(), eventTask.getStart(), eventTask.getEnd());
        } else {
            // Todo
            cloned = new Todo(task.getDescription());
        }
        
        // Preserve the done status
        if (task.isDone()) {
            cloned.markDone();
        }
        
        return cloned;
    }
}
