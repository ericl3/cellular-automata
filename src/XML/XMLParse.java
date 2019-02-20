package XML;

import XML.Enumerations.CellOptions;
import XML.Enumerations.CellStates;
import XML.Enumerations.GameParams;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author kunalupadya
 */
public class XMLParse {
    public static final String CELL_ERROR = "Cells are not properly input";
    public static final String CELL_STATES_ERROR = "Cell states are invalid";
    public static final String IMPROPER_TYPE_MESSAGE = "XML does not represent %s";
    public static final String BAD_GAME_ERROR = "XML is not a valid game";
    public static final String PARAMS_MISSING = "Params for this game are missing";
    public static final String CELL_OUT_OF_BOUNDS = "Cell out of bounds";
    public static final String CELL_IDENTIFIER = "cell";
    public static final String ROW = CellOptions.ROW.getKey();
    public static final String COL = CellOptions.COL.getKey();
    public static final String STATE = CellOptions.STATE.getKey();

    private final DocumentBuilder DOCUMENT_BUILDER;
    private final String TYPE_ATTRIBUTE;

    public XMLParse(String type){
        DOCUMENT_BUILDER = getDocumentBuilder();
        TYPE_ATTRIBUTE = type;
    }

    public Configuration getGame(File dataFile) throws XMLException {
        var root = getRootElement(dataFile);
        if (! isValidFile(root, Configuration.DATA_TYPE)) {
            throw new XMLException(IMPROPER_TYPE_MESSAGE, Configuration.DATA_TYPE);
        }
        // read data associated with the fields given by the object
        var results = new HashMap<String, String>();
        for (var field : Configuration.DATA_FIELDS) {
            results.put(field, getTextValue(root, field));
        }
        List<Cell> cells;
        cells = parseCells(root);
        String title = results.get("title");
        Map<String, String> myParams;
        Configuration myConfiguration;
        switch (title){
            case ("Spreading Fire"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.SPREADING_FIRE, CellStates.SPREADING_FIRE);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Wa-Tor World"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.WATOR_WORLD, CellStates.WATOR_WORLD);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Percolation"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.PERCOLATION, CellStates.PERCOLATION);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Segregation"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.SEGREGATION, CellStates.SEGREGATION);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Game of Life"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.GAME_OF_LIFE, CellStates.GAME_OF_LIFE);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Rock Paper Scissors"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.ROCKPAPERSCISSORS, CellStates.ROCKPAPERSCISSORS);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            case ("Self Replication"):{
                myParams = checkCellsAndParseParams(root, cells, GameParams.SELF_REPLICATION, CellStates.SELF_REPRODUCTION);
                myConfiguration = new Configuration(results,cells,myParams);
                break;
            }
            default:{
                throw new XMLException(BAD_GAME_ERROR);
            }
        }
        checkIfCellPositionsValid(myConfiguration);
        return new Configuration(results,cells,myParams);
    }

    private void checkIfCellPositionsValid(Configuration myConfiguration) {
        for (Cell cell: myConfiguration.getCells()){
            if (cell.getRow()>myConfiguration.getNumRows()){
                throw new XMLException(CELL_OUT_OF_BOUNDS);
            }
            if (cell.getColumn()>myConfiguration.getNumCols()){
                throw new XMLException(CELL_OUT_OF_BOUNDS);
            }
        }
    }

    private Map<String, String> checkCellsAndParseParams(Element root, List<Cell> cells, GameParams gameParams,CellStates possibleCellStates){
        throwExceptionIfCellStatesNotValid(cells, possibleCellStates);
        Map<String, String> myParams = getUniqueParams(root, gameParams);
        return myParams;
    }

    private void throwExceptionIfCellStatesNotValid(List<Cell> cells, CellStates possibleCellStates){
        if (!cellStatesValid(cells, possibleCellStates)){
            throw new XMLException(CELL_STATES_ERROR);
        }
    }

    private boolean cellStatesValid(List<Cell> cells, CellStates possibleCellStates){
        List<String> possibleStates = possibleCellStates.getMyPossibleStates();
        for (Cell cell: cells) {
            if (!possibleStates.contains(cell.getCellState())) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> getUniqueParams(Element root, GameParams game){
        Element params = (Element) root.getElementsByTagName("params").item(0);
        Map<String, String> inputtedParams = new HashMap<>();
        try {
            for (var field : game.getMyParams()) {
                inputtedParams.put(field, params.getElementsByTagName(field).item(0).getTextContent());
            }
        }
        catch (Exception e){
            throw new XMLException(PARAMS_MISSING);
        }
        return inputtedParams;
    }

    private Element getRootElement (File xmlFile) {
        try {
            DOCUMENT_BUILDER.reset();
            var xmlDocument = DOCUMENT_BUILDER.parse(xmlFile);
            return xmlDocument.getDocumentElement();
        }
        catch (SAXException | IOException e) {
            throw new XMLException(e);
        }
    }

    private String getAttribute (Element e, String attributeName) {
        return e.getAttribute(attributeName);
    }

    private List<Cell> parseCells (Element root) {
        NodeList cellsXML = root.getElementsByTagName(CELL_IDENTIFIER);
        List<Cell> cells = new ArrayList<>();
        iterateThroughCells(cellsXML, cells);
        return cells;
    }

    private Cell createCell(Element cell) {
        int row;
        int col;
        String state;
        try {
            row = Integer.parseInt(cell.getElementsByTagName(ROW).item(0).getTextContent());
            col = Integer.parseInt(cell.getElementsByTagName(COL).item(0).getTextContent());
            state = cell.getElementsByTagName(STATE).item(0).getTextContent();
        } catch (Exception e) {
            throw new XMLException(CELL_ERROR);
        }
        return new Cell(row, col, state);
    }

    private void iterateThroughCells(NodeList cellsXML, List<Cell> cells) {
        for (int k = 0; k<cellsXML.getLength();k++){
            Element cell = (Element) cellsXML.item(k);
            Cell newCell = createCell(cell);
            cells.add(newCell);
        }
    }

    private String getTextValue (Element e, String tagName) {
        var nodeList = e.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        else {
            return "";
        }
    }


    private boolean isValidFile (Element root, String type) {
        return getAttribute(root, TYPE_ATTRIBUTE).equals(type);
    }

    private DocumentBuilder getDocumentBuilder () {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }
}