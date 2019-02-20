package Grid;

import javafx.scene.shape.Polygon;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import Nodes.Node;

/**
 * @Author : Luke Truitt
 * @author Eric Lin 
 * Rectangular version of the subclass
 * Major difference is how it initializes and draws each individual cell
 */
public class RectangleGrid extends Grid {
    private boolean isTorus;
    private boolean verticesNeighbor;

    /**
     * Sets up the initial grid based on the parameters
     * @param width columns
     * @param height rows
     * @param isTorus wrap around
     * @param verticesNeighbor add the vertices as neighbors
     * @param gridOutlineOn have each cell be outlined
     */
    public RectangleGrid(int width, int height, boolean isTorus, boolean verticesNeighbor, boolean gridOutlineOn) {
        super(width, height, gridOutlineOn);
        this.isTorus = isTorus;
        this.verticesNeighbor = verticesNeighbor;
    }

    /**
     * Returns the neighbors to check against <Most important method>
     * @param i column
     * @param j row
     * @return a list of the neighbors for the node at i, j
     */
    public List<Node> getNeighbors(int i, int j) {
        List<Node> nodeNeighbors = new ArrayList<>();
        List<Node> temporaryNeighbors = new ArrayList<>();

        temporaryNeighbors.addAll(Arrays.asList(this.getLeft(i, j, this.isTorus), this.getUp(i, j, this.isTorus), this.getRight(i, j, this.isTorus), this.getDown(i, j, this.isTorus)));
        if (verticesNeighbor) {
            temporaryNeighbors.addAll(Arrays.asList(this.getUpperLeft(i, j, this.isTorus), this.getUpperRight(i, j, this.isTorus), this.getLowerLeft(i, j, this.isTorus), this.getLowerRight(i, j, this.isTorus)));
        }

        for (Node neighbor : temporaryNeighbors) {
            if (neighbor != null) {
                nodeNeighbors.add(neighbor);
            }
        }
        return nodeNeighbors;
    }

    private Polygon makeRectangle(int i, int j, double d) {
         Polygon rectangle = new Polygon();
             Double[] points = {
                    (SCREEN_WIDTH - this.size.y * SHAPE_LENGTH)/2 + j * d, Y_OFFSET + i * d,
                    (SCREEN_WIDTH - this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * d,
                    (SCREEN_WIDTH - this.size.y * SHAPE_LENGTH)/2 + j * d + d, Y_OFFSET + i * d + d,
                    (SCREEN_WIDTH - this.size.y * SHAPE_LENGTH)/2 + j * d, Y_OFFSET + i * d + d,
             };
             rectangle.getPoints().addAll(points);
         return rectangle;
    }

    /**
     * Draws rectangles at a given location
     */
        public void initializePolygons() {
            for (int i = 0; i < this.size.x; i++) {
                for (int j = 0; j < this.size.y; j++) {
                    Node node = this.getNode(i, j);
                    if(node == null) {
                        continue;
                    }
                    Polygon rectangle = makeRectangle(i, j, SHAPE_LENGTH);
                    linkPolyToNode(node, rectangle);
                }
            }
        }

}
