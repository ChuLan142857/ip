package rei.list;

import rei.task.Task;
import rei.task.Todo;
import rei.exceptions.ReiExceptions;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void delete_validIndex_taskRemoved() throws ReiExceptions {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task task = new Todo("read book");
        taskList.add(task);

        Task removed = taskList.remove(0);

        assertEquals(task, removed);
        assertEquals(0, taskList.size());
    }

    @Test
    public void delete_invalidIndex_throwsException() {
        TaskList taskList = new TaskList(new ArrayList<>());

        assertThrows(ReiExceptions.class, () -> taskList.remove(0));
    }
}
