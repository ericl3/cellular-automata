package Grid;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;

import java.awt.*;
import java.util.ArrayList;
import Nodes.Node;
import java.util.List;

/**
 * @author Eric Lin
 * @author : Luke Truitt
 * Overall grid class, responsible for maintaining game data
 */
abstract public class Grid {

    public static final double X_OFFSET = 150;
    public static final double Y_OFFSET = 20;
    public static final double SHAPE_LENGTH = 12;
    public static final double SCREEN_WIDTH = 800;

    protected Node[][] myGrid;

    protected Point size;
    private ArrayList<Node> nodeList;
    protected double polygonScale;

    /**
     * Initializes a grid with the given parameters
     * @param gridWidth number of columns
     * @param gridHeight number of rows
     * @param gridOutlineOn space between cells
     */
    public Grid(int gridWidth, int gridHeight, boolean gridOutlineOn) {
        this.size = new Point(gridWidth, gridHeight);
        myGrid = new Node[gridWidth][gridHeight];
        nodeList = new ArrayList<>();
        if (gridOutlineOn){
            polygonScale = 0.9;
        }
        else{
            polygonScale = 1;
        }
    }

    /**
     * Method to load up the shapes inside the grid
     */
    abstract public void initializePolygons();

    /**
     * Method for extracting a node from the grid
     * @param x row
     * @param y column
     * @return a node at the requested point
     */
    public Node getNode(int x, int y) {
        return myGrid[x][y];
    }

    /**
     *
     * @param x
     * @param y
     * @param newNode
     */
    public void setNode(int x, int y, Node newNode) {
        myGrid[x][y] = newNode;
    }

    public Node[][] getGrid() {
        return this.myGrid;
    }

    public List<Node> getNodes() {
        List<Node> myNodes = new ArrayList<>();
        for (int i = 0; i < this.size.x; i++){
            for (int j = 0; j < this.size.y; j++){
                myNodes.add(this.getNode(i, j));
            }
        }

        return myNodes;
    }

    public void removeNode(int i, int j) {
        nodeList.remove(getNode(i, j));
        myGrid[i][j] = null;
    }

    public void addNode(Node node) {
        nodeList.add(node);
        myGrid[node.getX()][node.getY()] = node;
    }

    /**
     * Sets the information for the polygon into the Node
     * @param node Node to update
     * @param polygon to add to Node
     */
    public void linkPolyToNode(Node node, Polygon polygon) {
        var color = node.getColor();
        node.setPolygon(polygon);
        node.setColor(color);
        node.getPolygon().setSmooth(true);
        node.getPolygon().setScaleX(polygonScale);
        node.getPolygon().setScaleY(polygonScale);
    }

    /**
     * Gets all the neighbors of a given cell
     * @param i row
     * @param j column
     * @return a Node
     */
    abstract public List<Node> getNeighbors(int i, int j);

    /**
     * Gets the node to the North of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getUp(int i, int j, boolean isTorus) {
        if (i - 1 >= 0) {
            return this.getNode(i - 1, j);
        } else if (isTorus) {
            return this.getNode(this.size.x - 1, j);
        }
        return null;
    }

    /**
     * Gets the node to the South of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getDown(int i, int j, boolean isTorus) {
        if (i + 1 < this.size.x) {
            return this.getNode(i + 1, j);
        } else if (isTorus) {
            return this.getNode(0, j);
        }
        return null;
    }

    /**
     * Gets the node to the West of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getLeft(int i, int j, boolean isTorus) {
        if (j - 1 >= 0) {
            return this.getNode(i, j - 1);
        } else if (isTorus) {
            return this.getNode(i, this.size.y - 1);
        }
        return null;
    }

    /**
     * Gets the node to the East of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getRight(int i, int j, boolean isTorus) {
        if (j + 1 < this.size.y) {
            return this.getNode(i, j + 1); // Down
        } else if (isTorus) {
            return this.getNode(i, 0);
        }
        return null;
    }

    /**
     * Gets the node to the North West of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getUpperLeft(int i, int j, boolean isTorus) {
        if (i - 1 >= 0 && j - 1 >= 0) {
            return this.getNode(i - 1, j - 1);
        } else if (isTorus) {
            Node upNode = this.getUp(i, j, true);
            return this.getLeft(upNode.getX(), upNode.getY(), true);
        }
        return null;
    }

    /**
     * Gets the node to the South West of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getLowerLeft(int i, int j, boolean isTorus) {
        if (i + 1 < this.size.x && j - 1 >= 0) {
            return this.getNode(i + 1, j - 1);
        } else if (isTorus) {
            Node downNode = this.getUp(i, j, true);
            return this.getLeft(downNode.getX(), downNode.getY(), true);
        }
        return null;
    }

    /**
     * Gets the node to the North East of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getUpperRight(int i, int j, boolean isTorus) {
        if (j + 1 < this.size.y && i - 1 >= 0) {
            return this.getNode(i - 1, j + 1); // upper-right
        } else if (isTorus) {
            Node upNode = this.getDown(i, j, true);
            return this.getRight(upNode.getX(), upNode.getY(), true);
        }
        return null;
    }

    /**
     * Gets the node to the South East of it
     * @param i row
     * @param j column
     * @param isTorus wrap around grid
     * @return a Node
     */
    public Node getLowerRight(int i, int j, boolean isTorus) {
        if (j + 1 < this.size.y && i + 1 < this.size.x) {
            return this.getNode(i + 1, j + 1); // bottom-left
        } else if (isTorus) {
            Node downNode = this.getDown(i, j, true);
            return this.getRight(downNode.getX(), downNode.getY(), true);
        }
        return null;
    }

    public int getNumRows(){
        return size.x;
    }

    public int getNumCols(){
        return size.y;
    }

    /**
     * gets the nodes around the given point
     * @param i row
     * @param j column
     * @param isTorus loop around the grid
     * @return an array of nodes around it
     */
    public Node[] getSurrounding(int i, int j, boolean isTorus) {
        Node[] nodes = new Node[4];
        if (j + 1 < this.size.y) {
            nodes[0] = this.getNode(i, j + 1); // Down
        } else if (isTorus) {
            nodes[0] = this.getNode(i, 0);
        }
        if (j - 1 >= 0) {
            nodes[1] = this.getNode(i, j - 1); // Up
        } else if (isTorus) {
            nodes[1] = this.getNode(i, this.size.y - 1);
        }
        if (i + 1 < this.size.x) {
            nodes[2] = this.getNode(i + 1, j); // Right
        } else if (isTorus) {
            nodes[2] = this.getNode(0, j);
        }
        if (i - 1 >= 0) {
            nodes[3] = this.getNode(i - 1, j); // Left
        } else if (isTorus) {
            nodes[3] = this.getNode(this.size.x - 1, j);
        }
        return nodes;
    }

}
