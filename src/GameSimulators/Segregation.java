package GameSimulators;

import java.util.List;
import java.util.ArrayList;

import Grid.Grid;
import Nodes.Node;
import XML.Configuration;
import XML.XMLException;
import javafx.scene.paint.Color;
import java.util.Collections;
import java.util.Map;

/**
 * Segregation Class: Handles the simulation logic
 * for the segregation simulation
 *
 * I have chosen to refactor this class for my masterpiece. This is
 * because this code had a lot of room to improve in terms of making
 * the code more modular. Before, some of the methods were extremely long,
 * such as the HandleSegregation method, but now I have the method to call
 * many smaller helper methods so that it is clear what exactly this method
 * does. I also noticed that I had a redundancy in my previous version to this
 * where I would have a list of satisfied nodes that I would add to my original
 * grid. This was not necessary at all, though, because I was not creating
 * a new temporary grid to add nodes to. Instead, I am collecting a list
 * of dissatisfied and empty nodes and swapping them on the original grid
 * (note that this does NOT effect the state of a node that becomes neighbors
 * of a newly swapped in node). Also, since this simulation was not as trivial
 * as other simulations such as SpreadingFire or GameOfLife that only needed
 * to change a current nodes state based on neighbors, I felt as though I did
 * a good job of implementing a new simulation with a different logic structure.
 *
 * In this refactored version, I have also decided to make my dissatisfiedList
 * and emptyList as instance variables. This allows me to reduce the amount
 * of Lists that I need to pass through methods. I believe this is better
 * design practice because these lists only need to communicate within the scope
 * of this simulation, so passing different arguments in should not be necessary
 * unless they belong to other scopes. Thus, this masterpiece code is much easier
 * to understand and reduces some redundancies in code. 
 *
 * @author Eric Lin
 * @author kunalupadya
 */
public class Segregation extends Simulator {
    public static final Color EMPTY = Color.WHITE;
    public static final Color RED = Color.RED;
    public static final Color BLUE = Color.BLUE;
    public static final double PERCENT_TO_PROBABILITY = 0.01;

    private double satisfactionThreshold;
    private double probEmpty;
    private double probRed;
    private List<Node> emptyNodes;
    private List<Node> dissatisfiedList;

    public Segregation (Configuration configuration){
        super(configuration);
    }

    protected void initializeDefaultCells() {
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    double randSeed = Math.random();
                    if (randSeed < probRed) {
                        myGrid.addNode(new Node(i,j, RED));
                    } else if (probRed < randSeed && randSeed < probRed+probEmpty) {
                        myGrid.addNode(new Node(i, j, EMPTY));
                    } else {
                        myGrid.addNode(new Node(i, j, BLUE));
                    }
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state) {
        switch (state){
            case("red"):{
                myGrid.setNode(i,j, new Node(i, j, RED));
                break;
            }
            case("blue"):{
                myGrid.setNode(i,j, new Node(i, j, BLUE));
                break;
            }
            case("empty"):{
                myGrid.setNode(i,j, new Node(i, j, EMPTY));
                break;
            }
        }
    }

    protected void extractParams(Map<String, String> myParams){
        try{
            satisfactionThreshold = Double.parseDouble(myParams.get("percentSimilarSatisfaction"))* PERCENT_TO_PROBABILITY;
            probEmpty = Double.parseDouble(myParams.get("percentEmpty"))*PERCENT_TO_PROBABILITY;
            probRed = Double.parseDouble(myParams.get("probRed"));
            }
            catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    private List<Node> returnColoredNeighbors(Node node) {
        List<Node> allNeighbors = this.myGrid.getNeighbors(node.getX(), node.getY());
        List<Node> coloredNeighbors = new ArrayList<>();
        for (Node n : allNeighbors) {
            if (n.getColor() != Color.WHITE) {
                coloredNeighbors.add(n);
            }
        }
        return coloredNeighbors;
    }

    private double fractionSimilar(Node node) {
         List<Node> coloredNeighbors = returnColoredNeighbors(node);
         double similar = 0;
         double total = coloredNeighbors.size();

         for (Node n : coloredNeighbors) {
             if (n.getColor() == node.getColor()){
                 similar++;
             }
         }

         return (similar / total);
    }

    private void setEmptyNodes() {
        emptyNodes = new ArrayList<>();
        for (int i = 0; i < myGrid.getNumRows(); i++){
            for (int j = 0; j < myGrid.getNumCols(); j++){
                if (myGrid.getNode(i, j).getColor() == Color.WHITE) {
                    emptyNodes.add(myGrid.getNode(i, j));
                }
            }
        }
    }

    private void setDissatisfied() {
        dissatisfiedList = new ArrayList<>();
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                Node currNode = myGrid.getNode(i, j);
                if (currNode.getColor() != Color.WHITE && fractionSimilar(currNode) < satisfactionThreshold) {
                    dissatisfiedList.add(currNode);
                }
            }
        }
    }

    private void initializeDissatisfiedAndEmptyNodes() {
        setDissatisfied();
        setEmptyNodes();
        Collections.shuffle(dissatisfiedList);
        Collections.shuffle(emptyNodes);
    }

    private void swapEmptyAndDissatisfied(Grid temp) {
        Node tempNode1 = new Node(dissatisfiedList.get(0).getX(), dissatisfiedList.get(0).getY(), Color.WHITE);
        Node tempNode2 = new Node(emptyNodes.get(0).getX(), emptyNodes.get(0).getY(), dissatisfiedList.get(0).getColor());
        temp.addNode(tempNode1);
        temp.addNode(tempNode2);
        dissatisfiedList.remove(0);
        emptyNodes.remove(0);
    }

    private void addRemainingNodes(Grid temp) {
        if (dissatisfiedList.size() != 0) {
            for (Node node : dissatisfiedList) {
                temp.addNode(node);
            }
        }
        if (emptyNodes.size() != 0) {
            for (Node node : emptyNodes) {
                temp.addNode(node);
            }
        }
    }

    private Grid handleSegregation(Grid temp) {
        initializeDissatisfiedAndEmptyNodes();
        while (dissatisfiedList.size() != 0 && emptyNodes.size() != 0) {
           swapEmptyAndDissatisfied(temp);
        }
        addRemainingNodes(temp);
        return temp;

    }

    /**
     * obtain grid for segregation simulation
     *
     * @return  segregation grid
     */
    public Grid getGrid() {
        return this.myGrid;
    }

    /**
     * Handle animation and logic for segregation simulation
     *
     * @param grid  Grid to handle logic on
     * @return  same grid, but with edited node states and locations
     */
    @Override
    public Grid go(Grid grid) {
        myGrid = grid;
        return handleSegregation(grid);
    }
}