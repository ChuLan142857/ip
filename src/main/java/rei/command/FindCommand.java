package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;

import java.util.ArrayList;

/**
 * Command to find and display tasks that contain a specific keyword.
 * Performs case-insensitive search through task descriptions.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a new FindCommand with the specified search keyword.
     *
     * @param keyword the keyword to search for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for tasks containing the keyword.
     * Displays all matching tasks or a message if no matches are found.
     *
     * @param tasks the TaskList to search through
     * @param ui the Ui for displaying search results
     * @param storage the Storage (not used in this command)
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage) {
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
