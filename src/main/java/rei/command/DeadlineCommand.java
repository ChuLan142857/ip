package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

import java.time.LocalDateTime;

public class DeadlineCommand extends Command{
    private final String description;
    private final LocalDateTime ddl;

    public DeadlineCommand(String description, LocalDateTime ddl) {
        this.description = description;
        this.ddl = ddl;
    }

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
