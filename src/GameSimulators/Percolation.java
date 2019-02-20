package GameSimulators;

import Grid.Grid;
import Nodes.Node;
import XML.Cell;
import XML.Configuration;
import XML.XMLException;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Percolation class: Handles the simulation logic for
 * Percolation simulation
 *
 * @author Eric Lin
 * @author kunalupadya
 */
public class Percolation extends Simulator {
    public static final String paramsError = "XML GameSimulators.Percolation params are invalid";
    public static final Color BLOCKED = Color.BLACK;
    public static final Color OPEN = Color.WHITE;
    public static final Color WATER = Color.CYAN;

    private double probBlocked = 0.55;
    private Node startingNode; // Starting cell cannot start on corners.
    private List<Node> hasPercolated = new ArrayList<>();

    /**
     * Constructor for instantiating percolation simulation
     *
     * @param configuration     initial configuration for simulation
     */
    public Percolation(Configuration configuration){
        super(configuration);
    }

    protected void initializeDefaultCells() {
        for(int i = 0; i< myGrid.getNumRows(); i++) {
            for(int j = 0; j< myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    if (i == myGrid.getNumRows() / 2 && j == 0) {
                        Node waterNode = new Node(i, j, Color.CYAN);
                        myGrid.addNode(waterNode);
                        startingNode = waterNode;
                    } else if (Math.random() < probBlocked) {
                        Node blockedNode = new Node(i, j, BLOCKED);
                        myGrid.addNode(blockedNode);
                    } else {
                        Node openNode = new Node(i, j, OPEN);
                        myGrid.addNode(openNode);
                    }
                }
            }
        }
    }


    protected void initializeCellFromXML(int i, int j, String state) {
        switch (state){
            case("blocked"):{
                myGrid.setNode(i,j, new Node(i, j, BLOCKED));
                break;
            }
            case("open"):{
                myGrid.setNode(i,j, new Node(i, j, OPEN));
                break;
            }
        }
    }

    protected void extractParams(Map<String, String> myParams){
        try{
            probBlocked = 1-Double.parseDouble(myParams.get("percentOpen"))*0.01;
        }
        catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    private void percolate(Node node, Grid temp) {
        List<Node> nodeNeighbors = this.myGrid.getNeighbors(node.getX(), node.getY());
        if (node.getColor() == Color.CYAN) {
            temp.addNode(node);
            for (Node neighbor : nodeNeighbors) {
                if (neighbor.getColor() == Color.BLACK) {
                    temp.addNode(neighbor);
                } else if (neighbor.getColor() == Color.WHITE) {
                    hasPercolated.add(neighbor);
                    Node waterFilled = new Node(neighbor.getX(), neighbor.getY(), Color.CYAN);
                    temp.addNode(waterFilled);
                } else {
                    temp.addNode(neighbor);
                }
            }
        } else if (node.getColor() == Color.BLACK) {
            temp.addNode(node);
        } else if (node.getColor() == Color.WHITE && !hasPercolated.contains(node)) {
            temp.addNode(node);
        }
    }

    private Node[] getOppositeEdge(Grid temp) {
        Node[] oppositeEdge = new Node[temp.getNumRows()];
        if (startingNode.getX() == 0) {
            oppositeEdge = temp.getGrid()[temp.getNumRows()-1];
        } else if (startingNode.getX() == temp.getNumRows()-1) {
            oppositeEdge = temp.getGrid()[0];
        } else if (startingNode.getY() == 0) {
            for (int i = 0; i < temp.getNumRows(); i++) {
                oppositeEdge[i] = temp.getGrid()[i][temp.getNumCols()-1];
            }
        } else {
            for (int i = 0; i < temp.getNumRows(); i++) {
                oppositeEdge[i] = temp.getGrid()[i][0];
            }
        }
        return oppositeEdge;
    }

    private boolean isConnected(Grid temp) {
        boolean isConnected = false;
        Node[] oppositeEdge = getOppositeEdge(temp);
        for (Node n : oppositeEdge) {
            if (n.getColor() == Color.CYAN) {
                isConnected = true;
            }
        }
        return isConnected;
    }

    /**
     * Obtain Percolation grid
     *
     * @return  Percolation Grid
     */
    public Grid getGrid() {
        return this.myGrid;
    }

    /**
     * Handle the percolation logic for every node in the grid
     *
     * @param grid  Grid to have nodes edited on.
     * @return new grid with new node states on every step
     */
    @Override
    public Grid go(Grid grid) {
        this.myGrid = grid;
        Grid temp = setGrid(myGridType, grid.getNumRows(), grid.getNumCols(), isTorus,verticesAsNeighbors,gridOutlineOn);
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                percolate(grid.getNode(i, j), temp);
            }
        }
        hasPercolated.clear();
        if(isConnected(temp)) {
            gameOver = true;
        }
        return temp;
    }

}
