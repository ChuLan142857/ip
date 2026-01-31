public class ListCommand extends Command{

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws ReiExceptions{

        ui.showLine();
        ui.show("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            ui.show((i + 1) + ". " + tasks.get(i));
        }
        ui.showLine();
    }
}
