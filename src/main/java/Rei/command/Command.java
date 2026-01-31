package Rei.command;

import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;
import Rei.exceptions.ReiExceptions;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions;

    public boolean isExit() {
        return false;
    }
}

