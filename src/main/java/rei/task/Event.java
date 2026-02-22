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
    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        assert start != null : "Event start time cannot be null";
        assert end != null : "Event end time cannot be null";
        assert !start.isAfter(end) : "Event start time should not be after end time";
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of this event task for display.
     *
     * @return a formatted string with [E] prefix, status, description, and time period
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + format(start) + " to: " + format(end) + ")";
    }

    /**
     * Returns a string representation of this event task for file storage.
     *
     * @return a formatted string with E prefix and time information for saving to file
     */
    @Override
    public String toFileString() {
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

    /**
     * Checks if this event is equal to another object.
     * Two events are equal if they have the same description, start time, and end time.
     *
     * @param obj the object to compare with
     * @return true if the events are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Event)) {
            return false;
        }
        Event other = (Event) obj;
        return this.getDescription().equals(other.getDescription())
                && this.start.equals(other.start)
                && this.end.equals(other.end);
    }

    /**
     * Returns a hash code for this event based on its description, start, and end times.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        int result = getDescription().hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        return result;
    }

    /**
     * Gets the start date and time of the event.
     *
     * @return the start date and time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Gets the end date and time of the event.
     *
     * @return the end date and time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Sets the start date and time of the event.
     *
     * @param start the new start date and time
     */
    public void setStart(LocalDateTime start) {
        assert start != null : "Event start time cannot be null";
        this.start = start;
    }

    /**
     * Sets the end date and time of the event.
     *
     * @param end the new end date and time
     */
    public void setEnd(LocalDateTime end) {
        assert end != null : "Event end time cannot be null";
        this.end = end;
    }
}
