//package XML;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.control.Alert;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import java.io.File;
//
//public class XMLTesting extends Application{
//        // kind of data files to look for
//        public static final String DATA_FILE_EXTENSION = "*.xml";
//
//        // NOTE: generally accepted behavior that the chooser remembers where user left it last
//        private FileChooser myChooser = makeChooser(DATA_FILE_EXTENSION);
//
//
//        @Override
//        public void start (Stage primaryStage) throws Exception {
//            var dataFile = myChooser.showOpenDialog(primaryStage);
//            while (dataFile != null) {
//                try {
//                    var m = new XMLParse("media").getGame(dataFile);
//                    new Alert(Alert.AlertType.INFORMATION, m.toString()).showAndWait();
//                }
//                catch (XMLException e) {
//                    new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
//                }
//                dataFile = myChooser.showOpenDialog(primaryStage);
//            }
//            // nothing selected, so quit the application
//            Platform.exit();
//        }
//
//        // set some sensible defaults when the FileChooser is created
//        private FileChooser makeChooser (String extensionAccepted) {
//            var result = new FileChooser();
//            result.setTitle("Open Data File");
//            // pick a reasonable place to start searching for files
//            result.setInitialDirectory(new File(System.getProperty("user.dir")));
//            result.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Text Files", extensionAccepted));
//            return result;
//        }
//
//
//        public static void main (String[] args) {
//            launch(args);
//        }
//}
