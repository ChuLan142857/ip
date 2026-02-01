package rei.exceptions;

/**
 * Custom exception class for the Rei application.
 * Used to handle application-specific error conditions with user-friendly messages.
 */
public class ReiExceptions extends Exception {
    /**
     * Constructs a new ReiExceptions with the specified error message.
     *
     * @param message the detail message explaining the error condition
     */
    public ReiExceptions(String message){
        super(message);
    }
}
