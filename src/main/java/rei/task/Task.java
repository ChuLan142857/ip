package rei.task;

/**
 * Abstract base class representing a task in the Rei application.
 * All specific task types (Todo, Deadline, Event) extend this class.
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructs a new task with the given description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        isDone = true
        ;
    }

    /**
     * Marks this task as not completed.
     */
    public void markUndone() {
        isDone = false;
    }

    /**
     * Checks if this task is completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets the done flag as a string for file storage.
     *
     * @return "1" if the task is done, "0" otherwise
     */
    public String getDoneFlag() {
        return isDone ? "1" : "0";
    }

    /**
     * Gets the visual status icon for displaying the task completion status.
     *
     * @return "[X]" if the task is done, "[ ]" otherwise
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Gets the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of this task for display purposes.
     *
     * @return a formatted string containing the status icon and description
     */
    @Override
    public String toString(){
        return getStatusIcon() + " " + description;
    }

    /**
     * Returns a string representation of this task for file storage.
     *
     * @return a formatted string suitable for saving to file
     */
    public String toFileString(){
        return getDoneFlag() + " | " + description;
    }
}
