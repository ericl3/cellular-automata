import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

/**
 * Initialize button for playing the simulation
 *
 * @author Eric Lin
 */
public class PlayButton {

    public static final double BUTTON_SCALE = 0.2;

    private Button playButton;
    private Image playImage = new Image(getClass().getResourceAsStream("play.jpeg"));

    /**
     * Construct the play button
     *
     * @param xPos  X position of play button
     * @param yPos  Y position of play button
     */
    public PlayButton(double xPos, double yPos) {
        playButton = new Button();
        playButton.setGraphic(new ImageView(playImage));
        playButton.setLayoutX(xPos);
        playButton.setLayoutY(yPos);
        playButton.setScaleX(BUTTON_SCALE);
        playButton.setScaleY(BUTTON_SCALE);
    }

    /**
     * Obtain the button object
     *
     * @return      button object of play button
     */
    public Button getPlayButton() {
        return this.playButton;
    }

}
