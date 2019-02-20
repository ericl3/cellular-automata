package GameSimulators;

import Grid.Grid;
import Grid.HexagonGrid;
import Grid.RectangleGrid;
import Grid.TriangleGrid;
import XML.Cell;
import XML.Configuration;
import XML.XMLException;


import java.util.List;
import java.util.Map;

/**
 * @author Eric Lin
 * @author kunalupadya
 * @author Luke Truitt
 * Abstract simulation class responsible for the overarching ideas behind each simulation.
 */
abstract public class Simulator {
    public static final String paramsError = "XML params are invalid";

    protected String gameType;
    protected Grid myGrid;
    protected boolean gameOver = false;
    protected String myGridType;
    protected Boolean isTorus;
    protected Boolean verticesAsNeighbors;
    protected Boolean gridOutlineOn;

    /**
     * @return if the simulation has ended
     */

    public Simulator(Configuration configuration){
        List<Cell> cells = configuration.getCells();
        int width = configuration.getNumRows();
        int height = configuration.getNumCols();
        Map<String, String> myParams = configuration.getMyParams();
        myGridType = configuration.getGridType();
        isTorus = configuration.getTorus();
        verticesAsNeighbors = configuration.getVerticesAsNeighbors();
        gridOutlineOn = configuration.getGridOutlineOn();
        myGrid = setGrid(myGridType,width, height, isTorus, verticesAsNeighbors, gridOutlineOn);
        extractParams(myParams);
        extractCellsFromXML(cells);
        initializeDefaultCells();
    }
    protected abstract void extractParams(Map<String, String> myParams);

    protected abstract void initializeDefaultCells();

    private void extractCellsFromXML(List<Cell> cells) {
        for (Cell cell:cells){
            int i = cell.getRow()-1;
            int j = cell.getColumn()-1;
            String state = cell.getCellState();
            initializeCellFromXML(i, j, state);
        }
    }

    protected abstract void initializeCellFromXML(int i, int j, String state);



    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * The method that runs the simulations
     * @param grid the current grid
     * @return the updated grid
     */
    abstract public Grid go(Grid grid);

    /**
     * @return current grid
     */
    public Grid getGrid() {
        return this.myGrid;
    }

    /**
     * Sets the initial grid for the given sim
     * @param gridType Shape of the nodes
     * @param width number of columns
     * @param height number of rows
     * @param isTorus infinite plane or not
     * @param verticesNeighbor do we want the vertices to be neighbors
     * @param gridOutlineOn do we want each cell to be outlined
     * @return initialized grid
     */
    protected Grid setGrid(String gridType, int width, int height, boolean isTorus, boolean verticesNeighbor, boolean gridOutlineOn){
        Grid grid;
        switch (gridType){
            case ("triangle"):{
                grid = new TriangleGrid(width, height, isTorus, verticesNeighbor, gridOutlineOn);
                break;
            }

            case ("rectangle"):{
                grid = new RectangleGrid(width, height, isTorus, verticesNeighbor, gridOutlineOn);
                break;
            }

            case ("hexagon"):{
                grid = new HexagonGrid(width, height, isTorus, verticesNeighbor, gridOutlineOn);
                break;
            }
            default:{
                throw new XMLException("Invalid grid type");
            }
        }
        return grid;
    }

    public Boolean getTorus() {
        return isTorus;
    }

    public Boolean getVerticesAsNeighbors() {
        return verticesAsNeighbors;
    }

    public Boolean getGridOutlineOn() {
        return gridOutlineOn;
    }
}
