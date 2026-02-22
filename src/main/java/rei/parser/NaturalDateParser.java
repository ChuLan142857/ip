package rei.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing natural language date expressions into LocalDateTime objects.
 * Supports day names (Mon, Tuesday, etc.), relative dates (today, tomorrow, yesterday),
 * and time specifications.
 */
public class NaturalDateParser {
    
    // Default time for dates without specific time
    private static final LocalTime DEFAULT_TIME = LocalTime.of(23, 59);
    
    // Mapping of day name abbreviations to DayOfWeek
    private static final Map<String, DayOfWeek> DAY_MAPPINGS = new HashMap<>();
    
    static {
        // Full day names
        DAY_MAPPINGS.put("monday", DayOfWeek.MONDAY);
        DAY_MAPPINGS.put("tuesday", DayOfWeek.TUESDAY);
        DAY_MAPPINGS.put("wednesday", DayOfWeek.WEDNESDAY);
        DAY_MAPPINGS.put("thursday", DayOfWeek.THURSDAY);
        DAY_MAPPINGS.put("friday", DayOfWeek.FRIDAY);
        DAY_MAPPINGS.put("saturday", DayOfWeek.SATURDAY);
        DAY_MAPPINGS.put("sunday", DayOfWeek.SUNDAY);
        
        // Three-letter abbreviations
        DAY_MAPPINGS.put("mon", DayOfWeek.MONDAY);
        DAY_MAPPINGS.put("tue", DayOfWeek.TUESDAY);
        DAY_MAPPINGS.put("wed", DayOfWeek.WEDNESDAY);
        DAY_MAPPINGS.put("thu", DayOfWeek.THURSDAY);
        DAY_MAPPINGS.put("fri", DayOfWeek.FRIDAY);
        DAY_MAPPINGS.put("sat", DayOfWeek.SATURDAY);
        DAY_MAPPINGS.put("sun", DayOfWeek.SUNDAY);
    }
    
    /**
     * Checks if a string appears to be a natural date expression.
     * 
     * @param input the input string to check
     * @return true if it looks like a natural date expression
     */
    public static boolean isNaturalDate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        String normalized = input.trim().toLowerCase();
        
        // Check for relative dates
        if (normalized.equals("today") || normalized.equals("tomorrow") || normalized.equals("yesterday")) {
            return true;
        }
        
        // Check for day names (with optional "next" prefix)
        String dayPart = normalized.startsWith("next ") ? normalized.substring(5).trim() : normalized;
        if (DAY_MAPPINGS.containsKey(dayPart)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Parses a natural date expression into a LocalDateTime.
     * Supports formats like "Mon", "next Mon", "today", "tomorrow", etc.
     * Time can be optionally specified after the date like "Mon 14:00".
     * 
     * @param input the natural date expression
     * @return the parsed LocalDateTime
     * @throws IllegalArgumentException if the format is not recognized
     */
    public static LocalDateTime parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Date input cannot be null or empty");
        }
        
        String normalized = input.trim();
        
        // Split on whitespace to separate date and optional time
        String[] parts = normalized.split("\\s+", 2);
        String datePart = parts[0].toLowerCase();
        String timePart = parts.length > 1 ? parts[1] : null;
        
        // Handle "next" prefix
        boolean explicitNext = false;
        if (datePart.equals("next") && parts.length > 1) {
            // "next Mon" format
            String[] newParts = normalized.split("\\s+", 3);
            datePart = newParts[1].toLowerCase();
            timePart = newParts.length > 2 ? newParts[2] : null;
            explicitNext = true;
        }
        
        LocalDate date = parseNaturalDate(datePart, explicitNext);
        LocalTime time = parseTime(timePart);
        
        return LocalDateTime.of(date, time);
    }
    
    /**
     * Parses the date portion of a natural date expression.
     * 
     * @param datePart the date part (e.g., "mon", "today", "tomorrow")
     * @param explicitNext whether "next" was explicitly specified
     * @return the LocalDate
     */
    private static LocalDate parseNaturalDate(String datePart, boolean explicitNext) {
        LocalDate today = LocalDate.now();
        
        // Handle relative dates
        if (datePart.equals("today")) {
            return today;
        } else if (datePart.equals("tomorrow")) {
            return today.plusDays(1);
        } else if (datePart.equals("yesterday")) {
            return today.minusDays(1);
        }
        
        // Handle day names
        DayOfWeek targetDay = DAY_MAPPINGS.get(datePart);
        if (targetDay != null) {
            return getNextOccurrence(today, targetDay, explicitNext);
        }
        
        throw new IllegalArgumentException("Unrecognized date format: " + datePart);
    }
    
    /**
     * Gets the next occurrence of a specific day of the week.
     * If explicitNext is false and today is that day, returns today.
     * Otherwise returns the next occurrence.
     * 
     * @param from the starting date
     * @param targetDay the target day of week
     * @param explicitNext whether to skip today even if it matches
     * @return the next occurrence of that day
     */
    private static LocalDate getNextOccurrence(LocalDate from, DayOfWeek targetDay, boolean explicitNext) {
        if (!explicitNext && from.getDayOfWeek() == targetDay) {
            // If it's already that day and "next" wasn't specified, use today
            return from;
        }
        
        // Find the next occurrence of that day
        return from.with(TemporalAdjusters.next(targetDay));
    }
    
    /**
     * Parses the time portion of a natural date expression.
     * Supports formats like "14:00", "2:30 PM", "9:00".
     * If no time is specified, returns default time (23:59).
     * 
     * @param timePart the time string, or null
     * @return the parsed LocalTime
     */
    private static LocalTime parseTime(String timePart) {
        if (timePart == null || timePart.trim().isEmpty()) {
            return DEFAULT_TIME;
        }
        
        String normalized = timePart.trim();
        
        // Try to parse as HH:mm format
        try {
            if (normalized.matches("\\d{1,2}:\\d{2}")) {
                String[] parts = normalized.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                return LocalTime.of(hour, minute);
            }
        } catch (Exception e) {
            // Fall through to default
        }
        
        // If we can't parse the time, use default
        return DEFAULT_TIME;
    }
}
