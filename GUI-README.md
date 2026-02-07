# Rei Chatbot GUI

This project now includes a JavaFX GUI implementation for the Rei chatbot.

## What's Been Added

### 1. JavaFX Dependencies
- Added JavaFX controls and FXML dependencies to `build.gradle.kts`
- Added JavaFX application plugin for proper module handling

### 2. Launcher Class (`src/main/java/rei/Launcher.java`)
- Created a separate launcher class to avoid JavaFX module issues
- This is the main entry point that launches the GUI application

### 3. GUI Application (`src/main/java/rei/ReiGui.java`)
- Main JavaFX Application class with chat interface
- Features a scrollable chat area, text input field, and send button
- Integrates with existing command parsing and execution system
- Automatically scrolls to show latest messages

### 4. Dialog Box (`src/main/java/rei/DialogBox.java`)
- Custom GUI component for displaying chat messages
- Visually distinguishes user messages (blue) from bot messages (green)
- Styled with rounded corners and appropriate spacing

### 5. GUI UI Interface (`src/main/java/rei/ui/GuiUi.java`)
- Implements the same interface as console UI but collects responses
- Allows commands to work seamlessly with GUI without modification

### 6. Interface Abstraction (`src/main/java/rei/ui/UiInterface.java`)
- Created common interface for both console and GUI UI implementations
- Updated all command classes to use this interface
- Enables polymorphic usage of different UI types

### 7. Visual Styling (`src/main/resources/styles.css`)
- Clean, modern styling for the chat interface
- Blue theme for user messages, green for bot responses
- Proper button and text field styling

## How to Run

### Option 1: Using Gradle (Recommended)
```bash
./gradlew run --no-configuration-cache
```

### Option 2: Build and Run JAR
```bash
./gradlew jar
java -jar build/libs/Rei.jar
```

## GUI Features

1. **Chat Interface**: Type commands and see responses in a conversational format
2. **Command Support**: All existing commands work (todo, deadline, event, list, find, delete, mark, unmark, bye)
3. **Visual Feedback**: Clear distinction between user input and bot responses
4. **Scrollable History**: View all previous interactions
5. **Keyboard Support**: Press Enter to send commands
6. **Responsive Design**: Window can be resized, minimum size constraints applied

## Example Usage

1. Launch the GUI application
2. Type commands in the input field at the bottom
3. Press Enter or click "Send" 
4. View responses in the chat area above

### Sample Commands:
- `todo Buy groceries` - Add a todo task
- `deadline Submit report /by 2024-12-31` - Add a deadline
- `event Team meeting /from 2024-12-25 /to 2024-12-25` - Add an event  
- `list` - View all tasks
- `mark 1` - Mark first task as done
- `delete 2` - Delete second task
- `find groceries` - Find tasks containing "groceries"
- `bye` - Exit the application

The GUI maintains all the functionality of the original console application while providing a modern, user-friendly interface.