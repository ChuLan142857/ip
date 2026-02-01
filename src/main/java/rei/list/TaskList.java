package rei.list;

import java.util.ArrayList;
import rei.task.*;
import rei.exceptions.ReiExceptions;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) throws ReiExceptions {
        checkIndex(index);
        return tasks.get(index);
    }

    public Task remove(int index) throws ReiExceptions {
        checkIndex(index);
        return tasks.remove(index);
    }

    public void markDone(int index) throws ReiExceptions {
        checkIndex(index);
        tasks.get(index).markDone();
    }

    public void markUndone(int index) throws ReiExceptions {
        checkIndex(index);
        tasks.get(index).markUndone();
    }

    public int size() {
        return tasks.size();
    }

    public Task getLast() {
        return tasks.get(tasks.size() - 1);
    }

    public ArrayList<Task> getAll() {
        return tasks;
    }

    private void checkIndex(int index) throws ReiExceptions {
        if (index < 0 || index >= tasks.size()) {
            throw new ReiExceptions("OOPS!!! That task number is invalid.");
        }
    }
}
