package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

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

