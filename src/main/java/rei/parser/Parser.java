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
    
    // Constants for string parsing offsets to avoid magic numbers
    private static final int FIND_COMMAND_PREFIX_LENGTH = 5;
    private static final int TODO_COMMAND_PREFIX_LENGTH = 5;
    private static final int EVENT_COMMAND_PREFIX_LENGTH = 6;
    private static final int MARK_COMMAND_PREFIX_LENGTH = 5;
    private static final int UNMARK_COMMAND_PREFIX_LENGTH = 7;
    private static final int DELETE_COMMAND_PREFIX_LENGTH = 7;
    
    // Constants for validation requirements
    private static final int MINIMUM_EVENT_PARTS = 3;
    private static final String DEADLINE_SEPARATOR = "/by";
    private static final String EVENT_SEPARATOR_REGEX = " /from | /to ";
    
    // Command keywords
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String FIND_COMMAND_PREFIX = "find ";
    private static final String TODO_COMMAND_PREFIX = "todo";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline";
    private static final String EVENT_COMMAND_PREFIX = "event";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String DELETE_COMMAND_PREFIX = "delete ";

    /**
     * Parses the user input string and returns the corresponding Command object.
     * Delegates to specific parsing methods for better code organization.
     *
     * @param input the user input string to parse
     * @return the Command object representing the user's intended action
     * @throws ReiExceptions if the input format is invalid or unrecognized
     */
    public static Command parse(String input) throws ReiExceptions {
        String processedInput = validateAndCleanInput(input);
        
        // Handle simple commands first (happy path)
        Command simpleCommand = tryParseSimpleCommands(processedInput);
        if (simpleCommand != null) {
            return simpleCommand;
        }
        
        // Handle parameterized commands
        Command parameterizedCommand = tryParseParameterizedCommands(processedInput);
        if (parameterizedCommand != null) {
            return parameterizedCommand;
        }
        
        // Default case for unrecognized commands
        throw new ReiExceptions("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }
    
    /**
     * Validates input and returns cleaned input string.
     * 
     * @param input the raw input string
     * @return cleaned and validated input string
     * @throws ReiExceptions if input is invalid
     */
    private static String validateAndCleanInput(String input) throws ReiExceptions {
        if (input == null) {
            throw new ReiExceptions("Input string cannot be null");
        }
        
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new ReiExceptions("Input string cannot be empty");
        }
        
        return trimmedInput;
    }
    
    /**
     * Attempts to parse simple commands that don't require parameters.
     * 
     * @param input the cleaned input string
     * @return Command object if matched, null otherwise
     */
    private static Command tryParseSimpleCommands(String input) {
        if (input.equals(BYE_COMMAND)) {
            return new ByeCommand();
        }
        
        if (input.equals(LIST_COMMAND)) {
            return new ListCommand();
        }
        
        return null; // No simple command matches
    }
    
    /**
     * Attempts to parse commands that require parameters.
     * 
     * @param input the cleaned input string
     * @return Command object if matched, null otherwise
     * @throws ReiExceptions if command format is invalid
     */
    private static Command tryParseParameterizedCommands(String input) throws ReiExceptions {
        if (input.startsWith(FIND_COMMAND_PREFIX)) {
            return parseFindCommand(input);
        }
        
        if (input.startsWith(TODO_COMMAND_PREFIX)) {
            return parseTodoCommand(input);
        }
        
        if (input.startsWith(DEADLINE_COMMAND_PREFIX)) {
            return parseDeadlineCommand(input);
        }
        
        if (input.startsWith(EVENT_COMMAND_PREFIX)) {
            return parseEventCommand(input);
        }
        
        if (input.startsWith(MARK_COMMAND_PREFIX)) {
            return parseMarkCommand(input);
        }
        
        if (input.startsWith(UNMARK_COMMAND_PREFIX)) {
            return parseUnmarkCommand(input);
        }
        
        if (input.startsWith(DELETE_COMMAND_PREFIX)) {
            return parseDeleteCommand(input);
        }
        
        return null; // No parameterized command matches
    }
    
    /**
     * Parses find command from input string.
     * 
     * @param input the input string starting with "find "
     * @return FindCommand object
     * @throws ReiExceptions if keyword is missing
     */
    private static Command parseFindCommand(String input) throws ReiExceptions {
        String keyword = extractSubstring(input, FIND_COMMAND_PREFIX_LENGTH);
        
        if (keyword.isEmpty()) {
            throw new ReiExceptions("Find command requires a keyword.");
        }
        
        return new FindCommand(keyword);
    }
    
    /**
     * Parses todo command from input string.
     * 
     * @param input the input string starting with "todo"
     * @return TodoCommand object
     * @throws ReiExceptions if description is empty
     */
    private static Command parseTodoCommand(String input) throws ReiExceptions {
        if (input.length() <= TODO_COMMAND_PREFIX_LENGTH) {
            throw new ReiExceptions("OOPS!!! The description of a todo cannot be empty.");
        }
        
        String description = extractSubstring(input, TODO_COMMAND_PREFIX_LENGTH);
        return new TodoCommand(description);
    }
    
    /**
     * Parses deadline command from input string.
     * 
     * @param input the input string starting with "deadline"
     * @return DeadlineCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parseDeadlineCommand(String input) throws ReiExceptions {
        if (!input.contains(DEADLINE_SEPARATOR)) {
            throw new ReiExceptions("OOPS!!! Deadline must have /by.");
        }
        
        String[] parts = input.split(DEADLINE_SEPARATOR);
        String description = parts[0].replace(DEADLINE_COMMAND_PREFIX, "").trim();
        
        if (description.isEmpty()) {
            throw new ReiExceptions("OOPS!!! The description cannot be empty.");
        }
        
        try {
            LocalDateTime deadlineDateTime = parseDate(parts[1].trim());
            return new DeadlineCommand(description, deadlineDateTime);
        } catch (Exception e) {
            throw new ReiExceptions("OOPS!!! Please use yyyy-MM-dd HH:mm format.");
        }
    }
    
    /**
     * Parses event command from input string.
     * 
     * @param input the input string starting with "event"
     * @return EventCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parseEventCommand(String input) throws ReiExceptions {
        String[] parts = input.split(EVENT_SEPARATOR_REGEX);
        
        if (parts.length < MINIMUM_EVENT_PARTS) {
            throw new ReiExceptions("OOPS!!! An event must have /from and /to.");
        }
        
        String description = parts[0].substring(EVENT_COMMAND_PREFIX_LENGTH).trim();
        LocalDateTime startDateTime = parseDate(parts[1].trim());
        LocalDateTime endDateTime = parseDate(parts[2].trim());
        
        return new EventCommand(description, startDateTime, endDateTime);
    }
    
    /**
     * Parses mark command from input string.
     * 
     * @param input the input string starting with "mark "
     * @return MarkCommand object
     * @throws ReiExceptions if index is invalid
     */
    private static Command parseMarkCommand(String input) throws ReiExceptions {
        int taskIndex = parseIndex(input, MARK_COMMAND_PREFIX_LENGTH);
        return new MarkCommand(taskIndex);
    }
    
    /**
     * Parses unmark command from input string.
     * 
     * @param input the input string starting with "unmark "
     * @return UnmarkCommand object
     * @throws ReiExceptions if index is invalid
     */
    private static Command parseUnmarkCommand(String input) throws ReiExceptions {
        int taskIndex = parseIndex(input, UNMARK_COMMAND_PREFIX_LENGTH);
        return new UnmarkCommand(taskIndex);
    }
    
    /**
     * Parses delete command from input string.
     * 
     * @param input the input string starting with "delete "
     * @return DeleteCommand object
     * @throws ReiExceptions if index is invalid
     */
    private static Command parseDeleteCommand(String input) throws ReiExceptions {
        int taskIndex = parseIndex(input, DELETE_COMMAND_PREFIX_LENGTH);
        return new DeleteCommand(taskIndex);
    }
    
    /**
     * Extracts substring starting from given position with proper trimming.
     * 
     * @param input the input string
     * @param startPosition the starting position
     * @return trimmed substring
     */
    private static String extractSubstring(String input, int startPosition) {
        if (startPosition >= input.length()) {
            return "";
        }
        return input.substring(startPosition).trim();
    }

    /**
     * Parses a task index from the user input string.
     * Converts from 1-based user input to 0-based array index.
     * Validates input thoroughly before parsing.
     *
     * @param input the full input string
     * @param startPosition the starting position to parse the index from
     * @return the 0-based index for accessing the task list
     * @throws ReiExceptions if the index is not a valid number or input is invalid
     */
    private static int parseIndex(String input, int startPosition) throws ReiExceptions {
        validateIndexParsingInput(input, startPosition);
        
        String indexString = extractSubstring(input, startPosition);
        
        if (indexString.isEmpty()) {
            throw new ReiExceptions("OOPS!!! Task number cannot be empty.");
        }
        
        try {
            int userProvidedIndex = Integer.parseInt(indexString);
            int arrayIndex = convertToArrayIndex(userProvidedIndex);
            return arrayIndex;
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }
    
    /**
     * Validates input parameters for index parsing.
     * 
     * @param input the input string to validate
     * @param startPosition the start position to validate
     * @throws ReiExceptions if validation fails
     */
    private static void validateIndexParsingInput(String input, int startPosition) throws ReiExceptions {
        if (input == null) {
            throw new ReiExceptions("Input string cannot be null");
        }
        
        if (startPosition < 0) {
            throw new ReiExceptions("Start position cannot be negative");
        }
        
        if (startPosition >= input.length()) {
            throw new ReiExceptions("Start position exceeds input string length");
        }
    }
    
    /**
     * Converts 1-based user index to 0-based array index.
     * 
     * @param userIndex the 1-based index from user input
     * @return the 0-based array index
     * @throws ReiExceptions if user index is invalid
     */
    private static int convertToArrayIndex(int userIndex) throws ReiExceptions {
        if (userIndex <= 0) {
            throw new ReiExceptions("OOPS!!! Task number must be positive.");
        }
        
        return userIndex - 1; // Convert to 0-based indexing
    }

    /**
     * Parses a date string into a LocalDateTime object using the expected format.
     * Provides clear error messages and proper validation.
     *
     * @param dateString the date string to parse in "yyyy-MM-dd HH:mm" format
     * @return the parsed LocalDateTime object
     * @throws ReiExceptions if the date string is not in the expected format or is invalid
     */
    private static LocalDateTime parseDate(String dateString) throws ReiExceptions {
        validateDateInput(dateString);
        
        String cleanedDateString = dateString.trim();
        
        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(cleanedDateString, INPUT_FORMAT);
            return parsedDateTime;
        } catch (Exception e) {
            throw new ReiExceptions("Please use yyyy-MM-dd HH:mm format!");
        }
    }
    
    /**
     * Validates date string input before parsing.
     * 
     * @param dateString the date string to validate
     * @throws ReiExceptions if validation fails
     */
    private static void validateDateInput(String dateString) throws ReiExceptions {
        if (dateString == null) {
            throw new ReiExceptions("Date string cannot be null");
        }
        
        if (dateString.trim().isEmpty()) {
            throw new ReiExceptions("Date string cannot be empty");
        }
    }
}
