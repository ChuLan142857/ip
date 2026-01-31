package Rei.command;

import Rei.task.*;
import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;
import Rei.exceptions.ReiExceptions;

public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        Task removed = tasks.remove(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("Noted. I've removed this task:");
        ui.show(removed.toString());
        ui.show("Now you have " + tasks.size() + " task(s) in the list.");
        ui.showLine();
    }
}