package rei.ui;

/**
 * Interface for user interface operations in the Rei application.
 * Defines the standard methods for input/output operations.
 */
public interface UiInterface {
    
    /**
     * Displays the welcome message when the application starts.
     */
    void showWelcome();

    /**
     * Reads a command from the user input.
     *
     * @return the command string entered by the user
     */
    String readCommand();

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    void show(String message);

    /**
     * Displays a horizontal line separator for visual organization.
     */
    void showLine();

    /**
     * Displays an error message.
     *
     * @param message the error message to display
     */
    void showError(String message);
}