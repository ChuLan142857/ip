package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

import java.time.LocalDateTime;

public class EventCommand extends Command {

    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public EventCommand(String description, LocalDateTime start, LocalDateTime end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute (TaskList tasks, Ui ui, Storage storage)
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
