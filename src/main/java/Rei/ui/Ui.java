package Rei.ui;

import java.util.Scanner;

public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        showLine();
        show("Hello! I'm Rei.");
        show("What can I do for you?");
        showLine();
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void show(String message) {
        System.out.println(message);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        showLine();
        show(message);
        showLine();
    }
}

