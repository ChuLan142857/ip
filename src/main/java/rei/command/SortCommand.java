package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.task.Task;
import rei.task.Deadline;
import rei.task.Event;
import rei.task.Todo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Command to sort tasks chronologically.
 * Sorts deadlines by their deadline date, events by their start date,
 * and places todos at the end (sorted alphabetically).
 */
public class SortCommand extends Command {

    /**
     * Executes the sort command to reorder tasks chronologically.
     *
     * @param tasks the TaskList to sort
     * @param ui the Ui for displaying messages
     * @param storage the Storage system for saving the sorted list
     * @throws ReiExceptions if there's an error saving the sorted list
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        
        if (tasks.size() == 0) {
            ui.showLine();
            ui.show("Your task list is empty. Nothing to sort.");
            ui.showLine();
            return;
        }
        
        // Get all tasks and sort them
        ArrayList<Task> taskList = tasks.getAll();
        taskList.sort(new TaskDateComparator());
        
        // Save the sorted list
        storage.save(tasks);
        
        ui.showLine();
        ui.show("Tasks sorted chronologically!");
        ui.show("Deadlines and events are sorted by date, todos are at the end.");
        ui.showLine();
    }
    
    /**
     * Comparator for sorting tasks by their dates.
     * Orders tasks as follows:
     * 1. Deadlines and Events sorted chronologically by their date
     * 2. Todos sorted alphabetically at the end
     */
    private static class TaskDateComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            LocalDateTime date1 = getTaskDate(t1);
            LocalDateTime date2 = getTaskDate(t2);
            
            // Both tasks have dates - compare chronologically
            if (date1 != null && date2 != null) {
                return date1.compareTo(date2);
            }
            
            // t1 has no date (Todo), t2 has date - t1 goes after
            if (date1 == null && date2 != null) {
                return 1;
            }
            
            // t1 has date, t2 has no date (Todo) - t1 goes before
            if (date1 != null && date2 == null) {
                return -1;
            }
            
            // Both are Todos - sort alphabetically by description
            return t1.getDescription().compareToIgnoreCase(t2.getDescription());
        }
        
        /**
         * Extracts the relevant date from a task for sorting purposes.
         * 
         * @param task the task to extract the date from
         * @return the deadline date for Deadline, start date for Event, null for Todo
         */
        private LocalDateTime getTaskDate(Task task) {
            if (task instanceof Deadline) {
                return ((Deadline) task).getDeadline();
            } else if (task instanceof Event) {
                return ((Event) task).getStart();
            } else {
                return null; // Todo has no date
            }
        }
    }
}
