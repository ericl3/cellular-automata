package GameSimulators;

import Grid.Grid;
import Nodes.Fish;
import Nodes.Node;
import Nodes.Shark;
import XML.Cell;
import XML.Configuration;
import XML.Enumerations.CellStates;
import XML.XMLException;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author kunal upadya
 * @author: Luke Truitt
 * Logic for the WaTorWorld simulation
 */
public class WaTorWorld extends Simulator {
    public static final Color WATER = Color.AQUA;
    public static final String FISH_XML = CellStates.WATOR_WORLD.getMyPossibleStates().get(0);
    public static final String SHARK_XML = CellStates.WATOR_WORLD.getMyPossibleStates().get(1);
    public static final String WATER_XML = CellStates.WATOR_WORLD.getMyPossibleStates().get(2);

    private Node newNode;
    private double rateOfSharks;
    private double rateOfFish;
    private int fishBreedingTurns;
    private int sharkStartingEnergy;
    private int sharkBreedingTurns;

    /**
     * Sets the initial parameters
     * @param configuration the details of the current game from
     */
    public WaTorWorld(Configuration configuration){
        super(configuration);
    }

    protected void extractParams(Map<String, String> myParams){
        try{
            rateOfFish = Double.parseDouble(myParams.get("percentFish"))*0.01;
            rateOfSharks = Double.parseDouble(myParams.get("percentShark"))*0.01;
            fishBreedingTurns = Integer.parseInt(myParams.get("turnsForFishToBreed"));
            sharkStartingEnergy = Integer.parseInt(myParams.get("fishBeforeSharksDie"));
            sharkBreedingTurns = Integer.parseInt(myParams.get("fishBeforeSharksBreed"));
        }catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    protected void initializeDefaultCells() {
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    var randSeed = Math.random();
                    if (randSeed < rateOfSharks) {
                        initializeCellFromXML(i,j,SHARK_XML);
                    } else if (rateOfSharks < randSeed && randSeed < rateOfSharks+rateOfFish) {
                        initializeCellFromXML(i,j, FISH_XML);
                    } else {
                        initializeCellFromXML(i,j, WATER_XML);
                    }
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state) {
        if (state.equals(FISH_XML)){
            Fish node = new Fish(i, j, fishBreedingTurns);
            myGrid.addNode(node);
        }
        if (state.equals(SHARK_XML)){
            Shark node = new Shark(i, j, sharkStartingEnergy, sharkBreedingTurns);
            myGrid.addNode(node);
        }
        if (state.equals(WATER_XML)){
            Node node = new Node(i, j, WATER);
            myGrid.addNode(node);
        }
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
        moveSharks(temp);
        moveFish(temp);
        fill(temp);
        return temp;
    }

    private Node simulate(Node node, Grid grid) {
        var type = node.getClass().getName();
        var temp = new Node(node.getX(), node.getY(), Color.AQUA);
        var nodes = myGrid.getSurrounding(node.getX(), node.getY(), true);
        if (type.equals("Nodes.Shark")) {
            temp = sharkMove((Shark) node, nodes, grid);
        } else if (type.equals("Nodes.Fish")) {
            temp = fishMove((Fish) node, grid);
        }
        return temp;
    }

    private Node sharkMove(Shark shark, Node[] neighbors, Grid grid) {
        var temp = new Shark(shark.getX(), shark.getY(), sharkStartingEnergy, sharkBreedingTurns);
        var fishes = checkFish(neighbors);
        if (fishes.size() <= 0) {
            shark = (Shark) moveNode(shark, grid);
        } else {
            var index = (int) Math.floor(Math.random() * fishes.size());
            var fish = neighbors[fishes.get(index)];
            removeFish(fish);
            shark.setX(fish.getX());
            shark.setY(fish.getY());
            shark.eatFish();
        }
        if (shark.checkDie()) {
            return new Node(shark.getX(), shark.getY(), Color.AQUA);
        }
        if (shark.checkAdd()) {
            newNode = temp;
        }
        shark.incrementFrames();
        return shark;
    }

    private Node fishMove(Fish fish, Grid grid) {
        var temp = new Fish(fish.getX(), fish.getY(), fishBreedingTurns);
        fish = (Fish) moveNode(fish, grid);
        if (fish.checkAdd()) {
            newNode = temp;
        }
        fish.incrementFrames();
        return fish;
    }

    private ArrayList<Integer> checkFish(Node[] neighbors) {
        ArrayList<Integer> fishes = new ArrayList<>();
        int i = 0;
        for (Node node : neighbors) {
            if (node.getClass().getName().equals("Nodes.Fish")) {
                fishes.add(i);
            }
            i++;
        }
        return fishes;
    }

    private void removeFish(Node fish) {
        myGrid.removeNode(fish.getX(), fish.getY());
        myGrid.addNode(new Node(fish.getX(), fish.getY(), Color.AQUA));
        return;
    }

    private Node moveNode(Node node, Grid grid) {
        var number = Math.random();
        var direction = node.move(number);
        Point cl = new Point(node.getX(), node.getY());
        if (direction.equals("right") ) {
            if ((!(cl.x >= this.myGrid.getNumRows() - 1) && !myGrid.getNode(cl.x + 1, cl.y).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(cl.x + 1, cl.y).getType().equals("Nodes.Shark"))
                    && ((grid.getNode(cl.x + 1, cl.y) != null
                    && !grid.getNode(cl.x + 1, cl.y).getType().equals("Nodes.Fish")
                    && !grid.getNode(cl.x + 1, cl.y).getType().equals("Nodes.Shark"))
                    || grid.getNode(cl.x + 1, cl.y) == null)) {
                node.setX(cl.x + 1);
                return node;
            } else if (((cl.x >= this.myGrid.getNumRows() - 1) && !myGrid.getNode(0, cl.y).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(0, cl.y).getType().equals("Nodes.Shark"))
                    & ((grid.getNode(0, cl.y) != null
                    && !grid.getNode(0, cl.y).getType().equals("Nodes.Fish")
                    && !grid.getNode(0, cl.y).getType().equals("Nodes.Shark"))
                    || grid.getNode(0, cl.y) == null)) {
                node.setX(0);
                return node;
            } else {
                direction = "up";
            }
        }
        if (direction.equals("up") ) {
            if (!(cl.y <= 0) && !myGrid.getNode(cl.x, cl.y - 1).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(cl.x, cl.y - 1).getType().equals("Nodes.Shark")
                    && ((grid.getNode(cl.x, cl.y - 1) != null
                    && !grid.getNode(cl.x, cl.y - 1).getType().equals("Nodes.Fish")
                    && !grid.getNode(cl.x, cl.y - 1).getType().equals("Nodes.Shark"))
                    || grid.getNode(cl.x, cl.y - 1) == null)) {
                node.setY(cl.y - 1);
                return node;
            } else if (((cl.y <= 0) && !myGrid.getNode(cl.x, this.myGrid.getNumCols() - 1).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(cl.x, this.myGrid.getNumCols() - 1).getType().equals("Nodes.Shark"))
                    && ((grid.getNode(cl.x, this.myGrid.getNumCols() - 1) != null
                    && !grid.getNode(cl.x, this.myGrid.getNumCols() - 1).getType().equals("Nodes.Fish")
                    && !grid.getNode(cl.x, this.myGrid.getNumCols() - 1).getType().equals("Nodes.Shark"))
                    || grid.getNode(cl.x, this.myGrid.getNumCols() - 1) == null)) {
                node.setY(myGrid.getNumCols() - 1);
                return node;
            } else {
                direction = "left";
            }
        }
        if (direction.equals("left") ) {
            if (!(cl.x <= 0) && !myGrid.getNode(cl.x - 1, cl.y).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(cl.x - 1, cl.y).getType().equals("Nodes.Shark")
                    && ((grid.getNode(cl.x - 1, cl.y) != null
                    && !grid.getNode(cl.x - 1, cl.y).getType().equals("Nodes.Shark") &&
                    !grid.getNode(cl.x - 1, cl.y).getType().equals("Nodes.Fish"))
                    || grid.getNode(cl.x - 1, cl.y) == null))  {
                node.setX(cl.x - 1);
                return node;
            } else if (((cl.x <= 0) && !myGrid.getNode(this.myGrid.getNumRows() - 1, cl.y).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(0, cl.y).getType().equals("Nodes.Shark"))
                    && grid.getNode(0, cl.y) != null
                    && !grid.getNode(0, cl.y).getType().equals("Nodes.Shark") &&
                    !grid.getNode(0, cl.y).getType().equals("Nodes.Fish")) {
                node.setX(this.myGrid.getNumRows() - 1);
                return node;
            } else {
                direction = "down";
            }
        }
        if (direction.equals("down") ) {
            if (!(cl.y >= this.myGrid.getNumCols() - 1) && !myGrid.getNode(cl.x, cl.y + 1).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(cl.x, cl.y).getType().equals("Nodes.Shark")
                    && (((grid.getNode(cl.x, cl.y + 1) != null &&
                    !grid.getNode(cl.x, cl.y + 1).getType().equals("Nodes.Fish") &&
                    !grid.getNode(cl.x, cl.y + 1).getType().equals("Nodes.Shark")) &&
                    grid.getNode(cl.x, cl.y + 1) == null ))) {
                node.setY(cl.y + 1);
                return node;
            } else if (((cl.y >= this.myGrid.getNumCols() + 1) && !myGrid.getNode(cl.x, 0).getType().equals("Nodes.Fish")
                    && !myGrid.getNode(0, cl.y).getType().equals("Nodes.Shark"))
                    && ((grid.getNode(cl.x, 0) != null &&
                    !grid.getNode(cl.x, 0).getType().equals("Nodes.Fish") &&
                    !grid.getNode(cl.x, 0).getType().equals("Nodes.Shark")) &&
                    grid.getNode(cl.x, 0) == null )) {
                node.setY(0);
                return node;
            }
        }
        return node;
    }


    private void moveSharks(Grid grid) {
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                var node = myGrid.getNode(i, j);
                if (node.getType().equals("Nodes.Shark")) {
                    var tempNode = simulate(node, grid);
                        grid.addNode(tempNode);
                    if (newNode != null) {
                        grid.addNode(newNode);
                        newNode = null;
                    }
                }
            }
        }
    }

    private void moveFish(Grid grid) {
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                var node = myGrid.getNode(i, j);
                if (node.getType().equals("Nodes.Fish")){
                    var tempNode = simulate(node, grid);
                    grid.addNode(tempNode);
                    if (newNode != null) {
                        grid.addNode(newNode);
                        newNode = null;
                    }
                }
            }
        }
    }

    private void fill(Grid grid) {
        for (int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                if (grid.getNode(i, j) == null) {
                    grid.addNode(new Node(i, j, Color.AQUA));
                }
            }
        }
    }
}
