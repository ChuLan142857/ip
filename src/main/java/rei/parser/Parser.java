package rei.parser;

import rei.exceptions.ReiExceptions;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import rei.command.*;
import rei.task.Priority;

/**
 * Parses user input strings and converts them into appropriate Command objects.
 * Handles the interpretation of various command formats and their parameters.
 */
public class Parser {

    private static final DateTimeFormatter INPUT_FORMAT_WITH_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter INPUT_FORMAT_DATE_ONLY =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);
    
    // Constants for string parsing offsets to avoid magic numbers
    private static final int FIND_COMMAND_PREFIX_LENGTH = 5;
    private static final int TODO_COMMAND_PREFIX_LENGTH = 5;
    private static final int EVENT_COMMAND_PREFIX_LENGTH = 6;
    private static final int MARK_COMMAND_PREFIX_LENGTH = 5;
    private static final int UNMARK_COMMAND_PREFIX_LENGTH = 7;
    private static final int DELETE_COMMAND_PREFIX_LENGTH = 7;
    private static final int UPDATE_COMMAND_PREFIX_LENGTH = 7;
    private static final int CLONE_COMMAND_PREFIX_LENGTH = 6;
    private static final int TAG_COMMAND_PREFIX_LENGTH = 4;
    private static final int UNTAG_COMMAND_PREFIX_LENGTH = 6;
    
    // Constants for validation requirements
    private static final int MINIMUM_EVENT_PARTS = 3;
    private static final String DEADLINE_SEPARATOR = "/by";
    private static final String EVENT_SEPARATOR_REGEX = " /from | /to ";
    
    // Command keywords
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String SOURCE_COMMAND = "source";
    private static final String SORT_COMMAND = "sort";
    private static final String SORT_COMMAND_PREFIX = "sort ";
    private static final String FIND_COMMAND_PREFIX = "find ";
    private static final String TODO_COMMAND_PREFIX = "todo";
    private static final String DEADLINE_COMMAND_PREFIX = "deadline";
    private static final String EVENT_COMMAND_PREFIX = "event";
    private static final String MARK_COMMAND_PREFIX = "mark ";
    private static final String UNMARK_COMMAND_PREFIX = "unmark ";
    private static final String DELETE_COMMAND_PREFIX = "delete ";
    private static final String UPDATE_COMMAND_PREFIX = "update ";
    private static final String CLONE_COMMAND_PREFIX = "clone ";
    private static final String SOURCE_COMMAND_PREFIX = "source ";
    private static final String TAG_COMMAND_PREFIX = "tag ";
    private static final String UNTAG_COMMAND_PREFIX = "untag ";
    private static final String PRIORITY_COMMAND_PREFIX = "priority ";

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
        
        if (input.equals(SOURCE_COMMAND)) {
            return new SourceCommand(); // Query current source
        }
        
        if (input.equals(SORT_COMMAND)) {
            return new SortCommand();
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
        
        if (input.startsWith(SOURCE_COMMAND_PREFIX)) {
            return parseSourceCommand(input);
        }
        
        if (input.startsWith(SORT_COMMAND_PREFIX)) {
            return parseSortCommand(input);
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
        
        if (input.startsWith(UPDATE_COMMAND_PREFIX)) {
            return parseUpdateCommand(input);
        }
        
        if (input.startsWith(CLONE_COMMAND_PREFIX)) {
            return parseCloneCommand(input);
        }
        
        if (input.startsWith(TAG_COMMAND_PREFIX)) {
            return parseTagCommand(input);
        }
        
        if (input.startsWith(UNTAG_COMMAND_PREFIX)) {
            return parseUntagCommand(input);
        }
        
        if (input.startsWith(PRIORITY_COMMAND_PREFIX)) {
            return parsePriorityCommand(input);
        }
        
        return null; // No parameterized command matches
    }
    
    /**
     * Parses find command from input string.
     * Supports both AND and OR search modes.
     * Format: "find [AND|OR] keyword..."
     * If no mode specified, defaults to OR.
     * 
     * @param input the input string starting with "find "
     * @return FindCommand object with appropriate search mode
     * @throws ReiExceptions if keyword is missing
     */
    private static Command parseFindCommand(String input) throws ReiExceptions {
        String keyword = extractSubstring(input, FIND_COMMAND_PREFIX_LENGTH);
        
        if (keyword.isEmpty()) {
            throw new ReiExceptions("Find command requires a keyword.");
        }
        
        // Check for AND/OR prefix
        String upperKeyword = keyword.trim().toUpperCase();
        boolean useAndLogic = false;
        String actualKeyword = keyword;
        
        if (upperKeyword.startsWith("AND ")) {
            useAndLogic = true;
            actualKeyword = keyword.substring(4).trim();
        } else if (upperKeyword.startsWith("OR ")) {
            useAndLogic = false;
            actualKeyword = keyword.substring(3).trim();
        }
        
        if (actualKeyword.isEmpty()) {
            throw new ReiExceptions("Find command requires at least one keyword after AND/OR.");
        }
        
        return new FindCommand(actualKeyword, useAndLogic);
    }
    
    /**
     * Parses source command from input string.
     * 
     * @param input the input string starting with "source "
     * @return SourceCommand object with the new file path
     * @throws ReiExceptions if file path is empty
     */
    private static Command parseSourceCommand(String input) throws ReiExceptions {
        String filePath = input.substring(SOURCE_COMMAND_PREFIX.length()).trim();
        
        if (filePath.isEmpty()) {
            throw new ReiExceptions("OOPS!!! The file path cannot be empty.");
        }
        
        return new SourceCommand(filePath);
    }
    
    /**
     * Parses sort command from input string.
     * 
     * @param input the input string starting with "sort "
     * @return SortCommand object with the specified sort type
     * @throws ReiExceptions if sort type is invalid
     */
    private static Command parseSortCommand(String input) throws ReiExceptions {
        String sortType = input.substring(SORT_COMMAND_PREFIX.length()).trim().toLowerCase();
        
        if (sortType.isEmpty()) {
            throw new ReiExceptions("OOPS!!! Please specify sort type: 'insertion' or 'date'.");
        }
        
        if (sortType.equals("insertion") || sortType.equals("original")) {
            return new SortCommand(true);
        } else if (sortType.equals("date") || sortType.equals("chronological")) {
            return new SortCommand(false);
        } else {
            throw new ReiExceptions("OOPS!!! Invalid sort type. Use 'insertion', 'original', 'date', or 'chronological'.");
        }
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
     * Parses update command from input string.
     * Format: "update <index> /desc <description>" or
     *         "update <index> /by <deadline>" or
     *         "update <index> /from <start> /to <end>"
     * 
     * @param input the input string starting with "update "
     * @return UpdateCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parseUpdateCommand(String input) throws ReiExceptions {
        String arguments = extractSubstring(input, UPDATE_COMMAND_PREFIX_LENGTH);
        
        if (arguments.isEmpty()) {
            throw new ReiExceptions("Update command requires a task number and update details.");
        }
        
        // Split by first space to separate index from the rest
        String[] parts = arguments.split("\\s+", 2);
        if (parts.length < 2) {
            throw new ReiExceptions("Update command requires update details after the task number.");
        }
        
        int taskIndex;
        try {
            int userProvidedIndex = Integer.parseInt(parts[0]);
            taskIndex = convertToArrayIndex(userProvidedIndex);
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
        
        String details = parts[1].trim();
        
        // Check for /desc (description update)
        if (details.startsWith("/desc ")) {
            String newDescription = details.substring(6).trim();
            if (newDescription.isEmpty()) {
                throw new ReiExceptions("Description cannot be empty.");
            }
            return new UpdateCommand(taskIndex, newDescription);
        }
        
        // Check for /by (deadline update)
        if (details.startsWith("/by ")) {
            String dateStr = details.substring(4).trim();
            if (dateStr.isEmpty()) {
                throw new ReiExceptions("Deadline date cannot be empty.");
            }
            LocalDateTime deadline = parseDate(dateStr);
            return new UpdateCommand(taskIndex, deadline);
        }
        
        // Check for /from and /to (event update)
        if (details.contains("/from") || details.contains("/to")) {
            String[] eventParts = details.split(" /from | /to ");
            LocalDateTime start = null;
            LocalDateTime end = null;
            
            if (details.contains("/from")) {
                int fromIndex = details.indexOf("/from");
                int toIndex = details.indexOf("/to");
                
                if (toIndex > fromIndex) {
                    // Both /from and /to present
                    String startStr = details.substring(fromIndex + 6, toIndex).trim();
                    String endStr = details.substring(toIndex + 4).trim();
                    start = parseDate(startStr);
                    end = parseDate(endStr);
                } else {
                    // Only /from present
                    String startStr = details.substring(fromIndex + 6).trim();
                    start = parseDate(startStr);
                }
            } else {
                // Only /to present
                int toIndex = details.indexOf("/to");
                String endStr = details.substring(toIndex + 4).trim();
                end = parseDate(endStr);
            }
            
            return new UpdateCommand(taskIndex, start, end);
        }
        
        throw new ReiExceptions("Invalid update format. Use /desc, /by, /from, or /to to specify what to update.");
    }
    
    /**
     * Parses clone command from input string.
     * 
     * @param input the input string starting with "clone "
     * @return CloneCommand object
     * @throws ReiExceptions if index is invalid
     */
    private static Command parseCloneCommand(String input) throws ReiExceptions {
        int taskIndex = parseIndex(input, CLONE_COMMAND_PREFIX_LENGTH);
        return new CloneCommand(taskIndex);
    }
    
    /**
     * Parses tag command from input string.
     * Format: "tag <index> <tag>"
     * 
     * @param input the input string starting with "tag "
     * @return TagCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parseTagCommand(String input) throws ReiExceptions {
        String arguments = extractSubstring(input, TAG_COMMAND_PREFIX_LENGTH);
        
        if (arguments.isEmpty()) {
            throw new ReiExceptions("OOPS!!! Tag command requires a task number and a tag.");
        }
        
        String[] parts = arguments.split(" ", 2);
        
        if (parts.length < 2) {
            throw new ReiExceptions("OOPS!!! Tag command requires both task number and tag.");
        }
        
        try {
            int userProvidedIndex = Integer.parseInt(parts[0].trim());
            int taskIndex = convertToArrayIndex(userProvidedIndex);
            String tag = parts[1].trim();
            
            if (tag.isEmpty()) {
                throw new ReiExceptions("OOPS!!! Tag cannot be empty.");
            }
            
            return new TagCommand(taskIndex, tag);
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }
    
    /**
     * Parses untag command from input string.
     * Format: "untag <index> <tag>"
     * 
     * @param input the input string starting with "untag "
     * @return UntagCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parseUntagCommand(String input) throws ReiExceptions {
        String arguments = extractSubstring(input, UNTAG_COMMAND_PREFIX_LENGTH);
        
        if (arguments.isEmpty()) {
            throw new ReiExceptions("OOPS!!! Untag command requires a task number and a tag.");
        }
        
        String[] parts = arguments.split(" ", 2);
        
        if (parts.length < 2) {
            throw new ReiExceptions("OOPS!!! Untag command requires both task number and tag.");
        }
        
        try {
            int userProvidedIndex = Integer.parseInt(parts[0].trim());
            int taskIndex = convertToArrayIndex(userProvidedIndex);
            String tag = parts[1].trim();
            
            if (tag.isEmpty()) {
                throw new ReiExceptions("OOPS!!! Tag cannot be empty.");
            }
            
            return new UntagCommand(taskIndex, tag);
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }
    
    /**
     * Parses priority command from input string.
     * Format: "priority <index> <level>" where level can be HIGH, MEDIUM, LOW, or a number 1-3.
     * 
     * @param input the input string starting with "priority "
     * @return PriorityCommand object
     * @throws ReiExceptions if format is invalid
     */
    private static Command parsePriorityCommand(String input) throws ReiExceptions {
        String arguments = extractSubstring(input, PRIORITY_COMMAND_PREFIX.length());
        
        if (arguments.isEmpty()) {
            throw new ReiExceptions("OOPS!!! Priority command requires a task number and priority level.");
        }
        
        String[] parts = arguments.split(" ", 2);
        
        if (parts.length < 2) {
            throw new ReiExceptions("OOPS!!! Priority command requires both task number and priority level.");
        }
        
        try {
            int userProvidedIndex = Integer.parseInt(parts[0].trim());
            int taskIndex = convertToArrayIndex(userProvidedIndex);
            String priorityStr = parts[1].trim();
            
            if (priorityStr.isEmpty()) {
                throw new ReiExceptions("OOPS!!! Priority level cannot be empty.");
            }
            
            Priority priority = parsePriorityLevel(priorityStr);
            
            return new PriorityCommand(taskIndex, priority);
        } catch (NumberFormatException e) {
            throw new ReiExceptions("OOPS!!! Task number must be a number.");
        }
    }
    
    /**
     * Parses a priority level from string input.
     * Accepts both names (HIGH, MEDIUM, LOW) and numbers (1, 2, 3).
     * 
     * @param priorityStr the priority string
     * @return the Priority enum value
     * @throws ReiExceptions if priority is invalid
     */
    private static Priority parsePriorityLevel(String priorityStr) throws ReiExceptions {
        try {
            // Try to parse as number first
            int level = Integer.parseInt(priorityStr);
            try {
                return Priority.fromLevel(level);
            } catch (IllegalArgumentException e) {
                throw new ReiExceptions("OOPS!!! Invalid priority level: " + priorityStr 
                        + ". Valid levels are 1 (HIGH), 2 (MEDIUM), 3 (LOW).");
            }
        } catch (NumberFormatException e) {
            // Try to parse as name (HIGH, MEDIUM, LOW)
            try {
                return Priority.fromString(priorityStr);
            } catch (IllegalArgumentException ex) {
                throw new ReiExceptions("OOPS!!! Invalid priority: " + priorityStr 
                        + ". Use HIGH/MEDIUM/LOW or 1/2/3.");
            }
        }
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
     * Parses a date string into a LocalDateTime object.
     * Supports natural language dates (e.g., "Mon", "today", "tomorrow"),
     * standard format with time ("yyyy-MM-dd HH:mm"),
     * and date-only format ("yyyy-MM-dd") which defaults to 23:59.
     * Provides clear error messages and proper validation.
     *
     * @param dateString the date string to parse
     * @return the parsed LocalDateTime object
     * @throws ReiExceptions if the date string is not in a recognized format or is invalid
     */
    private static LocalDateTime parseDate(String dateString) throws ReiExceptions {
        validateDateInput(dateString);
        
        String cleanedDateString = dateString.trim();
        
        // Try natural date parsing first
        if (NaturalDateParser.isNaturalDate(cleanedDateString)) {
            try {
                return NaturalDateParser.parse(cleanedDateString);
            } catch (Exception e) {
                throw new ReiExceptions("Invalid natural date format: " + e.getMessage());
            }
        }
        
        // Try standard format with time (yyyy-MM-dd HH:mm)
        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(cleanedDateString, INPUT_FORMAT_WITH_TIME);
            return parsedDateTime;
        } catch (Exception e) {
            // If that fails, try date-only format (yyyy-MM-dd)
            try {
                LocalDate date = LocalDate.parse(cleanedDateString, INPUT_FORMAT_DATE_ONLY);
                return LocalDateTime.of(date, DEFAULT_TIME);
            } catch (Exception e2) {
                throw new ReiExceptions("Please use yyyy-MM-dd or yyyy-MM-dd HH:mm format, or natural dates like 'Mon', 'today', 'tomorrow'!");
            }
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
