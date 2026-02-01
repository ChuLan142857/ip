package rei.command;

import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions;

    public boolean isExit() {
        return false;
    }
}

