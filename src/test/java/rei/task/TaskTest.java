package rei.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void markDone_marksTaskAsDone() {
        Task task = new Todo("read book");

        task.markDone();

        assertTrue(task.isDone(), "Task should be marked as done");
    }

    @Test
    public void markUndone_marksTaskAsNotDone() {
        Task task = new Todo("read book");
        task.markDone();

        task.markUndone();

        assertFalse(task.isDone(), "Task should be marked as not done");
    }

}
