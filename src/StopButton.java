import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

/**
 * StopButton class:
 * Creates button that allows user to pause the game
 *
 * @author Eric Lin
 */
public class StopButton {

    public static final double BUTTON_SCALE = 0.2;

    private Button stopButton;
    private Image stopImage = new Image(getClass().getResourceAsStream("pausebutton.png"));

    /**
     * Initializes the button on the screen
     *
     * @param xPos      X location of button
     * @param yPos      Y location of button
     */
    public StopButton(double xPos, double yPos) {
        stopButton = new Button();
        stopButton.setGraphic(new ImageView(stopImage));
        stopButton.setLayoutX(xPos);
        stopButton.setLayoutY(yPos);
        stopButton.setScaleX(BUTTON_SCALE);
        stopButton.setScaleY(BUTTON_SCALE);
    }

    /**
     * Return button object of stop button
     *
     * @return  a Button
     */
    public Button getStopButton() {
        return this.stopButton;
    }
}