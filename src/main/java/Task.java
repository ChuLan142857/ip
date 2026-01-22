public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        isDone = true
        ;
    }

    public void markUndone() {
        isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    @Override
    public String toString()
    {
        return getStatusIcon() + " " + description;
    }
}
