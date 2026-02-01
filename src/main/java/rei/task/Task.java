package rei.task;

public abstract class Task {
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

    public String getDoneFlag() {
        return isDone ? "1" : "0";
    }

    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    @Override
    public String toString(){
        return getStatusIcon() + " " + description;
    }

    public String toFileString(){
        return getDoneFlag() + " | " + description;
    }
}
