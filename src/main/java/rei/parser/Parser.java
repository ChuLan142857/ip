package rei.parser;

import rei.exceptions.ReiExceptions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import rei.command.*;

/**
 * Parses user input strings and converts them into appropriate Command objects.
 * Handles the interpretation of various command formats and their parameters.
 */
public class Parser {

    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses the user input string and returns the corresponding Command object.
     *
     * @param input the user input string to parse
     * @return the Command object representing the user's intended action
     * @throws ReiExceptions if the input format is invalid or unrecognized
     */
    public static Command parse(String input) throws ReiExceptions {

        if (input.equals("bye")) {
            return new ByeCommand();
        }

        if (input.equals("list")) {
            return new ListCommand();
        }

        if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                throw new ReiExceptions("Find command requires a keyword.");
            }
            return new FindCommand(keyword);
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

    /**
     * Parses a task index from the user input string.
     * Converts from 1-based user input to 0-based array index.
     *
     * @param input the full input string
     * @param start the starting position to parse the index from
     * @return the 0-based index for accessing the task list
     * @throws ReiExceptions if the index is not a valid number
     */
    private static int parseIndex(String input, int start) throws ReiExceptions {
        try {
            return Integer.parseInt(input.substring(start)) - 1;
        }
        catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }

    /**
     * Parses a date string into a LocalDateTime object using the expected format.
     *
     * @param date the date string to parse in "yyyy-MM-dd HH:mm" format
     * @return the parsed LocalDateTime object
     * @throws ReiExceptions if the date string is not in the expected format
     */
    private static LocalDateTime parseDate (String date) throws ReiExceptions {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date, INPUT_FORMAT);
            return  dateTime;
        } catch (Exception e) {
            throw new ReiExceptions("Please use yyyy-MM-dd HH:mm format!");
        }
    }
}
