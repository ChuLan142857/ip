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
    public String toString(){
        return "[T]" + super.toString();
    }

    /**
     * Returns a string representation of this todo task for file storage.
     *
     * @return a formatted string with T prefix for saving to file
     */
    @Override
    public String toFileString(){
        return "T | " + super.toFileString();
    }
}
