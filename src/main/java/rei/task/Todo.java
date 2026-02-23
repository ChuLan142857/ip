package rei.task;

/**
 * Represents a simple todo task without any time constraints.
 * Extends the base Task class to provide todo-specific functionality.
 */
public class Todo extends Task {

    /**
     * Constructs a new Todo task with the given description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this todo task for display.
     *
     * @return a formatted string with [T] prefix, status icon, and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a string representation of this todo task for file storage.
     *
     * @return a formatted string with T prefix for saving to file
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString() + getTagsForFile();
    }

    /**
     * Checks if this todo is equal to another object.
     * Two todos are equal if they are both Todo instances with the same description.
     *
     * @param obj the object to compare with
     * @return true if the todos are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Todo)) {
            return false;
        }
        Todo other = (Todo) obj;
        return this.getDescription().equals(other.getDescription());
    }

    /**
     * Returns a hash code for this todo based on its description.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        return getDescription().hashCode();
    }
}
