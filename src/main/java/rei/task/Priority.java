package rei.task;

/**
 * Represents the priority level of a task.
 * Provides both numeric levels and display names for task prioritization.
 */
public enum Priority {
    HIGH(1, "HIGH"),
    MEDIUM(2, "MEDIUM"),
    LOW(3, "LOW"),
    NONE(0, "");

    private final int level;
    private final String displayName;

    /**
     * Constructs a Priority with the specified level and display name.
     *
     * @param level numeric priority level (lower is higher priority)
     * @param displayName the display name for the priority
     */
    Priority(int level, String displayName) {
        this.level = level;
        this.displayName = displayName;
    }

    /**
     * Gets the numeric level of this priority.
     * Lower numbers indicate higher priority (1 = highest).
     *
     * @return the numeric priority level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the display name of this priority.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a numeric level to a Priority enum.
     *
     * @param level the numeric level (0, 1, 2, or 3)
     * @return the corresponding Priority
     * @throws IllegalArgumentException if level is invalid
     */
    public static Priority fromLevel(int level) {
        for (Priority p : Priority.values()) {
            if (p.level == level) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid priority level: " + level);
    }

    /**
     * Converts a string to a Priority enum (case-insensitive).
     *
     * @param str the string representation ("HIGH", "MEDIUM", "LOW", or "NONE")
     * @return the corresponding Priority
     * @throws IllegalArgumentException if string is invalid
     */
    public static Priority fromString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return NONE;
        }
        
        String upper = str.trim().toUpperCase();
        for (Priority p : Priority.values()) {
            if (p.name().equals(upper)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid priority: " + str);
    }

    /**
     * Returns a string representation suitable for file storage.
     *
     * @return the numeric level as a string
     */
    public String toFileString() {
        return String.valueOf(level);
    }

    /**
     * Returns a formatted string for display with brackets.
     *
     * @return formatted priority (e.g., "[HIGH]" or empty string for NONE)
     */
    public String toDisplayString() {
        if (this == NONE) {
            return "";
        }
        return "[" + displayName + "]";
    }
}
