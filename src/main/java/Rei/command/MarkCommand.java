package Rei.command;

import Rei.task.*;
import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;
import Rei.exceptions.ReiExceptions;

public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        tasks.markDone(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("Nice! I've marked this task as done:");
        ui.show(tasks.get(index).toString());
        ui.showLine();
    }
}
