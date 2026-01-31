package Rei.command;

import Rei.task.*;
import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;
import Rei.exceptions.ReiExceptions;

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
