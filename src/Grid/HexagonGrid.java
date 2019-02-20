package Grid;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.shape.Polygon;
import Nodes.Node;

/**
 * Hexagon Grid class
 * Handles the initialization of a grid
 * with hexagonal grid locations
 *
 * @author Eric Lin
 */
public class HexagonGrid extends Grid {
    public static final double HEXAGON_SHAPE_OFFSET = 0.75;

    private boolean isTorus;
    private boolean verticesNeighbor;

    /**
     * Initializes a grid with hexagon node locations
     *
     * @param width             Columns of grid
     * @param height            Rows of grid
     * @param isTorus           Allow wrapping
     * @param verticesNeighbor  Whether or not to consider diagonals as neighbors
     * @param gridOutlineOn     Grid outline boolean
     */
    public HexagonGrid(int width, int height, boolean isTorus, boolean verticesNeighbor, boolean gridOutlineOn) {
        super(width, height, gridOutlineOn);
        this.isTorus = isTorus;
        this.verticesNeighbor = verticesNeighbor;
    }

    /**
     * Obtain neighbors of a node for hexagon shape
     *
     * @param i row
     * @param j column
     * @return   List of neighbors
     */
    public List<Node> getNeighbors(int i, int j) {
        List<Node> nodeNeighbors = new ArrayList<>();
        List<Node> temporaryNeighbors = new ArrayList<>();
        temporaryNeighbors.addAll(Arrays.asList(this.getRight(i, j, this.isTorus), this.getLeft(i, j, this.isTorus), this.getDown(i, j, this.isTorus), this.getUp(i, j, this.isTorus)));

        if ((i % 2) == 0) {
            temporaryNeighbors.addAll(Arrays.asList(this.getUpperLeft(i, j, this.isTorus), this.getLowerLeft(i, j, this.isTorus)));
        } else {
            temporaryNeighbors.addAll(Arrays.asList(this.getUpperRight(i, j, this.isTorus), this.getLowerRight(i, j, this.isTorus)));
        }

        for (Node neighbor : temporaryNeighbors) {
            if (neighbor != null) {
                nodeNeighbors.add(neighbor);
            }
        }
        return nodeNeighbors;
    }

    private Polygon makeHexagon(int i, int j, double d) {
        Polygon hexagon = new Polygon();
        if ((i % 2) == 0) {
            Double[] points = {
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d/4, // top-left
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d, // top
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d/4, // top-right
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + HEXAGON_SHAPE_OFFSET * d, // bottom-right
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d, // bottom
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + HEXAGON_SHAPE_OFFSET * d, // bottom-left
            };
            hexagon.getPoints().addAll(points);
        } else {
            Double[] points = {
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d/4, // top-left
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d, // top
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + 3 * d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d/4, // top-right
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + 3 * d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + HEXAGON_SHAPE_OFFSET * d, // bottom-right
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + d, // bottom
                    (SCREEN_WIDTH-this.size.y * SHAPE_LENGTH)/2 + j * d + d/2, Y_OFFSET + i * HEXAGON_SHAPE_OFFSET * d + HEXAGON_SHAPE_OFFSET * d // bottom-left
            };
            hexagon.getPoints().addAll(points);
        }
        return hexagon;

    }

    /**
     * Draw hexagons on grid
     */
    public void initializePolygons() {
        for (int i = 0; i < this.size.x; i++) {
            for (int j = 0; j < this.size.y; j++) {
                Node node = this.getNode(i, j);
                Polygon hexagon = makeHexagon(i, j, SHAPE_LENGTH);
                linkPolyToNode(node, hexagon);
            }
        }

    }
}
