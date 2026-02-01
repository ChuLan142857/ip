package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

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
