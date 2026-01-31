package Rei.command;

import Rei.list.TaskList;
import Rei.ui.Ui;
import Rei.storage.Storage;

public class ByeCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.show("Bye. Have a nice day.");
        ui.showLine();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}

