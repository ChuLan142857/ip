package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.exceptions.ConfirmationRequiredException;

import java.time.LocalDateTime;

/**
 * Command to create and add a new deadline task to the task list.
 * A deadline task must be completed by a specific date and time.
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime ddl;

    /**
     * Constructs a new DeadlineCommand with the specified description and deadline.
     *
     * @param description the description of the deadline task
     * @param ddl the deadline date and time when the task must be completed
     */
    public DeadlineCommand(String description, LocalDateTime ddl) {
        this.description = description;
        this.ddl = ddl;
    }

    /**
     * Executes the deadline command by creating a new deadline task and adding it to the task list.
     * Checks for duplicates before adding. If a duplicate exists, throws ConfirmationRequiredException.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList to add the new deadline task to
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error saving to storage
     * @throws ConfirmationRequiredException if a duplicate task is detected
     */
    @Override
    public void execute (TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        Deadline newTask = new Deadline(description, ddl);
        
        if (tasks.hasDuplicate(newTask)) {
            PendingTaskCommand pendingCommand = new PendingTaskCommand(newTask);
            throw new ConfirmationRequiredException(
                "WARNING: This task already exists in your list!\n" +
                "Are you sure you want to add this task?\n" +
                "Answer me with yes or no(y/n)",
                pendingCommand
            );
        }

        tasks.add(newTask);
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it. I've added this task:");
        ui.show(tasks.getLast().toString());
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
