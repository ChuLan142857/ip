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
 * Command to archive all tasks to a separate file and start with a clean slate.
 * Saves all tasks (completed and non-completed) to an archive file and clears the task list.
 */
public class ArchiveCommand extends Command {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Executes the archive command by saving all tasks to an archive file.
     * Clears the task list completely for a fresh start.
     *
     * @param tasks the TaskList to archive from
     * @param ui the Ui for displaying confirmation messages
     * @param storage the Storage for persisting the updated task list
     * @throws ReiExceptions if there's an error writing to the archive file or saving tasks
     */
    @Override
    public void execute(TaskList tasks, UiInterface ui, Storage storage)
            throws ReiExceptions {

        ArrayList<Task> allTasks = tasks.getAll();

        if (allTasks.isEmpty()) {
            ui.showLine();
            ui.show("No tasks to archive. Your list is already empty.");
            ui.showLine();
            return;
        }

        // Generate archive file name with timestamp
        String archiveFileName = generateArchiveFileName(storage);
        
        // Write all tasks to archive file
        writeToArchiveFile(archiveFileName, allTasks);

        int totalCount = allTasks.size();

        // Remove all tasks from the task list
        for (int i = allTasks.size() - 1; i >= 0; i--) {
            tasks.remove(i);
        }

        // Save the empty task list
        storage.save(tasks);

        ui.showLine();
        ui.show("Archived " + totalCount + " task(s) to:");
        ui.show(archiveFileName);
        ui.show("Your task list has been cleared. Starting with a clean slate!");
        ui.showLine();
    }

    /**
     * Generates an archive file name based on the current storage file path.
     * 
     * @param storage the Storage instance
     * @return the archive file name with timestamp
     */
    private String generateArchiveFileName(Storage storage) {
        String currentFilePath = storage.getFilePath();
        File file = new File(currentFilePath);
        String directory = file.getParent();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        
        String archiveFileName = "Rei_archive_" + timestamp + ".txt";
        
        if (directory != null) {
            return directory + File.separator + archiveFileName;
        }
        return archiveFileName;
    }

    /**
     * Writes tasks to the archive file.
     * 
     * @param archiveFileName the path to the archive file
     * @param tasks the tasks to archive
     * @throws ReiExceptions if there's an error writing to the file
     */
    private void writeToArchiveFile(String archiveFileName, ArrayList<Task> tasks) 
            throws ReiExceptions {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archiveFileName))) {
            for (Task task : tasks) {
                String taskFileString = task.toFileString();
                writer.write(taskFileString);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to write to archive file: " + archiveFileName);
        }
    }
}
