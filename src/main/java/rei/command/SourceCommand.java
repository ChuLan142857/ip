package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;
import rei.exceptions.ConfirmationRequiredException;

import java.io.File;

/**
 * Command to switch the data source file or display the current data source.
 * If provided with a file path, requests a switch to that file.
 * If no file path is provided, displays the current data source being used.
 */
public class SourceCommand extends Command {
    private final String newFilePath;
    private final boolean isQuery;

    /**
     * Constructs a new SourceCommand  to switch data sources.
     *
     * @param newFilePath the path to the new data file
     */
    public SourceCommand(String newFilePath) {
        this.newFilePath = newFilePath;
        this.isQuery = false;
    }

    /**
     * Constructs a new SourceCommand to query the current data source.
     */
    public SourceCommand() {
        this.newFilePath = null;
        this.isQuery = true;
    }

    /**
     * Executes the source command. Either displays the current data source
     * or checks if the new file exists before switching.
     *
     * @param tasks the TaskList (will be ignored for queries)
     * @param ui the Ui for displaying messages
     * @param storage the Storage system
     * @throws ReiExceptions if there's an error accessing storage information
     * @throws ConfirmationRequiredException if the new file doesn't exist
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        ui.showLine();
        
        if (isQuery) {
            // Display current data source
            ui.show("Current data source: " + storage.getFilePath());
        } else {
            // Check if file exists
            File newFile = new File(newFilePath);
            if (!newFile.exists()) {
                // File doesn't exist - ask for confirmation to create
                PendingSourceCommand pendingCommand = new PendingSourceCommand(newFilePath);
                throw new ConfirmationRequiredException(
                    "The file '" + newFilePath + "' doesn't exist.\n" +
                    "Do you want to create a new data file at this location?\n" +
                    "Answer me with yes or no (y/n)",
                    pendingCommand
                );
            }
            
            // File exists - just acknowledge the request
            ui.show("Switching data source to: " + newFilePath);
            ui.show("Don't worry, your current tasks will be saved before switching.");
        }
        
        ui.showLine();
    }

    /**
     * Gets the new file path for data source switching.
     *
     * @return the new file path, or null if this is a query command
     */
    public String getNewFilePath() {
        return newFilePath;
    }

    /**
     * Checks if this is a query command (just displaying current source).
     *
     * @return true if querying current source, false if switching
     */
    public boolean isQuery() {
        return isQuery;
    }
}
