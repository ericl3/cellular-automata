import javafx.stage.FileChooser;
import javafx.scene.control.Button;

import java.io.File;

/**
 * FileChooserButton class:
 * Creates button for choosing different simulations
 *
 * @author Eric Lin
 * @author kunalupadya
 */
public class FileChooserButton extends Animation {
    private FileChooser myChooser;
    private Button fileChooserButton;

    /**
     * Constructs the button for selecting file choosing
     *
     * @param xPos  X position of button
     * @param yPos  Y position of button
     */
    public FileChooserButton(double xPos, double yPos) {
        myChooser = makeChooser(DATA_FILE_EXTENSION);
        fileChooserButton = new Button("Choose Simulation");
        fileChooserButton.setLayoutX(xPos);
        fileChooserButton.setLayoutY(yPos);
    }

    private FileChooser makeChooser (String extensionAccepted) {
        var result = new FileChooser();
        result.setTitle("Open Data File");
        result.setInitialDirectory(new File("data"));
        result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Text Files", extensionAccepted));
        return result;
    }

    /**
     * Obtain button object of file chooser
     *
     * @return  Button of file chooser
     */
    public Button getFileChooserButton() {
        return this.fileChooserButton;
    }

    /**
     * Return the file chooser object
     *
     * @return return the file chooser object
     */
    public FileChooser getMyChooser() {
        return this.myChooser;
    }

}
