package Grid;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import Nodes.Node;

import javafx.scene.shape.Polygon;

/**
 * Class to handle grid that contains triangle shapes
 *
 * @author Eric Lin
 */
public class TriangleGrid extends Grid {
    private boolean isTorus;
    private boolean verticesNeighbor;

    /**
     * Initializes Grid with triangle node locations
     *
     * @param width             Columns of grid
     * @param height            Rows of grid
     * @param isTorus           Wrapping
     * @param verticesNeighbor  Allow neighbors to include vertices touching nodes
     * @param gridOutlineOn     turn on or off grid outline
     */
    public TriangleGrid(int width, int height, boolean isTorus, boolean verticesNeighbor, boolean gridOutlineOn) {
        super(width, height, gridOutlineOn);
        this.isTorus = isTorus;
        this.verticesNeighbor = verticesNeighbor;
    }

    /**
     * Obtain neighbors for a triangle node
     *
     * @param i row
     * @param j column
     * @return  All neighbors in a list for a node
     */
    public List<Node> getNeighbors(int i, int j) {
        List<Node> nodeNeighbors = new ArrayList<>();
        List<Node> temporaryNeighbors = new ArrayList<>();

        Node leftNode = this.getLeft(i, j, this.isTorus);
        Node upNode = this.getUp(i, j, this.isTorus);
        Node rightNode = this.getRight(i, j, this.isTorus);
        Node downNode = this.getDown(i, j, this.isTorus);
        Node upperLeftNode = this.getUpperLeft(i, j, this.isTorus);
        Node upperRightNode = this.getUpperRight(i, j, this.isTorus);
        Node lowerLeftNode = this.getLowerLeft(i, j, this.isTorus);
        Node lowerRightNode = this.getLowerRight(i, j, this.isTorus);

        temporaryNeighbors.addAll(Arrays.asList(leftNode, rightNode));

        if ((i + j) % 2 == 0) {
            temporaryNeighbors.add(downNode);
        } else {
            temporaryNeighbors.add(upNode);
        }

        if (verticesNeighbor) {
            temporaryNeighbors.addAll(Arrays.asList(upperLeftNode, upperRightNode, lowerLeftNode, lowerRightNode));
            if (leftNode != null) {
                temporaryNeighbors.add(this.getLeft(leftNode.getX(), leftNode.getY(), this.isTorus));
            }
            if (rightNode != null) {
                temporaryNeighbors.add(this.getRight(rightNode.getX(), rightNode.getY(), this.isTorus));
            }
            if ((i + j) % 2 == 0) {
                if (lowerRightNode != null) {
                    temporaryNeighbors.add(this.getRight(lowerRightNode.getX(), lowerRightNode.getY(), this.isTorus));
                }
                if (lowerLeftNode != null) {
                    temporaryNeighbors.add(this.getLeft(lowerLeftNode.getX(), lowerLeftNode.getY(), this.isTorus));
                }
                temporaryNeighbors.add(upNode);
            } else {
                if (upperLeftNode != null) {
                    temporaryNeighbors.add(this.getLeft(upperLeftNode.getX(), upperLeftNode.getY(), this.isTorus));
                }
                temporaryNeighbors.add(downNode);
                if (upperRightNode != null) {
                    temporaryNeighbors.add(this.getRight(upperRightNode.getX(), upperRightNode.getY(), this.isTorus));
                }
            }
        }

        for (Node neighbor : temporaryNeighbors) {
            if (neighbor != null) {
                nodeNeighbors.add(neighbor);
            }
        }
        return nodeNeighbors;
    }

    private Polygon makeUpwardsTriangle(int i, int j, double d) {
        Polygon upwardsTriangle = new Polygon();
        Double[] points = {
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2 + d/2, Y_OFFSET + i * d,
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2, Y_OFFSET + i * d + d,
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2 + d, Y_OFFSET + i * d + d
        };
        upwardsTriangle.getPoints().addAll(points);
        return upwardsTriangle;
    }

    private Polygon makeDownwardsTriangle(int i, int j, double d) {
        Polygon downwardsTriangle = new Polygon();
        Double[] points = {
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2 + d/2, Y_OFFSET + i * d + d,
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2, Y_OFFSET + i * d,
                (SCREEN_WIDTH-this.size.x * SHAPE_LENGTH)/2 + j * d/2 + d, Y_OFFSET + i * d
        };
        downwardsTriangle.getPoints().addAll(points);
        return downwardsTriangle;
    }

    /**
     * Draw triangle nodes on the grid
     */
    public void initializePolygons() {
        for (int i = 0; i < this.size.x; i++) {
            for (int j = 0; j < this.size.y; j++) {
                Node node = this.getNode(i, j);
                if ((i + j) % 2 == 0) {
                    Polygon upwardsTriangle = makeUpwardsTriangle(i, j, SHAPE_LENGTH);
                    linkPolyToNode(node, upwardsTriangle);
                } else {
                    Polygon downwardsTriangle = makeDownwardsTriangle(i, j, SHAPE_LENGTH);
                    linkPolyToNode(node, downwardsTriangle);
                }
            }
        }

    }
}
