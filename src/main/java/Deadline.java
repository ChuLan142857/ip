import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDateTime ddl;

    public Deadline(String description, LocalDateTime ddl){
        super(description);
        this.ddl = ddl;
    }

    @Override
    public String toString(){
        return "[D]" + super.toString()
                + " (by: " + formatDate(ddl) + ")";
    }

    @Override
    public String toFileString(){
        return "D | " + super.toFileString()
                + " | " + ddl;
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return dateTime.format(formatter);
    }
}
