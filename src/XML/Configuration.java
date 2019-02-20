package XML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Grid.Grid;


/**
 * @author kunalupadya
 */
public class Configuration {

    public static final String DATA_TYPE = "Game";
    public static final String DIMENSION_ERROR = "There is an error with the dimensions of the game specified";
    public static final String GAME_SPEED_INVALID = "Game speed is invalid";
    public static final String GRID_TYPE_INVALID = "Grid type is invalid";
    public static final String GAME_NOT_FOUND = "Game not found";
    public static final String TORUS_IS_INVALID = "isTorus is invalid";
    public static final String VERTICES_AS_NEIGHBORS_IS_INVALID = "Vertices as neighbors is invalid";
    public static final String GRID_OUTLINE_IS_INVALID = "Grid outline is invalid";

    public static final List<String> DATA_FIELDS = List.of(
            "title",
            "numRows",
            "numCols",
            "gameSpeed",
            "gridType",
            "isTorus",
            "verticesAsNeighbors",
            "gridOutline",
            "cells",
            "params"
    );


    private Map<String, String> myDataValues;

    private String myTitle;
    private int numRows;
    private int numCols;
    private double gameSpeed = 1.0;
    private String gridType = "rectangle";
    private Boolean isTorus = true;
    private Boolean verticesAsNeighbors = true;
    private Boolean gridOutlineOn = true;
    protected List<Cell> cells;
    protected Map<String, String> myParams;

    public Configuration(){

    }

    public Configuration(String title, int xDimension, int yDimension){

        myTitle = title;
        numRows = xDimension;
        numCols = yDimension;
        myDataValues = new HashMap<>();
    }

    public Configuration(Map<String, String> dataValues, List<Cell> cells, Map<String, String> myParams){
        this.myDataValues = dataValues;
        myTitle = extractStringSafely(DATA_FIELDS.get(0), GAME_NOT_FOUND);
        numRows = extractIntSafely(DATA_FIELDS.get(1), DIMENSION_ERROR);
        numCols = extractIntSafely(DATA_FIELDS.get(2), DIMENSION_ERROR);
        if (dataValues.get(DATA_FIELDS.get(3)) != null && !dataValues.get(DATA_FIELDS.get(3)).equals("")) {
            gameSpeed = extractDoubleSafely(DATA_FIELDS.get(3), GAME_SPEED_INVALID);
        }
        if (dataValues.get(DATA_FIELDS.get(4)) != null && !dataValues.get(DATA_FIELDS.get(4)).equals("")) {
            this.gridType = dataValues.get(DATA_FIELDS.get(4));
        }
        gridType = extractStringSafely(DATA_FIELDS.get(4),GRID_TYPE_INVALID);
        isTorus = extractBooleanSafely(DATA_FIELDS.get(5), TORUS_IS_INVALID);
        verticesAsNeighbors = extractBooleanSafely(DATA_FIELDS.get(6), VERTICES_AS_NEIGHBORS_IS_INVALID);
        gridOutlineOn = extractBooleanSafely(DATA_FIELDS.get(7), GRID_OUTLINE_IS_INVALID);
        this.cells = cells;
        this.myParams = myParams;
    }

    private Integer extractIntSafely(String key, String error) {
        try {
            return Integer.parseInt(myDataValues.get(key));
        }
        catch (Exception e){
            throw new XMLException(error);
        }
    }

    private Double extractDoubleSafely(String key, String error) {
        try {

            return Double.parseDouble(myDataValues.get(key));
        }
        catch (Exception e){
            throw new XMLException(error);
        }

    }

    private String extractStringSafely(String key, String error) {
        try {
            return myDataValues.get(key);
        }
        catch (Exception e){
            throw new XMLException(error);
        }
    }

    private boolean extractBooleanSafely(String key, String error) {
        try {
            if (myDataValues.get(key) != null && !myDataValues.get(key).equals("")) {
                if (myDataValues.get(key).equals("off")){
                    return false;
                }
            }
        }
        catch (Exception e){
            throw new XMLException(error);
        }
        return true;
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

    public String getMyTitle() {
        return myTitle;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public String getGridType() {
        return gridType;
    }

    public Map<String, String> getMyParams() {
        return myParams;
    }

    public List<Cell> getCells() {
        return cells;
    }
}
