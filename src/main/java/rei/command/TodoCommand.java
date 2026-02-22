package rei.command;

import rei.task.*;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Command to create and add a new todo task to the task list.
 * A todo task is a simple task without any time constraints.
 */
public class TodoCommand extends Command {
    private final String description;

    /**
     * Constructs a new TodoCommand with the specified task description.
     *
     * @param description the description of the todo task to create
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the todo command by creating a new todo task and adding it to the task list.
     * Checks for duplicates before adding. If a duplicate exists, asks for user confirmation.
     * Also saves the updated task list to storage and displays confirmation to the user.
     *
     * @param tasks the TaskList to add the new todo task to
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error saving to storage
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        Todo newTask = new Todo(description);
        
        if (tasks.hasDuplicate(newTask)) {
            ui.showLine();
            ui.show("WARNING: This task already exists in your list!");
            boolean confirmed = ui.readConfirmation("Are you sure you want to add this task? (y/n)");
            
            if (!confirmed) {
                ui.showLine();
                ui.show("Task not added.");
                ui.showLine();
                return;
            }
        }

        tasks.add(newTask);
        storage.save(tasks);

        ui.showLine();
        ui.show("Got it. I've added this task:");
        ui.show(tasks.getLast().toString());
        ui.show("Now you have " + tasks.size() + " tasks in the list.");
        ui.showLine();
    }
}
