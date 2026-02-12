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
    
    // Constants for file format and validation
    private static final String FIELD_SEPARATOR = " \\| ";
    private static final String DONE_FLAG_TRUE = "1";
    private static final String DONE_FLAG_FALSE = "0";
    
    // Constants for task type identifiers
    private static final String TODO_TYPE_IDENTIFIER = "T";
    private static final String DEADLINE_TYPE_IDENTIFIER = "D";
    private static final String EVENT_TYPE_IDENTIFIER = "E";
    
    // Constants for minimum required parts for each task type
    private static final int MINIMUM_TASK_PARTS = 3;
    private static final int MINIMUM_TODO_PARTS = 3;
    private static final int MINIMUM_DEADLINE_PARTS = 4;
    private static final int MINIMUM_EVENT_PARTS = 5;
    
    // Array indices for task parts
    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DEADLINE_DATE_INDEX = 3;
    private static final int EVENT_START_INDEX = 3;
    private static final int EVENT_END_INDEX = 4;

    /**
     * Constructs a new Storage instance with the specified file path.
     * Validates the file path to ensure it's not null or empty.
     *
     * @param filePath the path to the file used for storing tasks
     * @throws ReiExceptions if filePath is null or empty
     */
    public Storage(String filePath) throws ReiExceptions {
        validateFilePath(filePath);
        this.filePath = filePath;
    }
    
    /**
     * Validates the provided file path.
     * 
     * @param filePath the file path to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateFilePath(String filePath) throws ReiExceptions {
        if (filePath == null) {
            throw new ReiExceptions("File path cannot be null");
        }
        
        if (filePath.trim().isEmpty()) {
            throw new ReiExceptions("File path cannot be empty");
        }
    }

    /**
     * Loads tasks from the storage file.
     * Creates the file and parent directories if they don't exist.
     * Uses try-with-resources for proper resource management.
     *
     * @return an ArrayList of tasks loaded from the file
     * @throws ReiExceptions if there's an error reading from the file
     */
    public ArrayList<Task> load() throws ReiExceptions {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            ensureFileExists(file);
            
            // If file was just created, return empty list
            if (isFileEmpty(file)) {
                return tasks;
            }
            
            loadTasksFromFile(file, tasks);
            return tasks;
            
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to load tasks from file.");
        }
    }
    
    /**
     * Ensures the storage file and its parent directory exist.
     * 
     * @param file the file to ensure exists
     * @throws IOException if file creation fails
     */
    private void ensureFileExists(File file) throws IOException {
        File parentDirectory = file.getParentFile();
        
        if (parentDirectory != null && !parentDirectory.exists()) {
            boolean directoryCreated = parentDirectory.mkdirs();
            if (!directoryCreated) {
                throw new IOException("Failed to create parent directory");
            }
        }
        
        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Failed to create file");
            }
        }
    }
    
    /**
     * Checks if the file is empty or has no readable content.
     * 
     * @param file the file to check
     * @return true if file is empty, false otherwise
     */
    private boolean isFileEmpty(File file) {
        return file.length() == 0;
    }
    
    /**
     * Loads tasks from the file into the provided list.
     * 
     * @param file the file to read from
     * @param tasks the list to add tasks to
     * @throws IOException if file reading fails
     * @throws ReiExceptions if task parsing fails
     */
    private void loadTasksFromFile(File file, ArrayList<Task> tasks) throws IOException, ReiExceptions {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                Task task = parseTaskFromLine(line);
                
                if (task != null) {
                    tasks.add(task);
                }
            }
        }
    }

    /**
     * Saves the current task list to the storage file.
     * Uses try-with-resources for proper resource management.
     *
     * @param tasks the TaskList containing all tasks to save
     * @throws ReiExceptions if there's an error writing to the file
     */
    public void save(TaskList tasks) throws ReiExceptions {
        validateTaskListForSaving(tasks);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            ArrayList<Task> taskList = tasks.getAll();
            
            for (Task task : taskList) {
                String taskFileString = task.toFileString();
                writer.write(taskFileString);
                writer.newLine();
            }
            
        } catch (IOException e) {
            throw new ReiExceptions("OOPS!!! Unable to save tasks to file.");
        }
    }
    
    /**
     * Validates the task list before saving.
     * 
     * @param tasks the task list to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateTaskListForSaving(TaskList tasks) throws ReiExceptions {
        if (tasks == null) {
            throw new ReiExceptions("Task list cannot be null");
        }
    }

    /**
     * Parses a line from the storage file and creates the appropriate task object.
     * Validates input and delegates to specific parsing methods.
     *
     * @param line the line to parse from the file
     * @return the Task object created from the line, or null if line is invalid
     * @throws ReiExceptions if the line format is invalid or corrupted
     */
    private Task parseTaskFromLine(String line) throws ReiExceptions {
        if (line == null || line.trim().isEmpty()) {
            return null; // Skip empty lines
        }
        
        String[] parts = splitTaskLine(line);
        validateTaskParts(parts);
        
        String taskType = parts[TYPE_INDEX];
        
        // Use explicit if-else chain for better readability and explicit default handling
        if (TODO_TYPE_IDENTIFIER.equals(taskType)) {
            return createTodoFromParts(parts);
        } else if (DEADLINE_TYPE_IDENTIFIER.equals(taskType)) {
            return createDeadlineFromParts(parts);
        } else if (EVENT_TYPE_IDENTIFIER.equals(taskType)) {
            return createEventFromParts(parts);
        } else {
            // Explicit default case for unknown task types
            throw new ReiExceptions("OOPS!!! Unknown task type in data file: " + taskType);
        }
    }
    
    /**
     * Splits a task line into its component parts.
     * 
     * @param line the line to split
     * @return array of parts
     */
    private String[] splitTaskLine(String line) {
        return line.split(FIELD_SEPARATOR);
    }
    
    /**
     * Validates that task parts meet minimum requirements.
     * 
     * @param parts the parsed parts to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateTaskParts(String[] parts) throws ReiExceptions {
        if (parts.length < MINIMUM_TASK_PARTS) {
            throw new ReiExceptions("OOPS!!! Corrupted data file - insufficient parts.");
        }
    }

    /**
     * Creates a Todo task from parsed file data.
     * Validates input parts before creating the task.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Todo task
     * @throws ReiExceptions if parts are invalid
     */
    private Task createTodoFromParts(String[] parts) throws ReiExceptions {
        validateTodoParts(parts);
        
        String description = parts[DESCRIPTION_INDEX];
        String statusFlag = parts[STATUS_INDEX];
        
        Task todoTask = new Todo(description);
        setTaskCompletionStatus(todoTask, statusFlag);
        return todoTask;
    }
    
    /**
     * Validates parts for Todo task creation.
     * 
     * @param parts the parts to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateTodoParts(String[] parts) throws ReiExceptions {
        if (parts.length < MINIMUM_TODO_PARTS) {
            throw new ReiExceptions("OOPS!!! Invalid Todo format in data file.");
        }
        
        if (!TODO_TYPE_IDENTIFIER.equals(parts[TYPE_INDEX])) {
            throw new ReiExceptions("OOPS!!! Expected Todo type identifier.");
        }
    }

    /**
     * Creates a Deadline task from parsed file data.
     * Validates input parts before creating the task.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Deadline task
     * @throws ReiExceptions if parts are invalid
     */
    private Task createDeadlineFromParts(String[] parts) throws ReiExceptions {
        validateDeadlineParts(parts);
        
        String description = parts[DESCRIPTION_INDEX];
        String deadlineDateString = parts[DEADLINE_DATE_INDEX];
        String statusFlag = parts[STATUS_INDEX];
        
        try {
            LocalDateTime deadlineDateTime = LocalDateTime.parse(deadlineDateString);
            Task deadlineTask = new Deadline(description, deadlineDateTime);
            setTaskCompletionStatus(deadlineTask, statusFlag);
            return deadlineTask;
        } catch (Exception e) {
            throw new ReiExceptions("OOPS!!! Invalid deadline date format in data file.");
        }
    }
    
    /**
     * Validates parts for Deadline task creation.
     * 
     * @param parts the parts to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateDeadlineParts(String[] parts) throws ReiExceptions {
        if (parts.length < MINIMUM_DEADLINE_PARTS) {
            throw new ReiExceptions("OOPS!!! Invalid Deadline format in data file.");
        }
        
        if (!DEADLINE_TYPE_IDENTIFIER.equals(parts[TYPE_INDEX])) {
            throw new ReiExceptions("OOPS!!! Expected Deadline type identifier.");
        }
    }

    /**
     * Creates an Event task from parsed file data.
     * Validates input parts and date logic before creating the task.
     *
     * @param parts the parsed components from the file line
     * @return the constructed Event task
     * @throws ReiExceptions if parts are invalid
     */
    private Task createEventFromParts(String[] parts) throws ReiExceptions {
        validateEventParts(parts);
        
        String description = parts[DESCRIPTION_INDEX];
        String startDateString = parts[EVENT_START_INDEX];
        String endDateString = parts[EVENT_END_INDEX];
        String statusFlag = parts[STATUS_INDEX];
        
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startDateString);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateString);
            
            validateEventDateLogic(startDateTime, endDateTime);
            
            Task eventTask = new Event(description, startDateTime, endDateTime);
            setTaskCompletionStatus(eventTask, statusFlag);
            return eventTask;
        } catch (Exception e) {
            throw new ReiExceptions("OOPS!!! Invalid event date format in data file.");
        }
    }
    
    /**
     * Validates parts for Event task creation.
     * 
     * @param parts the parts to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateEventParts(String[] parts) throws ReiExceptions {
        if (parts.length < MINIMUM_EVENT_PARTS) {
            throw new ReiExceptions("OOPS!!! Invalid Event format in data file.");
        }
        
        if (!EVENT_TYPE_IDENTIFIER.equals(parts[TYPE_INDEX])) {
            throw new ReiExceptions("OOPS!!! Expected Event type identifier.");
        }
    }
    
    /**
     * Validates that event start time is not after end time.
     * 
     * @param startDateTime the start date and time
     * @param endDateTime the end date and time
     * @throws ReiExceptions if start is after end
     */
    private void validateEventDateLogic(LocalDateTime startDateTime, LocalDateTime endDateTime) 
            throws ReiExceptions {
        if (startDateTime.isAfter(endDateTime)) {
            throw new ReiExceptions("OOPS!!! Event start time cannot be after end time.");
        }
    }

    /**
     * Sets the completion status of a task based on the done flag from file.
     * Validates the done flag before setting status.
     *
     * @param task the task to update
     * @param doneFlag "1" if task is done, "0" if not done
     * @throws ReiExceptions if done flag is invalid
     */
    private void setTaskCompletionStatus(Task task, String doneFlag) throws ReiExceptions {
        validateTaskCompletionFlag(task, doneFlag);
        
        if (DONE_FLAG_TRUE.equals(doneFlag)) {
            task.markDone();
        }
        // If flag is "0", task remains not done (default state)
    }
    
    /**
     * Validates task and completion flag before setting status.
     * 
     * @param task the task to validate
     * @param doneFlag the flag to validate
     * @throws ReiExceptions if validation fails
     */
    private void validateTaskCompletionFlag(Task task, String doneFlag) throws ReiExceptions {
        if (task == null) {
            throw new ReiExceptions("Task cannot be null");
        }
        
        if (doneFlag == null) {
            throw new ReiExceptions("Done flag cannot be null");
        }
        
        if (!DONE_FLAG_TRUE.equals(doneFlag) && !DONE_FLAG_FALSE.equals(doneFlag)) {
            throw new ReiExceptions("OOPS!!! Invalid completion status in data file: " + doneFlag);
        }
    }
}

