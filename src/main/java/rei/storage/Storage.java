package rei.storage;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import rei.task.*;
import rei.list.TaskList;
import rei.exceptions.ReiExceptions;

/**
 * Handles file operations for storing and loading tasks.
 * Manages persistence of task data to and from the file system.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a new Storage instance with the specified file path.
     *
     * @param filePath the path to the file used for storing tasks
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * Creates the file and parent directories if they don't exist.
     *
     * @return an ArrayList of tasks loaded from the file
     * @throws ReiExceptions if there's an error reading from the file
     */
    public ArrayList<Task> load() throws ReiExceptions {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);

                if (task != null){
                    tasks.add(task);
                }
            }
            reader.close();
            return tasks;

        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to load tasks from file.");
        }
    }

    /**
     * Saves the current task list to the storage file.
     *
     * @param tasks the TaskList containing all tasks to save
     * @throws ReiExceptions if there's an error writing to the file
     */
    public void save(TaskList tasks) throws ReiExceptions {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            ArrayList<Task> list = tasks.getAll();
            for (Task task : list) {
                writer.write(task.toFileString());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to save tasks to file.");
        }
    }

    /**
     * Parses a line from the storage file and creates the appropriate task object.
     *
     * @param line the line to parse from the file
     * @return the Task object created from the line
     * @throws ReiExceptions if the line format is invalid or corrupted
     */
    private Task parseTask(String line) throws ReiExceptions {
        String[] parts = line.split(" \\| ");

        switch (parts[0]) {
            case "T":
                return loadTodo(parts);
            case "D":
                return loadDeadline(parts);
            case "E":
                return loadEvent(parts);
            default:
                throw new ReiExceptions("OOPS!!! Corrupted data file.");
        }
    }

    /**
     * Creates a Todo task from parsed file data.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Todo task
     */
    private Task loadTodo(String[] parts)
    {
        Task t = new Todo(parts[2]);
        setDone(t, parts[1]);
        return t;
    }

    /**
     * Creates a Deadline task from parsed file data.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Deadline task
     */
    private Task loadDeadline(String[] parts)
    {
        LocalDateTime by = LocalDateTime.parse(parts[3]);
        Task d = new Deadline(parts[2], by);
        setDone(d, parts[1]);
        return d;
    }

    /**
     * Creates an Event task from parsed file data.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Event task
     */
    private Task loadEvent(String[] parts)
    {
        LocalDateTime from = LocalDateTime.parse(parts[3]);
        LocalDateTime to = LocalDateTime.parse(parts[4]);
        Task e = new Event(parts[2], from, to);
        setDone(e, parts[1]);
        return e;
    }

    /**
     * Sets the completion status of a task based on the done flag from file.
     *
     * @param task the task to update
     * @param doneFlag "1" if task is done, "0" if not done
     */
    private void setDone(Task task, String doneFlag)
    {
        if (doneFlag.equals("1")) {
            task.markDone();
        }
    }
}

