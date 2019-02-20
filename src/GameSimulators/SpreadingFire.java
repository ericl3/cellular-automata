package GameSimulators;

import Grid.Grid;
import Nodes.Node;
import XML.Cell;
import XML.Configuration;
import XML.Enumerations.CellStates;
import XML.XMLException;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Map;

/**
 * @author Luke_Truitt
 * @author kunalupadya
 * @author Eric Lin
 * Logic for the Spreading Fire Simulation
 */
public class SpreadingFire extends Simulator {
    public static final Color EMPTY_CELL = Color.YELLOW;
    public static final Color BURNING_CELL = Color.RED;
    public static final Color TREE_CELL = Color.GREEN;
    public static final String EMPTY_XML = CellStates.SPREADING_FIRE.getMyPossibleStates().get(0);
    public static final String TREE_XML = CellStates.SPREADING_FIRE.getMyPossibleStates().get(1);
    public static final String BURNING_XML = CellStates.SPREADING_FIRE.getMyPossibleStates().get(2);
    public static final String DEFAULT_XML = CellStates.SPREADING_FIRE.defaultStateIs();


    private double probCatch = 0.7;

    /**
     * Uploads current simulation parameters
     * @param configuration the parameters for the game
     */
    public SpreadingFire(Configuration configuration){
        super(configuration);
    }


    protected void initializeDefaultCells() {
        for(int i = 0; i< myGrid.getNumRows(); i++) {
            for(int j = 0; j< myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i,j) == null) {
                    initializeCellFromXML(i,j,DEFAULT_XML);
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state) {
        if (state.equals(EMPTY_XML)){
            myGrid.setNode(i,j, new Node(i, j, EMPTY_CELL));
        }
        else if (state.equals(TREE_XML)){
            myGrid.setNode(i,j, new Node(i, j, TREE_CELL));
        }
        else if (state.equals(BURNING_XML)){
            myGrid.setNode(i,j, new Node(i, j, BURNING_CELL));
        }
    }

    protected void extractParams(Map<String, String> myParams){
        try{
        probCatch = Double.parseDouble(myParams.get("probabilityOfCatchingFire"));
        }
        catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    private Node spread(Node node) {
        Node temp = new Node(node.getX(), node.getY(), Color.GREEN);
        temp.setPolygon(node.getPolygon());
        if (node.getColor() == Color.YELLOW) {
            return node;
        }
        if (node.getColor() == Color.GREEN) {
            List<Node> nodeNeighbors = this.myGrid.getNeighbors(node.getX(), node.getY());
            for (Node neighbor : nodeNeighbors) {
                if (neighbor.getColor() == Color.RED) {
                    if (Math.random() < probCatch) {
                        temp.setColor(Color.RED);
                    }
                    break;
                }
            }
        } else if (node.getColor() == Color.RED) {
            temp.setColor(Color.YELLOW);
        }
        return temp;
    }

    /**
     * Run the simulations
     * @param grid the current grid
     * @return updated grid
     */
    @Override
    public Grid go(Grid grid) {
        myGrid = grid;
        var temp = setGrid(myGridType, grid.getNumRows(), grid.getNumCols(), isTorus,verticesAsNeighbors,gridOutlineOn);
        for (int i = 0; i<grid.getNumRows(); i++){
            for(int j = 0; j<grid.getNumCols(); j++){
                Node node = grid.getNode(i, j);
                temp.addNode(spread(node));
            }
        }

        return temp;
    }
}
