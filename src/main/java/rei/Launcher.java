package rei;

/**
 * A launcher class to workaround classpath issues when launching a JavaFX application.
 * This is a common practice to avoid issues with JavaFX module system.
 */
public class Launcher {
    public static void main(String[] args) {
        ReiGui.main(args);
    }
}