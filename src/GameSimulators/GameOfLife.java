package GameSimulators;

import java.util.List;
import java.util.Map;

import Grid.Grid;
import Nodes.Node;
import XML.Cell;
import XML.Configuration;
import XML.XMLException;
import javafx.scene.paint.Color;

/**
 * Game of Life Class: Handles the simulation logic for
 * the Game of Life simulation
 *
 * @author Eric Lin
 * @author kunalupadya
 * */
public class GameOfLife extends Simulator {
    public static final String paramsError = "XML Game of Life params are invalid";
    public static final Color ALIVE = Color.BLACK;
    public static final Color DEAD = Color.WHITE;
    public static final double PERCENT_TO_PROBABILITY = 0.01;

    private double percentAlive;

    /**
     * Constructor for Game of Life, instantiate GameOfLife simulation
     *
     * @param configuration     XML configuration parameters to initialize simulation.
     */
    public GameOfLife(Configuration configuration){
        super(configuration);
    }

    protected void initializeDefaultCells() {
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    var randSeed = Math.random();
                    if (randSeed < percentAlive) {
                        Node node = new Node(i,j, ALIVE);
                        myGrid.addNode(node);
                    } else {
                        Node node = new Node(i, j, DEAD);
                        myGrid.addNode(node);
                    }
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state) {
        switch (state){
            case("alive"):{
                myGrid.setNode(i,j, new Node(i, j, ALIVE));
                break;
            }
            case("dead"):{
                myGrid.setNode(i,j,new Node(i, j, DEAD));
                break;
            }
        }
    }

    protected void extractParams(Map<String, String> myParams){
        try{
             percentAlive = Double.parseDouble(myParams.get("percentAlive"))* PERCENT_TO_PROBABILITY;
        }
        catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    private boolean isAlive(Node node) {
        return node.getColor() == ALIVE;
    }

    private int neighboringAliveCount(List<Node> nodeNeighbors) {
        int aliveCount = 0;
        for (Node n : nodeNeighbors) {
            if (n.getColor() == ALIVE) {
                aliveCount++;
            }
        }
        return aliveCount;
    }

    private Node thisIsLife(Node node) {
        List<Node> nodeNeighbors = this.myGrid.getNeighbors(node.getX(), node.getY());
        boolean isAlive = isAlive(node);
        int aliveCount = neighboringAliveCount(nodeNeighbors);
        Node temp;
        if (isAlive) {
            if (aliveCount < 2 || aliveCount > 3) {
                temp = new Node(node.getX(), node.getY(), DEAD); // Dies
            } else {
                temp = node;
            }
        } else {
            if (aliveCount == 3) {
                temp = new Node(node.getX(), node.getY(), ALIVE);
            } else {
                temp = node;
            }
        }
        return temp;
    }

    /**
     * Obtain the game of life simulation grid
     *
     * @return  game of life grid
     */
    public Grid getGrid() {
        return this.myGrid;
    }

    /**
     * Handle simulation logic on each node and set new nodes to
     * temporary grid that is returned
     *
     * @param grid  Grid to have nodes modified
     * @return      temp grid that contains the new nodes and their locations
     */
    @Override
    public Grid go(Grid grid) {
        myGrid = grid;
        var temp = setGrid(myGridType, grid.getNumRows(), grid.getNumCols(), isTorus,verticesAsNeighbors,gridOutlineOn);
        for (int i = 0; i < grid.getNumRows(); i++){
            for (int j = 0; j < grid.getNumCols(); j++){
                Node node = grid.getNode(i, j);
                temp.addNode(thisIsLife(node));
            }
        }
        return temp;
    }

}
