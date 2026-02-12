package rei.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline that must be completed by a specific date and time.
 * Extends the base Task class to include deadline functionality.
 */
public class Deadline extends Task {
    private LocalDateTime ddl;

    /**
     * Constructs a new Deadline task with the given description and deadline.
     *
     * @param description the description of the deadline task
     * @param ddl the deadline date and time when the task must be completed
     */
    public Deadline(String description, LocalDateTime ddl) {
        super(description);
        assert ddl != null : "Deadline date cannot be null";
        this.ddl = ddl;
    }

    /**
     * Returns a string representation of this deadline task for display.
     *
     * @return a formatted string with [D] prefix, status, description, and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + formatDate(ddl) + ")";
    }

    /**
     * Returns a string representation of this deadline task for file storage.
     *
     * @return a formatted string with D prefix and deadline information for saving to file
     */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString()
                + " | " + ddl;
    }

    /**
     * Formats a LocalDateTime into a user-friendly display format.
     *
     * @param dateTime the date and time to format
     * @return a formatted date string in "MMM dd yyyy HH:mm" format
     */
    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return dateTime.format(formatter);
    }
}
