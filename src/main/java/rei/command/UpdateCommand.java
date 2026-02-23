package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;
import rei.task.Deadline;
import rei.task.Event;

import java.time.LocalDateTime;

/**
 * Command to update details of an existing task.
 * Allows updating description, deadline, or event times without recreating the task.
 */
public class UpdateCommand extends Command {
    private final int targetIndex;
    private final String newDescription;
    private final LocalDateTime newDeadline;
    private final LocalDateTime newStart;
    private final LocalDateTime newEnd;

    /**
     * Constructs an UpdateCommand to update a task's description.
     *
     * @param targetIndex the 0-based index of the task to update
     * @param newDescription the new description
     */
    public UpdateCommand(int targetIndex, String newDescription) {
        this.targetIndex = targetIndex;
        this.newDescription = newDescription;
        this.newDeadline = null;
        this.newStart = null;
        this.newEnd = null;
    }

    /**
     * Constructs an UpdateCommand to update a Deadline task's deadline.
     *
     * @param targetIndex the 0-based index of the task to update
     * @param newDeadline the new deadline date/time
     */
    public UpdateCommand(int targetIndex, LocalDateTime newDeadline) {
        this.targetIndex = targetIndex;
        this.newDescription = null;
        this.newDeadline = newDeadline;
        this.newStart = null;
        this.newEnd = null;
    }

    /**
     * Constructs an UpdateCommand to update an Event task's times.
     *
     * @param targetIndex the 0-based index of the task to update
     * @param newStart the new start date/time (null to keep current)
     * @param newEnd the new end date/time (null to keep current)
     */
    public UpdateCommand(int targetIndex, LocalDateTime newStart, LocalDateTime newEnd) {
        this.targetIndex = targetIndex;
        this.newDescription = null;
        this.newDeadline = null;
        this.newStart = newStart;
        this.newEnd = newEnd;
    }

    /**
     * Executes the update command by modifying the specified task.
     *
     * @param tasks the TaskList containing the task to update
     * @param ui the Ui for displaying messages
     * @param storage the Storage system for saving changes
     * @throws ReiExceptions if the index is invalid or update is incompatible with task type
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        
        Task task = tasks.get(targetIndex);
        
        // Update description
        if (newDescription != null) {
            task.setDescription(newDescription);
            storage.save(tasks);
            
            ui.showLine();
            ui.show("Got it! I've updated this task:");
            ui.show("  " + task);
            ui.showLine();
            return;
        }
        
        // Update deadline
        if (newDeadline != null) {
            if (!(task instanceof Deadline)) {
                throw new ReiExceptions("Cannot set deadline on a " + getTaskTypeName(task) + " task. Only Deadline tasks have deadlines.");
            }
            
            Deadline deadlineTask = (Deadline) task;
            deadlineTask.setDeadline(newDeadline);
            storage.save(tasks);
            
            ui.showLine();
            ui.show("Got it! I've updated the deadline:");
            ui.show("  " + task);
            ui.showLine();
            return;
        }
        
        // Update event times
        if (newStart != null || newEnd != null) {
            if (!(task instanceof Event)) {
                throw new ReiExceptions("Cannot set event times on a " + getTaskTypeName(task) + " task. Only Event tasks have start/end times.");
            }
            
            Event eventTask = (Event) task;
            if (newStart != null) {
                eventTask.setStart(newStart);
            }
            if (newEnd != null) {
                eventTask.setEnd(newEnd);
            }
            storage.save(tasks);
            
            ui.showLine();
            ui.show("Got it! I've updated the event:");
            ui.show("  " + task);
            ui.showLine();
            return;
        }
        
        throw new ReiExceptions("No valid update parameters provided.");
    }

    /**
     * Gets the type name of a task for error messages.
     *
     * @param task the task
     * @return "Todo", "Deadline", or "Event"
     */
    private String getTaskTypeName(Task task) {
        if (task instanceof Deadline) {
            return "Deadline";
        } else if (task instanceof Event) {
            return "Event";
        } else {
            return "Todo";
        }
    }
}
