import java.util.Scanner;
import java.util.ArrayList;

public class Rei {

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Rei() throws ReiExceptions {
        ui = new Ui();
        storage = new Storage("./data/Rei.txt");
        tasks = new TaskList(storage.load());
    }

    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parse(input);
                command.execute(tasks, ui, storage);

                if (command.isExit()) {
                    break;
                }
            } catch (ReiExceptions e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws ReiExceptions {
        new Rei().run();
    }
}
