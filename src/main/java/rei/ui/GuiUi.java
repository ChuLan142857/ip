package rei.ui;

/**
 * A GUI-compatible UI class that collects output instead of printing to console.
 * Used by the JavaFX GUI to capture command responses.
 */
public class GuiUi implements UiInterface {
    private final StringBuilder responseBuilder;

    public GuiUi() {
        this.responseBuilder = new StringBuilder();
    }

    /**
     * Adds a message to the response buffer.
     *
     * @param message the message to add
     */
    public void show(String message) {
        if (responseBuilder.length() > 0) {
            responseBuilder.append("\n");
        }
        responseBuilder.append(message);
    }

    /**
     * Adds a line separator to the response buffer.
     */
    public void showLine() {
        // In GUI, we don't need visible line separators
        // Just add a newline for spacing if needed
        if (responseBuilder.length() > 0) {
            responseBuilder.append("\n");
        }
    }

    /**
     * Adds an error message to the response buffer.
     *
     * @param message the error message to add
     */
    public void showError(String message) {
        show("Error: " + message);
    }

    /**
     * Gets the collected response and clears the buffer.
     *
     * @return the collected response string
     */
    public String getResponse() {
        String response = responseBuilder.toString();
        responseBuilder.setLength(0); // Clear the buffer
        return response;
    }

    /**
     * This method is not used in GUI mode.
     *
     * @return empty string
     */
    public String readCommand() {
        return "";
    }

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        show("Hello! I'm Rei.");
        show("What can I do for you?");
    }
}