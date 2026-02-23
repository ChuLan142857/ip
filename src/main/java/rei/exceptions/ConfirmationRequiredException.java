package rei.exceptions;

import rei.command.Command;

/**
 * Exception thrown when a command requires user confirmation before proceeding.
 * Contains the pending command that needs confirmation.
 */
public class ConfirmationRequiredException extends ReiExceptions {
    private final Command pendingCommand;

    /**
     * Constructs a new ConfirmationRequiredException with a message and pending command.
     *
     * @param message the confirmation message to display to the user
     * @param pendingCommand the command that needs confirmation
     */
    public ConfirmationRequiredException(String message, Command pendingCommand) {
        super(message);
        this.pendingCommand = pendingCommand;
    }

    /**
     * Gets the command that is pending confirmation.
     *
     * @return the pending command
     */
    public Command getPendingCommand() {
        return pendingCommand;
    }
}
