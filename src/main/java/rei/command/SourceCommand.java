package rei.command;

import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

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
     * or shows the file path for switching (actual switch handled by Rei).
     *
     * @param tasks the TaskList (will be ignored for queries)
     * @param ui the Ui for displaying messages
     * @param storage the Storage system
     * @throws ReiExceptions if there's an error accessing storage information
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {
        ui.showLine();
        
        if (isQuery) {
            // Display current data source
            ui.show("Current data source: " + storage.getFilePath());
        } else {
            // Just acknowledge the request - actual switching happens in Rei
            ui.show("Switching data source to: " + newFilePath);
            ui.show("Your current tasks will be saved before switching.");
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
