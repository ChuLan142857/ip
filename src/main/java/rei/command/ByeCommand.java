package rei.command;

import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;

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

