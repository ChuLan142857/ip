package Rei.command;

import Rei.task.*;
import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;
import Rei.exceptions.ReiExceptions;

public class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        tasks.markUndone(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("OK, I've marked this task as not done yet:");
        ui.show(tasks.get(index).toString());
        ui.showLine();
    }
}

