package rei.ui;

import java.util.Scanner;

/**
 * Handles user interface operations for the Rei application.
 * Manages input reading and output display to the console.
 */
public class Ui implements UiInterface {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        showLine();
        show("Hello! I'm Rei.");
        show("What can I do for you?");
        showLine();
    }

    /**
     * Reads a command from the user input.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    public void show(String message) {
        assert message != null : "Message to display cannot be null";
        System.out.println(message);
    }

    /**
     * Displays a horizontal line separator for visual organization.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error message with line separators for emphasis.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        showLine();
        show(message);
        showLine();
    }

    /**
     * Reads a yes/no confirmation from the user.
     *
     * @param prompt the confirmation prompt to display
     * @return true if the user confirms (y/yes), false otherwise
     */
    public boolean readConfirmation(String prompt) {
        show(prompt + " (y/n)");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
}

