import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser {

    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Command parse(String input) throws ReiExceptions {

        if (input.equals("bye")) {
            return new ByeCommand();
        }

        if (input.equals("list")) {
            return new ListCommand();
        }

        if (input.startsWith("todo")) {
            if (input.length() <= 5) {
                throw new ReiExceptions("OOPS!!! The description of a todo cannot be empty.");
            }
            return new TodoCommand(input.substring(5));
        }

        if (input.startsWith("deadline")) {
            if (!input.contains("/by")) {
                throw new ReiExceptions("OOPS!!! Deadline must have /by.");
            }

            String[] parts = input.split("/by");
            String description = parts[0].replace("deadline", "").trim();

            if (description.isEmpty()) {
                throw new ReiExceptions("OOPS!!! The description cannot be empty.");
            }

            try {
                LocalDateTime ddl = parseDate(parts[1].trim());
                return new DeadlineCommand(description, ddl);
            } catch (Exception e) {
                throw new ReiExceptions("OOPS!!! Please use yyyy-MM-dd HH:mm format.");
            }
        }

        if (input.startsWith("event")) {
            String[] parts = input.split(" /from | /to ");
            if (parts.length < 3) {
                throw new ReiExceptions("OOPS!!! An event must have /from and /to.");
            }
            LocalDateTime start = parseDate(parts[1]);
            LocalDateTime end = parseDate(parts[2]);
            return new EventCommand(parts[0].substring(6),
                    start,
                    end);
        }

        if (input.startsWith("mark ")) {
            return new MarkCommand(parseIndex(input, 5));
        }

        if (input.startsWith("unmark ")) {
            return new UnmarkCommand(parseIndex(input, 7));
        }

        if (input.startsWith("delete ")) {
            return new DeleteCommand(parseIndex(input, 7));
        }

        throw new ReiExceptions("OOPS!!! I'm sorry, but I don't know what that means :-(");

    }

    private static int parseIndex(String input, int start) throws ReiExceptions {
        try {
            return Integer.parseInt(input.substring(start)) - 1;
        }
        catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }

    private static LocalDateTime parseDate (String date) throws ReiExceptions {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, INPUT_FORMAT);
            return  dateTime;
        } catch (Exception e) {
            throw new ReiExceptions("Please use yyyy-MM-dd HH:mm format!");
        }
    }
}
