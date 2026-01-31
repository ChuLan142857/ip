import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end){
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString(){
        return "[E]" + super.toString()
                + " (from: " + format(start) + " to: " + format(end) + ")";
    }

    @Override
    public String toFileString(){
        return "E | " + super.toFileString()
                + " | " + start + " | " + end;
    }

    private String format(LocalDateTime dt)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return dt.format(formatter);
    }
}
