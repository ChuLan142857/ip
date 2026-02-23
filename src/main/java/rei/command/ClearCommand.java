package rei.command;

import rei.task.Task;
import rei.list.TaskList;
import rei.ui.UiInterface;
import rei.storage.Storage;
import rei.exceptions.ReiExceptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Command to clear completed tasks from the list and record them.
 * Removes completed tasks from the active list and appends them to a completed tasks file.
 */
public class ClearCommand extends Command {
    private static final String COMPLETED_FILE_NAME = "Rei_completed.txt";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Executes the clear command by removing all completed tasks.
     * Appends completed tasks to a record file and removes them from the active list.
     *
     * @param tasks the TaskList to clear from
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error writing to the completed file or saving tasks
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        ArrayList<Task> allTasks = tasks.getAll();
        ArrayList<Task> completedTasks = new ArrayList<>();
        
        // Collect all completed tasks
        for (Task task : allTasks) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }

        if (completedTasks.isEmpty()) {
            ui.showLine();
            ui.show("There are no completed tasks to clear. \n Wish I could help with your work.");
            ui.showLine();
            return;
        }

        // Get completed file path
        String completedFilePath = getCompletedFilePath(storage);
        
        // Append completed tasks to the completed tasks file
        appendToCompletedFile(completedFilePath, completedTasks);

        // Remove completed tasks from the task list
        int removedCount = 0;
        for (int i = allTasks.size() - 1; i >= 0; i--) {
            if (allTasks.get(i).isDone()) {
                tasks.remove(i);
                removedCount++;
            }
        }

        // Save the updated task list
        storage.save(tasks);

        ui.showLine();
        ui.show("Cleared " + removedCount + " completed task(s) and recorded in:");
        ui.show(normalizePathDisplay(completedFilePath));
        ui.show("Your active task list has been updated. Keep on fighting.");
        ui.showLine();
    }

    /**
     * Gets the file path for the completed tasks file.
     * 
     * @param storage the Storage instance
     * @return the completed tasks file path
     */
    private String getCompletedFilePath(Storage storage) {
        String currentFilePath = storage.getFilePath();
        File file = new File(currentFilePath);
        String directory = file.getParent();
        
        if (directory != null) {
            return directory + File.separator + COMPLETED_FILE_NAME;
        }
        return COMPLETED_FILE_NAME;
    }

    /**
     * Normalizes file path display to use forward slashes for consistency.
     * 
     * @param filePath the file path to normalize
     * @return the normalized path with forward slashes
     */
    private String normalizePathDisplay(String filePath) {
        return filePath.replace(File.separator, "/");
    }

    /**
     * Appends completed tasks to the completed tasks file with timestamp.
     * Creates the file if it doesn't exist.
     * 
     * @param completedFilePath the path to the completed tasks file
     * @param tasks the tasks to append
     * @throws ReiExceptions if there's an error writing to the file
     */
    private void appendToCompletedFile(String completedFilePath, ArrayList<Task> tasks) 
            throws ReiExceptions {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(completedFilePath, true))) {
            // Add a timestamp header
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            writer.write("// Cleared on " + timestamp);
            writer.newLine();
            
            // Write each completed task
            for (Task task : tasks) {
                String taskFileString = task.toFileString();
                writer.write(taskFileString);
                writer.newLine();
            }
            
            // Add a blank line for separation
            writer.newLine();
        } catch (IOException e) {
            throw new ReiExceptions("Error detected: Unable to write to completed tasks file: " + completedFilePath);
        }
    }
}
