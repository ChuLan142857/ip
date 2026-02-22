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
 * Command to archive completed tasks to a separate file.
 * Removes completed tasks from the active list and saves them to an archive file.
 */
public class ArchiveCommand extends Command {
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Executes the archive command by moving all completed tasks to an archive file.
     * Also saves the updated task list to storage and displays confirmation to the user.
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
        ArrayList<Task> completedTasks = new ArrayList<>();
        
        // Collect all completed tasks
        for (Task task : allTasks) {
            if (task.isDone()) {
                completedTasks.add(task);
            }
        }

        if (completedTasks.isEmpty()) {
            ui.showLine();
            ui.show("No completed tasks to archive.");
            ui.showLine();
            return;
        }

        // Generate archive file name with timestamp
        String archiveFileName = generateArchiveFileName(storage);
        
        // Write completed tasks to archive file
        writeToArchiveFile(archiveFileName, completedTasks);

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
        ui.show("Archived " + removedCount + " completed task(s) to:");
        ui.show(archiveFileName);
        ui.show("Your task list has been updated.");
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
