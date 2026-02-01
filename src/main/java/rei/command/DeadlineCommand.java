package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

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
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList to add the new deadline task to
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error saving to storage
     */
    @Override
    public void execute (TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {
        tasks.add(new Deadline(description, ddl));
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it. I've added this task:");
        ui.show(tasks.getLast().toString());
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
