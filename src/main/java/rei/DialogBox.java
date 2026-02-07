package rei;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an avatar and a label containing text from the speaker.
 * Supports loading PNG images from resources/images/ folder.
 */
public class DialogBox extends HBox {
    private Label dialog;
    private StackPane avatarContainer;

    private DialogBox(String text, boolean isUser) {
        // Create the text label
        dialog = new Label(text);
        dialog.setWrapText(true);
        dialog.setMaxWidth(300); // Limit width for better text wrapping
        dialog.setPadding(new Insets(10));
        dialog.setPrefWidth(Region.USE_COMPUTED_SIZE);
        
        // Create avatar (try image first, fallback to generated)
        avatarContainer = createAvatar(isUser);
        
        // Make the HBox fill the full width
        this.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPadding(new Insets(5));
        this.setSpacing(10);
        
        if (isUser) {
            dialog.setStyle("-fx-background-color: #E6F3FF; -fx-background-radius: 10; -fx-text-fill: black;");
            
            // Create spacer to push user messages to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            // Add spacer first, then dialog and avatar
            this.getChildren().addAll(spacer, dialog, avatarContainer);
            this.setAlignment(Pos.CENTER_RIGHT);
        } else {
            dialog.setStyle("-fx-background-color: #F0FFF0; -fx-background-radius: 10; -fx-text-fill: black;");
            this.getChildren().addAll(avatarContainer, dialog);
            this.setAlignment(Pos.CENTER_LEFT);
        }
    }

    /**
     * Creates avatar - tries to load PNG image from /images/ folder first, 
     * falls back to generated avatar if not found.
     */
    private StackPane createAvatar(boolean isUser) {
        StackPane avatar = new StackPane();
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);
        
        // Try to load image from resources/images/ folder
        String imagePath = isUser ? "/images/user-avatar.png" : "/images/bot-avatar.png";
        
        try {
            Image avatarImage = new Image(getClass().getResourceAsStream(imagePath));
            if (!avatarImage.isError() && avatarImage.getWidth() > 0) {
                ImageView imageView = new ImageView(avatarImage);
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(false); // Fill the entire rectangle
                imageView.setSmooth(true);
                
                // Add rounded corners for rectangle style
                imageView.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                
                avatar.getChildren().add(imageView);
                avatar.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                return avatar;
            }
        } catch (Exception e) {
            // Image not found, fall back to generated avatar
        }
        
        // Fallback: Create generated avatar
        return createGeneratedAvatar(isUser);
    }

    /**
     * Creates a generated avatar as fallback when images are not available.
     */
    private StackPane createGeneratedAvatar(boolean isUser) {
        StackPane avatar = new StackPane();
        avatar.setMinSize(40, 40);
        avatar.setMaxSize(40, 40);
        
        if (isUser) {
            // User avatar - Blue gradient background (rectangular)
            RadialGradient userGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4A90E2")),
                new Stop(1, Color.web("#1E5A96"))
            );
            avatar.setStyle("-fx-background-color: linear-gradient(to bottom, #4A90E2, #1E5A96); " +
                           "-fx-background-radius: 5; -fx-border-radius: 5;");
            
            // Create person silhouette
            Circle head = new Circle(6);
            head.setFill(Color.WHITE);
            head.setTranslateY(-8);
            
            // Body shape using multiple circles to simulate torso
            Circle bodyUpper = new Circle(4);
            bodyUpper.setFill(Color.WHITE);
            bodyUpper.setTranslateY(-1);
            
            Circle bodyLower = new Circle(6);
            bodyLower.setFill(Color.WHITE);
            bodyLower.setTranslateY(6);
            
            avatar.getChildren().addAll(head, bodyUpper, bodyLower);
        } else {
            // Bot avatar - Green gradient background (rectangular)
            avatar.setStyle("-fx-background-color: linear-gradient(to bottom, #32CD32, #228B22); " +
                           "-fx-background-radius: 5; -fx-border-radius: 5;");
            
            // Create robot design
            // Head (square-ish)
            Circle robotHead = new Circle(7);
            robotHead.setFill(Color.WHITE);
            robotHead.setTranslateY(-8);
            
            // Eyes
            Circle leftEye = new Circle(1.5);
            leftEye.setFill(Color.web("#228B22"));
            leftEye.setTranslateX(-3);
            leftEye.setTranslateY(-8);
            
            Circle rightEye = new Circle(1.5);  
            rightEye.setFill(Color.web("#228B22"));
            rightEye.setTranslateX(3);
            rightEye.setTranslateY(-8);
            
            // Antenna
            Circle antenna = new Circle(1);
            antenna.setFill(Color.WHITE);
            antenna.setTranslateY(-15);
            
            // Body
            Circle robotBody = new Circle(8);
            robotBody.setFill(Color.WHITE);
            robotBody.setTranslateY(4);
            
            // Body details (buttons)
            Circle button1 = new Circle(1);
            button1.setFill(Color.web("#228B22"));
            button1.setTranslateX(-3);  
            button1.setTranslateY(2);
            
            Circle button2 = new Circle(1);
            button2.setFill(Color.web("#228B22"));
            button2.setTranslateX(3);
            button2.setTranslateY(2);
            
            Circle centerPanel = new Circle(2);
            centerPanel.setFill(Color.web("#228B22"));
            centerPanel.setTranslateY(6);
            
            avatar.getChildren().addAll(robotHead, leftEye, rightEye, antenna, robotBody, button1, button2, centerPanel);
        }
        
        return avatar;
    }

    /**
     * Creates a dialog box for user or bot messages.
     */
    public static DialogBox getUserDialog(String text, boolean isUser) {
        return new DialogBox(text, isUser);
    }
}