package rei.command;

import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;

import java.util.ArrayList;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ArrayList<Task> matches = new ArrayList<>();

        for (Task task : tasks.getAll()) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matches.add(task);
            }
        }

        ui.showLine();
        if (matches.isEmpty()) {
            ui.show("No tasks match the keyword: " + keyword);
        } else {
            ui.show("Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                ui.show((i + 1) + ". " + matches.get(i));
            }
        }
        ui.showLine();
    }

}
