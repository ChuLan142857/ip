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
        assert filePath != null : "File path cannot be null";
        assert !filePath.trim().isEmpty() : "File path cannot be empty";
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
        assert line != null : "Line to parse cannot be null";
        assert !line.trim().isEmpty() : "Line to parse cannot be empty";
        
        String[] parts = line.split(" \\| ");
        assert parts.length >= 3 : "Parsed line should have at least 3 parts (type, status, description)";

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
        assert parts != null : "Parts array cannot be null";
        assert parts.length >= 3 : "Todo requires at least 3 parts: type, status, description";
        assert "T".equals(parts[0]) : "First part should be 'T' for Todo";
        assert parts[1] != null : "Status part cannot be null";
        assert parts[2] != null : "Description part cannot be null";
        
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
        assert parts != null : "Parts array cannot be null";
        assert parts.length >= 4 : "Deadline requires at least 4 parts: type, status, description, deadline";
        assert "D".equals(parts[0]) : "First part should be 'D' for Deadline";
        assert parts[1] != null : "Status part cannot be null";
        assert parts[2] != null : "Description part cannot be null";
        assert parts[3] != null : "Deadline part cannot be null";
        
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
        assert parts != null : "Parts array cannot be null";
        assert parts.length >= 5 : "Event requires at least 5 parts: type, status, description, start, end";
        assert "E".equals(parts[0]) : "First part should be 'E' for Event";
        assert parts[1] != null : "Status part cannot be null";
        assert parts[2] != null : "Description part cannot be null";
        assert parts[3] != null : "Start time part cannot be null";
        assert parts[4] != null : "End time part cannot be null";
        
        LocalDateTime from = LocalDateTime.parse(parts[3]);
        LocalDateTime to = LocalDateTime.parse(parts[4]);
        assert !from.isAfter(to) : "Event start time should not be after end time";
        
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
        assert task != null : "Task cannot be null";
        assert doneFlag != null : "Done flag cannot be null";
        assert "0".equals(doneFlag) || "1".equals(doneFlag) : "Done flag must be '0' or '1'";
        
        if (doneFlag.equals("1")) {
            task.markDone();
        }
    }
}

