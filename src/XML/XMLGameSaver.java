//this code is unfinished, but was supposed to be able to allow for saving the game state
//package XML;
//
//import java.io.File;
//import java.util.List;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import GameSimulators.*;
//import Grid.Grid;
//import Nodes.Node;
//import org.w3c.dom.Attr;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import Grid.TriangleGrid;
//import Grid.RectangleGrid;
//import Grid.HexagonGrid;
//
//import static XML.Configuration.DATA_FIELDS;

///**
// * @author kunalupadya
// */
//public class XMLGameSaver {
//
//    public static final String TITLE = DATA_FIELDS.get(0);
//    public static final String NUMROWS = DATA_FIELDS.get(1);
//    public static final String NUMCOLS = DATA_FIELDS.get(2);
//    public static final String GAMESPEED = DATA_FIELDS.get(3);
//    public static final String GRIDTYPE = DATA_FIELDS.get(4);
//    public static final String ISTORUS = DATA_FIELDS.get(5);
//    public static final String VERTICESASNEIGHBORS = DATA_FIELDS.get(6);
//    public static final String GRIDOUTLINE = DATA_FIELDS.get(7);
//    public static final String CELLS = DATA_FIELDS.get(8);
//    public static final String PARAMS = DATA_FIELDS.get(9);
//
//
//    public static final String xmlFilePath = "C:\\Users\\nikos7\\Desktop\\files\\xmlfile.xml";
//    private DocumentBuilder documentBuilder;
//    private Document document;
//
//    public XMLGameSaver(Simulator mySim, double inputGameSpeed){
//        try {
//            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
//
//            documentBuilder = documentFactory.newDocumentBuilder();
//            document = documentBuilder.newDocument();
//
//            Element root = document.createElement("data");
//
//            Attr attr = document.createAttribute("media");
//            attr.setValue("Game");
//            root.setAttributeNode(attr);
//
//            Grid myGrid = mySim.getGrid();
//
//            createElementAndAddToParentWithText(root, NUMROWS, String.valueOf(myGrid.getNumRows()));
//            createElementAndAddToParentWithText(root, NUMCOLS, String.valueOf(myGrid.getNumCols()));
//            createElementAndAddToParentWithText(root, GAMESPEED, String.valueOf(inputGameSpeed));
//            createElementAndAddToParentWithText(root, ISTORUS, convertBooleanToXML(mySim.getTorus()));
//            createElementAndAddToParentWithText(root, VERTICESASNEIGHBORS, convertBooleanToXML(mySim.getVerticesAsNeighbors()));
//            createElementAndAddToParentWithText(root, GRIDOUTLINE, convertBooleanToXML(mySim.getGridOutlineOn()));
//
//            Element gridType = document.createElement(GRIDTYPE);
//            root.appendChild(gridType);
//
//            if (myGrid instanceof TriangleGrid) {
//                gridType.appendChild(document.createTextNode("triangle"));
//            }
//            if (myGrid instanceof RectangleGrid) {
//                gridType.appendChild(document.createTextNode("rectangle"));
//            }
//            if (myGrid instanceof HexagonGrid) {
//                gridType.appendChild(document.createTextNode("hexagon"));
//            }
//
//
//            Element title = document.createElement(TITLE);
//            Element cells = document.createElement(CELLS);
//            List<Node> allNodes = myGrid.getNodes();
//            Element params = document.createElement(PARAMS);
//            if (mySim instanceof GameOfLife) {
//                title.appendChild(document.createTextNode("Spreading Fire"));
//                for (Node node: allNodes){
//                    Element cell = document.createElement("cell");
//                    createElementAndAddToParentWithText(cell,"row", String.valueOf(node.getX()));
//                    createElementAndAddToParentWithText(cell,"col", String.valueOf(node.getY()));
//                    Element state = document.createElement("state");
//                    node.getColor().toString();
////                    cells.appendChild();
//                }
//            }
//            if (mySim instanceof WaTorWorld) {
//                title.appendChild(document.createTextNode("Wa-Tor World"));
//            }
//            if (mySim instanceof Percolation) {
//                title.appendChild(document.createTextNode("Percolation"));
//            }
//            if (mySim instanceof Segregation) {
//                title.appendChild(document.createTextNode("Segregation"));
//            }
//            if (mySim instanceof GameOfLife) {
//                title.appendChild(document.createTextNode("Game of Life"));
//            }
//            if (mySim instanceof RockPaperScissors) {
//                title.appendChild(document.createTextNode("Rock Paper Scissors"));
//            }
//            if (mySim instanceof SelfReplication) {
//                title.appendChild(document.createTextNode("Self Replication"));
//            }
//        }
//         catch (ParserConfigurationException pce) {
//            pce.printStackTrace();
//        }
//    }
//
//    private void createElementAndAddToParentWithText(Element parent,String childName, String childText) {
//        Element gameSpeed = document.createElement(childName);
//        gameSpeed.appendChild(document.createTextNode(childText));
//        parent.appendChild(gameSpeed);
//    }
//
//    private String convertBooleanToXML(Boolean bool){
//        if (bool){
//            return "off";
//        }
//        else {
//            return "on";
//        }
//    }
//}
