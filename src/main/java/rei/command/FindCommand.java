package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;
import rei.task.Deadline;
import rei.task.Event;
import rei.task.Todo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to find and display tasks that match search criteria.
 * Performs flexible, case-insensitive partial matching across multiple fields:
 * - Task descriptions
 * - Task types (todo, deadline, event)
 * Supports both AND and OR search modes:
 * - OR mode: matches if ANY keyword is found (default)
 * - AND mode: matches only if ALL keywords are found
 */
public class FindCommand extends Command {
    private final String keyword;
    private final boolean useAndLogic;

    /**
     * Constructs a new FindCommand with the specified search keyword(s).
     * Multiple keywords can be space-separated.
     * Uses OR logic by default.
     *
     * @param keyword the keyword(s) to search for
     */
    public FindCommand(String keyword) {
        this(keyword, false);
    }

    /**
     * Constructs a new FindCommand with the specified search keyword(s) and logic mode.
     * Multiple keywords can be space-separated.
     *
     * @param keyword the keyword(s) to search for
     * @param useAndLogic true for AND logic, false for OR logic
     */
    public FindCommand(String keyword, boolean useAndLogic) {
        this.keyword = keyword;
        this.useAndLogic = useAndLogic;
    }

    /**
     * Executes the find command by searching for tasks matching the keyword(s).
     * Displays all matching tasks or a message if no matches are found.
     *
     * @param tasks the TaskList to search through
     * @param ui the Ui for displaying search results
     * @param storage the Storage (not used in this command)
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage) {
        // Split keyword into multiple search terms
        List<String> searchTerms = Arrays.asList(keyword.trim().split("\\s+"));
        ArrayList<Task> matches = new ArrayList<>();

        for (Task task : tasks.getAll()) {
            if (matchesKeywords(task, searchTerms)) {
                matches.add(task);
            }
        }

        ui.showLine();
        if (matches.isEmpty()) {
            ui.show("No tasks match the search: " + keyword);
        } else {
            String searchMode = useAndLogic ? "AND" : "OR";
            ui.show("Here are the matching tasks in your list (" + searchMode + " search):");
            for (int i = 0; i < matches.size(); i++) {
                ui.show((i + 1) + ". " + matches.get(i));
            }
            ui.show("\nFound " + matches.size() + " matching task(s).");
        }
        ui.showLine();
    }

    /**
     * Checks if a task matches the search keywords according to the search mode.
     * Performs case-insensitive partial matching against:
     * - Task description
     * - Task type name (todo, deadline, event)
     * 
     * @param task the task to check
     * @param keywords the list of keywords to search for
     * @return true if the task matches according to AND/OR logic
     */
    private boolean matchesKeywords(Task task, List<String> keywords) {
        String description = task.getDescription().toLowerCase();
        String taskType = getTaskType(task).toLowerCase();
        
        if (useAndLogic) {
            // AND logic: all keywords must match
            for (String keyword : keywords) {
                String lowerKeyword = keyword.toLowerCase();
                
                // Check if keyword matches description or task type
                if (!description.contains(lowerKeyword) && !taskType.contains(lowerKeyword)) {
                    return false; // If any keyword doesn't match, reject the task
                }
            }
            return true; // All keywords matched
        } else {
            // OR logic: any keyword can match
            for (String keyword : keywords) {
                String lowerKeyword = keyword.toLowerCase();
                
                // Check if keyword matches description or task type
                if (description.contains(lowerKeyword) || taskType.contains(lowerKeyword)) {
                    return true; // If any keyword matches, accept the task
                }
            }
            return false; // No keywords matched
        }
    }

    /**
     * Gets the type name of a task.
     * 
     * @param task the task
     * @return "todo", "deadline", or "event"
     */
    private String getTaskType(Task task) {
        if (task instanceof Todo) {
            return "todo";
        } else if (task instanceof Deadline) {
            return "deadline";
        } else if (task instanceof Event) {
            return "event";
        }
        return "";
    }

}
