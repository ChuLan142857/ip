package rei.list;

import java.util.ArrayList;
import rei.task.*;
import rei.exceptions.ReiExceptions;

/**
 * Manages a list of tasks with operations for adding, removing, and modifying tasks.
 * Provides a wrapper around ArrayList with additional task-specific functionality.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs a new TaskList with the provided list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks list cannot be null";
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the task list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task to list";
        assert tasks != null : "Task list should be initialized";
        tasks.add(task);
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index the 0-based index of the task to retrieve
     * @return the task at the specified index
     * @throws ReiExceptions if the index is out of bounds
     */
    public Task get(int index) throws ReiExceptions {
        checkIndex(index);
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the 0-based index of the task to remove
     * @return the removed task
     * @throws ReiExceptions if the index is out of bounds
     */
    public Task remove(int index) throws ReiExceptions {
        checkIndex(index);
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as completed.
     *
     * @param index the 0-based index of the task to mark as done
     * @throws ReiExceptions if the index is out of bounds
     */
    public void markDone(int index) throws ReiExceptions {
        checkIndex(index);
        tasks.get(index).markDone();
    }

    /**
     * Marks the task at the specified index as not completed.
     *
     * @param index the 0-based index of the task to mark as undone
     * @throws ReiExceptions if the index is out of bounds
     */
    public void markUndone(int index) throws ReiExceptions {
        checkIndex(index);
        tasks.get(index).markUndone();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        assert tasks != null : "Task list should be initialized";
        return tasks.size();
    }

    /**
     * Returns the last task added to the list.
     *
     * @return the most recently added task
     */
    public Task getLast() {
        assert tasks != null : "Task list should be initialized";
        assert !tasks.isEmpty() : "Cannot get last task from empty list";
        return tasks.get(tasks.size() - 1);
    }

    /**
     * Returns the underlying ArrayList containing all tasks.
     *
     * @return the ArrayList of all tasks
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }

    /**
     * Validates that the given index is within the valid range of the task list.
     *
     * @param index the index to check
     * @throws ReiExceptions if the index is negative or exceeds the list size
     */
    private void checkIndex(int index) throws ReiExceptions {
        assert tasks != null : "Task list should be initialized";
        assert index >= -1 : "Index should not be extremely negative"; // Additional safety check
        if (index < 0 || index >= tasks.size()) {
            throw new ReiExceptions("OOPS!!! That task number is invalid.");
        }
    }
}
