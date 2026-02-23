package rei.task;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Abstract base class representing a task in the Rei application.
 * All specific task types (Todo, Deadline, Event) extend this class.
 */
public abstract class Task {
    private static long nextInsertionOrder = 0;
    
    private String description;
    private boolean isDone;
    private final long insertionOrder;
    private Set<String> tags;
    private Priority priority;

    /**
     * Constructs a new task with the given description.
     * The task is initially marked as not done.
     * Assigns an insertion order for tracking original add sequence.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        this.description = description;
        this.isDone = false;
        this.insertionOrder = nextInsertionOrder++;
        this.tags = new HashSet<>();
        this.priority = Priority.NONE;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markUndone() {
        isDone = false;
    }

    /**
     * Checks if this task is completed.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets the done flag as a string for file storage.
     *
     * @return "1" if the task is done, "0" otherwise
     */
    public String getDoneFlag() {
        return isDone ? "1" : "0";
    }

    /**
     * Gets the visual status icon for displaying the task completion status.
     *
     * @return "[X]" if the task is done, "[ ]" otherwise
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Gets the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        assert description != null : "Task description should never be null";
        return description;
    }

    /**
     * Sets the description of this task.
     *
     * @param description the new task description
     */
    public void setDescription(String description) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        this.description = description;
    }

    /**
     * Gets the insertion order of this task.
     * Used for sorting tasks back to their original order.
     *
     * @return the insertion order number
     */
    public long getInsertionOrder() {
        return insertionOrder;
    }

    /**
     * Gets the priority of this task.
     *
     * @return the priority level
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of this task.
     *
     * @param priority the new priority level
     */
    public void setPriority(Priority priority) {
        assert priority != null : "Priority cannot be null";
        this.priority = priority;
    }

    /**
     * Adds a tag to this task.
     *
     * @param tag the tag to add (without # prefix)
     */
    public void addTag(String tag) {
        assert tag != null : "Tag cannot be null";
        assert !tag.trim().isEmpty() : "Tag cannot be empty";
        // Normalize tag: remove # if present, convert to lowercase
        String normalizedTag = tag.trim().toLowerCase();
        if (normalizedTag.startsWith("#")) {
            normalizedTag = normalizedTag.substring(1);
        }
        tags.add(normalizedTag);
    }

    /**
     * Removes a tag from this task.
     *
     * @param tag the tag to remove (without # prefix)
     * @return true if the tag was removed, false if it wasn't present
     */
    public boolean removeTag(String tag) {
        assert tag != null : "Tag cannot be null";
        // Normalize tag: remove # if present, convert to lowercase
        String normalizedTag = tag.trim().toLowerCase();
        if (normalizedTag.startsWith("#")) {
            normalizedTag = normalizedTag.substring(1);
        }
        return tags.remove(normalizedTag);
    }

    /**
     * Gets all tags for this task.
     *
     * @return a set of tags
     */
    public Set<String> getTags() {
        return new HashSet<>(tags); // Return a copy to prevent external modification
    }

    /**
     * Checks if this task has a specific tag.
     *
     * @param tag the tag to check for
     * @return true if the task has this tag
     */
    public boolean hasTag(String tag) {
        String normalizedTag = tag.trim().toLowerCase();
        if (normalizedTag.startsWith("#")) {
            normalizedTag = normalizedTag.substring(1);
        }
        return tags.contains(normalizedTag);
    }

    /**
     * Formats tags for display.
     *
     * @return a string of tags formatted as #tag1 #tag2, or empty string if no tags
     */
    protected String formatTags() {
        if (tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .sorted()
                .map(tag -> "#" + tag)
                .collect(Collectors.joining(" ", " ", ""));
    }

    /**
     * Formats tags for file storage.
     *
     * @return a string of tags separated by commas, or empty string if no tags
     */
    protected String formatTagsForFile() {
        if (tags.isEmpty()) {
            return "";
        }
        return tags.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

    /**
     * Returns a string representation of this task for display purposes.
     *
     * @return a formatted string containing the status icon and description
     */
    @Override
    public String toString() {
        String priorityStr = priority.toDisplayString();
        String prefix = priorityStr.isEmpty() ? "" : priorityStr + " ";
        return getStatusIcon() + " " + prefix + description + formatTags();
    }

    /**
     * Returns a string representation of this task for file storage.
     *
     * @return a formatted string suitable for saving to file
     */
    public String toFileString() {
        return getDoneFlag() + " | " + priority.toFileString() + " | " + description;
    }
    
    /**
     * Returns the tags formatted for file storage, with pipe separator prefix if tags exist.
     * 
     * @return empty string if no tags, otherwise " | tags"
     */
    protected String getTagsForFile() {
        String tagsString = formatTagsForFile();
        if (tagsString.isEmpty()) {
            return "";
        }
        return " | " + tagsString;
    }

    /**
     * Checks if this task is equal to another object.
     * Two tasks are considered equal if they have the same description and type.
     * The done status is not considered in equality comparison.
     *
     * @param obj the object to compare with
     * @return true if the tasks are equal, false otherwise
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns a hash code value for this task.
     *
     * @return a hash code value based on the task's description
     */
    @Override
    public abstract int hashCode();
}
