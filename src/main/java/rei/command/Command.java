package rei.command;

import rei.list.TaskList;
import rei.ui.Ui;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Abstract base class for all commands in the Rei application.
 * Each command represents a specific action that can be performed on the task list.
 */
public abstract class Command {
    /**
     * Executes the command with the provided task list, user interface, and storage.
     *
     * @param tasks the task list to operate on
     * @param ui the user interface for displaying output
     * @param storage the storage system for persisting tasks
     * @throws ReiExceptions if there's an error executing the command
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions;

    /**
     * Checks if this command should terminate the application.
     *
     * @return true if this command should exit the application, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}

