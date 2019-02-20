import Grid.Grid;
import Grid.HexagonGrid;
import Grid.TriangleGrid;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * BackgroundRectangle Class: Initializes the grid outline rectangle
 * in the background of the grid
 *
 * @author Eric Lin
 */
public class BackgroundRectangle extends Animation {

    public static final double HEXAGON_ROW_MULTIPLIER = 0.75;
    public static final double SHAPE_LENGTH = 12;
    public static final double NON_RECTANGLE_COLUMN_OFFSET = 6;
    public static final double HEXAGON_ROW_OFFSET = 3;

    private Rectangle backgroundRectangle;

    /**
     * Constructor for the background grid. Grid outline
     * changes based on what grid shapes we are using
     *
     * @param myGrid    Grid of simulation
     */
    public BackgroundRectangle(Grid myGrid) {
        backgroundRectangle = new Rectangle();
        if (myGrid instanceof HexagonGrid) {
            backgroundRectangle.setHeight(HEXAGON_ROW_MULTIPLIER * myGrid.getNumRows() * SHAPE_LENGTH + HEXAGON_ROW_OFFSET);
            backgroundRectangle.setWidth(myGrid.getNumCols() * SHAPE_LENGTH + NON_RECTANGLE_COLUMN_OFFSET);
            backgroundRectangle.setX((SCREEN_WIDTH-myGrid.getNumCols() * SHAPE_LENGTH)/2);
            backgroundRectangle.setY(Y_POS_GRID_RECTANGLE);
        } else if (myGrid instanceof TriangleGrid){
            backgroundRectangle.setHeight(myGrid.getNumRows() * SHAPE_LENGTH);
            backgroundRectangle.setWidth(myGrid.getNumRows() * SHAPE_LENGTH + NON_RECTANGLE_COLUMN_OFFSET);
            backgroundRectangle.setX((SCREEN_WIDTH-myGrid.getNumRows() * SHAPE_LENGTH)/2);
            backgroundRectangle.setY(Y_POS_GRID_RECTANGLE);
        } else {
            backgroundRectangle.setHeight(myGrid.getNumRows() * SHAPE_LENGTH);
            backgroundRectangle.setWidth(myGrid.getNumCols() * SHAPE_LENGTH);
            backgroundRectangle.setX((SCREEN_WIDTH-myGrid.getNumCols() * SHAPE_LENGTH)/2);
            backgroundRectangle.setY(Y_POS_GRID_RECTANGLE);
        }
        backgroundRectangle.setFill(Color.BLACK);
    }

    /**
     * Obtain the rectangle object of background grid
     *
     * @return      Background rectangle object
     */
    public Rectangle getBackgroundRectangle() {
        return this.backgroundRectangle;
    }

}
