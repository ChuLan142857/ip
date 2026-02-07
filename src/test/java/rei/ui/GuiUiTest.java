package rei.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GuiUi to verify GUI interface functionality.
 */
public class GuiUiTest {

    @Test
    public void show_message_storesMessage() {
        GuiUi guiUi = new GuiUi();
        guiUi.show("Test message");
        
        String response = guiUi.getResponse();
        assertEquals("Test message", response);
    }

    @Test
    public void show_multipleMessages_concatenatesWithNewlines() {
        GuiUi guiUi = new GuiUi();
        guiUi.show("First message");
        guiUi.show("Second message");
        
        String response = guiUi.getResponse();
        assertEquals("First message\nSecond message", response);
    }

    @Test
    public void showError_message_formatsAsError() {
        GuiUi guiUi = new GuiUi();
        guiUi.showError("Something went wrong");
        
        String response = guiUi.getResponse();
        assertEquals("Error: Something went wrong", response);
    }

    @Test
    public void getResponse_clearsBuffer_afterRetrieving() {
        GuiUi guiUi = new GuiUi();
        guiUi.show("Test message");
        
        String firstResponse = guiUi.getResponse();
        String secondResponse = guiUi.getResponse();
        
        assertEquals("Test message", firstResponse);
        assertEquals("", secondResponse);
    }

    @Test
    public void readCommand_returnsEmptyString() {
        GuiUi guiUi = new GuiUi();
        String command = guiUi.readCommand();
        
        assertEquals("", command);
    }
}