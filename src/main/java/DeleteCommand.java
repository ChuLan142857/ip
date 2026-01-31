public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions {

        Task removed = tasks.remove(index);
        storage.save(tasks);

        ui.showLine();
        ui.show("Noted. I've removed this task:");
        ui.show(removed.toString());
        ui.show("Now you have " + tasks.size() + " task(s) in the list.");
        ui.showLine();
    }
}