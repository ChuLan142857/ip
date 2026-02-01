package rei.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs during a specific time period.
 * Extends the base Task class to include start and end time functionality.
 */
public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Constructs a new Event task with the given description, start time, and end time.
     *
     * @param description the description of the event
     * @param start the start date and time of the event
     * @param end the end date and time of the event
     */
    public Event(String description, LocalDateTime start, LocalDateTime end){
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of this event task for display.
     *
     * @return a formatted string with [E] prefix, status, description, and time period
     */
    @Override
    public String toString(){
        return "[E]" + super.toString()
                + " (from: " + format(start) + " to: " + format(end) + ")";
    }

    /**
     * Returns a string representation of this event task for file storage.
     *
     * @return a formatted string with E prefix and time information for saving to file
     */
    @Override
    public String toFileString(){
        return "E | " + super.toFileString()
                + " | " + start + " | " + end;
    }

    /**
     * Formats a LocalDateTime into a user-friendly display format.
     *
     * @param dt the date and time to format
     * @return a formatted date string in "MMM dd yyyy HH:mm" format
     */
    private String format(LocalDateTime dt)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return dt.format(formatter);
    }
}
