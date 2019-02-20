import GameSimulators.*;
import Grid.Grid;
import Nodes.Node;
import XML.Configuration;
import XML.XMLException;
import XML.XMLParse;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Eric Lin
 * @author kunalupadya
 * @Author: Luke Truitt
 *
 * Controller class, connects the GUI to the Back-End.
 * Runs simulation, manages interactions with elements
 * Steps through each movement of the grid
 */
public class Animation extends Application {

    public static final String GAME_NAME = "Cellular Automata";
    public static final Paint BACKGROUND = Color.WHITE;
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public static final int FRAMES_PER_SECOND = 1;
    public static final int MILLISECOND_DELAY = 400/FRAMES_PER_SECOND;
    public static final String DATA_FILE_EXTENSION = "*.xml";
    public static final int MIN_ANIMATION_RATE = 1;
    public static final int MAX_ANIMATION_RATE = 5;
    public static final int DEFAULT_ANIMATION_RATE = 3;
    public static final int Y_POS_GRID_RECTANGLE = 20;
    public static final int X_POS_PLAY_BUTTON = 5;
    public static final int Y_POS_PLAY_BUTTON = 620;
    public static final int X_POS_STOP_BUTTON = 65;
    public static final int Y_POS_STOP_BUTTON = 622;
    public static final int X_POS_FILE_BUTTON = 230;
    public static final int Y_POS_FILE_BUTTON = 725;
    public static final double X_POS_SLIDER = 520;
    public static final double Y_POS_SLIDER = 725;
    public static final double X_POS_CHART = 150;
    public static final double Y_POS_CHART = 510;

    private double gameSpeed;
    private Grid myGrid;
    private Group root;
    private Stage primaryStage;
    private Scene myScene;
    private Simulator mySim;
    private Timeline animation;
    private PlayButton playButton;
    private StopButton stopButton;
    private FileChooserButton myFileChooserButton;
    private SpeedSlider speedSlider;
    private Configuration startingConfiguration;
    private BackgroundRectangle backgroundRectangle;
    private CountsChart popChart;
    private int playCount = 0;
    private int useCount = 0;
    private double timeStep = 0;

    /**
     * Runs through the simulations
     * @param primaryStage default parameter
     * @throws XMLException XML parsing throws errors when bad data is read in
     */
    @Override
    public void start(Stage primaryStage) throws XMLException {
        this.primaryStage = primaryStage;
        myScene = setupGUI();
        primaryStage.setTitle(GAME_NAME);
        primaryStage.setScene(myScene);
        primaryStage.show();
        animation = new Timeline();
    }

    private Scene setupGUI(){
        root = new Group();
        Scene scene = new Scene(root,SCREEN_WIDTH,SCREEN_HEIGHT,BACKGROUND);

        myFileChooserButton = new FileChooserButton(X_POS_FILE_BUTTON, Y_POS_FILE_BUTTON);
        handleFileChoosing();
        root.getChildren().add(myFileChooserButton.getFileChooserButton());

        playButton = new PlayButton(X_POS_PLAY_BUTTON, Y_POS_PLAY_BUTTON);
        handlePlay();
        root.getChildren().add(playButton.getPlayButton());

        stopButton = new StopButton(X_POS_STOP_BUTTON, Y_POS_STOP_BUTTON);
        handlePause();
        root.getChildren().add(stopButton.getStopButton());

        speedSlider = new SpeedSlider(MIN_ANIMATION_RATE, MAX_ANIMATION_RATE,DEFAULT_ANIMATION_RATE, X_POS_SLIDER, Y_POS_SLIDER);
        root.getChildren().add(speedSlider.getSlider());
        root.getChildren().add(speedSlider.getSliderText());

        return scene;
    }

    private void animate() {
        timeStep++;
        clearRoot();
        primaryStage.setTitle(startingConfiguration.getMyTitle());
        myGrid = mySim.go(myGrid);
        myGrid.initializePolygons();
        displayGrid();
        speedSlider.changeSpeed(animation);
        if (mySim.isGameOver()) {
            animation.stop();
        }
        popChart.changeChart(myGrid.getNodes(), mySim, timeStep);
    }

    private void step(){
            animate();
    }

    private void clearRoot() {
        for(int i = 0; i < myGrid.getNumRows(); i++) {
            for(int j = 0; j < myGrid.getNumCols(); j++) {
                Node node = myGrid.getNode(i, j);
                if(node != null) {
                    root.getChildren().remove(node.getPolygon());
                }
            }
        }
    }

    private void displayGrid() {
        for(int i = 0; i < myGrid.getNumRows(); i++) {
            for(int j = 0; j < myGrid.getNumCols(); j++) {
                Node node = myGrid.getNode(i, j);
                if(node != null) {
                    root.getChildren().add(node.getPolygon());
                }
            }
        }
    }

    private void setAnimation() {
        while (playCount == 0) {
            animation = new Timeline();
            var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY / gameSpeed), event -> step());
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.getKeyFrames().add(frame);
            animation.play();
            playCount++;
        }
    }

    private void handlePlay() {
        playButton.getPlayButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setAnimation();
            }
        });
    }

    private void handlePause() {
        stopButton.getStopButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                animation.stop();
                playCount = 0;
            }
        });
    }

    private void revertToBlankGui() {
        playCount = 0;
        if (useCount > 0) {
            timeStep = 0;
            clearRoot();
            root.getChildren().remove(backgroundRectangle.getBackgroundRectangle());
            animation.stop();
            root.getChildren().remove(popChart.getLineChart());
        }
    }

    private void handleXMLData() {
        var dataFile = myFileChooserButton.getMyChooser().showOpenDialog(primaryStage);
        //initialized to empty game only to prevent error wil staringConfiguration not being initialized
        startingConfiguration = new Configuration();
        while (dataFile != null) {
            try {
                startingConfiguration = new XMLParse("media").getGame(dataFile);
                break;
            }
            catch (XMLException exception) {
                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            }
            dataFile = myFileChooserButton.getMyChooser().showOpenDialog(primaryStage);
        }
        gameSpeed = startingConfiguration.getGameSpeed();
    }

    private void initializeGame(Simulator mySim, String simCss) {
        myGrid = (mySim).getGrid();
        popChart = new CountsChart(myGrid, X_POS_CHART, Y_POS_CHART);
        popChart.initializeSeries(mySim);
        myScene.getStylesheets().add(simCss);
    }

    private void initializeNewGui() {
        backgroundRectangle = new BackgroundRectangle(myGrid);
        root.getChildren().add(backgroundRectangle.getBackgroundRectangle());
        myGrid.initializePolygons();
        displayGrid();
        root.getChildren().add(popChart.getLineChart());
        useCount++;
    }
    
    private void handleFileChoosing() {
        myFileChooserButton.getFileChooserButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                revertToBlankGui();
                handleXMLData();
                try {
                    switch (startingConfiguration.getMyTitle()) {
                        case ("Spreading Fire"): {
                            mySim = new SpreadingFire(startingConfiguration);
                            initializeGame(mySim, "StyleSpreading.css");
                            break;
                        }
                        case ("Wa-Tor World"): {
                            mySim = new WaTorWorld(startingConfiguration);
                            initializeGame(mySim, "StyleWaTor.css");
                            break;
                        }
                        case ("Percolation"): {
                            mySim = new Percolation(startingConfiguration);
                            initializeGame(mySim, "StylePercolation.css");
                            break;
                        }
                        case ("Segregation"): {
                            mySim = new Segregation(startingConfiguration);
                            initializeGame(mySim, "StyleSegregation.css");
                            break;
                        }
                        case ("Game of Life"): {
                            mySim = new GameOfLife(startingConfiguration);
                            initializeGame(mySim, "StyleGameOfLife.css");
                            break;
                        }
                        case ("Rock Paper Scissors"): {
                            mySim = new RockPaperScissors(startingConfiguration);
                            initializeGame(mySim, "StyleRPS.css");
                            break;
                        }
                        case ("Self Replication"): {
                            mySim = new SelfReplication(startingConfiguration);
                            initializeGame(mySim, "StyleSelfReplication.css");
                            break;
                        }
                    }
                } catch (XMLException exception){
                    new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
                }
                initializeNewGui();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}