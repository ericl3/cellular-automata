import javafx.scene.control.Slider;
import javafx.animation.Timeline;
import javafx.scene.text.Text;

/**
 * SpeedSlider class: creates the
 * speed slider that adjusts speed of animation
 *
 */
public class SpeedSlider {

    public static final double SPEED_MULTIPLIER_INCREMENT = 0.25;
    public static final double TEXT_OFFSET_X = 40;
    public static final double TEXT_OFFSET_Y = 10;

    private Slider slider;
    private Text sliderText;

    /**
     * Constructs the speed slider object and places on screen
     *
     * @param base              Lowest speed value
     * @param max               Maximum speed value
     * @param defaultValue      Default speed value
     * @param xPos              X position of speed slider
     * @param yPos              Y position of speed slider
     */
    public SpeedSlider(int base, int max, int defaultValue, double xPos, double yPos) {
        slider = new Slider(base, max, defaultValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(SPEED_MULTIPLIER_INCREMENT);
        slider.setMajorTickUnit(base);
        slider.snapToTicksProperty();
        slider.setLayoutX(xPos);
        slider.setLayoutY(yPos);
        sliderText = new Text("Set Speed");
        sliderText.setX(xPos + TEXT_OFFSET_X);
        sliderText.setY(yPos - TEXT_OFFSET_Y);

    }

    /**
     * Return slider object of speed slider
     *
     * @return  Slider of speed slider
     */
    public Slider getSlider() {
        return this.slider;
    }

    /**
     * Return text of speed slider
     *
     * @return  speed slider text
     */
    public Text getSliderText() {
        return this.sliderText;
    }

    /**
     * Change speed on step for animation
     *
     * @param animation     animation object of simulation
     */
    public void changeSpeed(Timeline animation) {
        animation.setRate(slider.getValue());
    }

}
