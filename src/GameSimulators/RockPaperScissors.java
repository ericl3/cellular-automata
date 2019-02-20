package GameSimulators;

import Grid.Grid;
import Nodes.Node;
import Nodes.RPS;
import XML.Cell;
import XML.Configuration;
import XML.XMLException;
import javafx.scene.paint.Color;
import java.util.Collections;
import java.util.Map;
import java.util.List;

/**
 * @Author : Luke Truitt
 * Logic for the Rock Paper Scissors simulator.
 * Uses the rules to change the grid one set at a time.
 */
public class RockPaperScissors extends Simulator {

    public static final Color EMPTY = Color.WHITE;
    public static final Color RED = Color.RED;
    public static final Color BLUE = Color.BLUE;
    public static final Color GREEN = Color.GREEN;

    private double probEmpty;
    private double probRed;
    private double probBlue;
    private int count;

    /**
     * @Author; Luke Truitt
     * @param configuration passes in the parameters set by the XML parsing
     */
    public RockPaperScissors (Configuration configuration){
        super(configuration);
    }

    protected void initializeDefaultCells() {
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    var randSeed = Math.random();
                    if (randSeed < probRed) {
                        Node node = new RPS(i,j, RED, 0);
                        myGrid.addNode(node);
                    } else if (probRed < randSeed && randSeed < probRed+probBlue) {
                        Node node = new RPS(i, j, BLUE, 0);
                        myGrid.addNode(node);
                    } else if (probRed + probBlue < randSeed && randSeed < probRed+probBlue + probEmpty) {
                        Node node = new RPS(i, j, GREEN, 0);
                        myGrid.addNode(node);
                    }else {
                        Node node = new RPS(i, j, EMPTY, 0);
                        myGrid.addNode(node);
                    }
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state){}

    protected void extractParams(Map<String, String> myParams){
        try{
            probEmpty = Double.parseDouble(myParams.get("percentEmpty"))*0.01;
            probRed = Double.parseDouble(myParams.get("probRed"));
            probBlue = Double.parseDouble(myParams.get("probBlue"));
        }
        catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    private Grid handleRPS(Grid grid) {
        for(int i = 0; i < grid.getNumRows(); i++) {
            for(int j = 0; j < grid.getNumCols(); j++) {
                var node = (RPS) myGrid.getNode(i, j);
                var neighbors = myGrid.getNeighbors(i, j);
                Collections.shuffle(neighbors);
                var replaceNode = (RPS) neighbors.get(0);
                if (node.colorsEqual(replaceNode)) {
                    grid.addNode(node);
                } else if (node.currentWin(replaceNode)) {
                    replaceNode = new RPS(replaceNode.getX(), replaceNode.getY(), node.getColor(), 0);
                    grid.addNode(replaceNode);
                    grid.addNode(node);
                } else if (node.replaceWin(replaceNode)){
                    node = new RPS(node.getX(), node.getY(), replaceNode.getColor(), 0);
                    grid.addNode(node);
                } else if (node.isWhite()) {
                        node = new RPS(node.getX(), node.getY(), replaceNode.getColor(), (replaceNode).getShade());
                        grid.addNode(node);
                } else if (replaceNode.isWhite()){
                    replaceNode = new RPS(replaceNode.getX(), replaceNode.getY(), node.getColor(), (node).getShade());
                    grid.addNode(replaceNode);
                    grid.addNode(node);
                }
            }
        }
        return grid;
    }


    /**
     * Runs the simulation
     * @param grid the current grid
     * @return updated grid
     */
    @Override
    public Grid go(Grid grid) {
        myGrid = grid;
        var temp = setGrid(myGridType, grid.getNumRows(), grid.getNumCols(), isTorus,verticesAsNeighbors,gridOutlineOn);
        temp = handleRPS(temp);
        return temp;
    }
}
