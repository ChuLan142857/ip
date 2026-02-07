package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

import java.time.LocalDateTime;

/**
 * Command to create and add a new event task to the task list.
 * An event task occurs during a specific time period with start and end times.
 */
public class EventCommand extends Command {

    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructs a new EventCommand with the specified description, start time, and end time.
     *
     * @param description the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     */
    public EventCommand(String description, LocalDateTime start, LocalDateTime end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    /**
     * Executes the event command by creating a new event task and adding it to the task list.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList to add the new event task to
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error saving to storage
     */
    @Override
    public void execute (TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        tasks.add(new Event(description, start, end));
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it. I've added this task:");
        ui.show(tasks.getLast().toString());
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
