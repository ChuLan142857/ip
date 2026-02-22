package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

/**
 * Represents a source switch command that is pending user confirmation.
 * Used when switching to a new data file that doesn't exist yet.
 */
public class PendingSourceCommand extends Command {
    private final String newFilePath;

    /**
     * Constructs a new PendingSourceCommand with the file path to create and switch to.
     *
     * @param newFilePath the path to the new data file
     */
    public PendingSourceCommand(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    /**
     * Executes the pending source command by signaling that the switch should proceed.
     * The actual file creation and switching happens in Rei.java.
     *
     * @param tasks the TaskList (ignored)
     * @param ui the Ui for displaying messages
     * @param storage the Storage system (ignored)
     * @throws ReiExceptions if there's an error
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        // Just show confirmation message - actual switching handled by Rei
        ui.showLine();
        ui.show("Creating new data file: " + newFilePath);
        ui.showLine();
    }

    /**
     * Gets the new file path for data source switching.
     *
     * @return the new file path
     */
    public String getNewFilePath() {
        return newFilePath;
    }
}
