package GameSimulators;

import Grid.Grid;
import Nodes.Node;
import XML.Cell;
import XML.Configuration;
import XML.XMLException;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @Author : Luke Truitt
 * Logic for the Self Replication simulator.
 * Uses the rules to change the grid one set at a time.
 */
public class SelfReplication extends Simulator{
    private static final int UP = 0;
    private static final int RIGHT = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;

    private int cycle;
    private int freqRight;
    private int freqLeft;
    private int start_x;
    private int start_y;
    private int start_direction;

    /**
     * @Author; Luke Truitt
     * @param configuration passes in the parameters set by the XML parsing
     */
    public SelfReplication (Configuration configuration){
        super(configuration);
    }

    protected void initializeDefaultCells() {
        for (int i = 0; i < myGrid.getNumRows(); i++) {
            for (int j = 0; j < myGrid.getNumCols(); j++) {
                if (myGrid.getNode(i, j) == null) {
                    myGrid.addNode(new Node(i, j, Color.BLACK));
                }
            }
        }
    }

    protected void initializeCellFromXML(int i, int j, String state) {
        switch (state){
            case("wall"):{
                myGrid.setNode(i,j, new Node(i, j, Color.BLUE));
                break;
            }
            case("channel"):{
                myGrid.setNode(i,j, new Node(i, j, Color.RED));
                break;
            }
            case("straight"):{
                myGrid.setNode(i,j, new Node(i, j, Color.WHITE));
                break;
            }
            case("turn"):{
                myGrid.setNode(i, j, new Node(i, j, Color.PURPLE));
            }
        }
    }

    protected void extractParams(Map<String, String> myParams){
        try{
            freqLeft = Integer.parseInt(myParams.get("freqLeft"));
            freqRight = Integer.parseInt(myParams.get("freqRight"));
            start_x = Integer.parseInt(myParams.get("start_x"));
            start_y = Integer.parseInt(myParams.get("start_y"));
            start_direction = Integer.parseInt(myParams.get("start_direction"));
        }
        catch (Exception e){
            throw new XMLException(paramsError);
        }
    }

    @Override
    public Grid getGrid() { return this.myGrid; }

    private Node generateNext(int i, int j) {
        var choices = new ArrayList<Node>();
        var white = 20 - freqLeft - freqRight;
        for (int m = 0; m < freqRight; m++) {
            choices.add(new Node(i, j, Color.GREEN));
        }
        for (int m = 0; m < freqLeft; m++) {
            choices.add(new Node(i, j, Color.PURPLE));
        }
        if(white > 0) {
            for (int m = 0; m < white; m++) {
                choices.add(new Node(i, j, Color.WHITE));
            }
        }
        Collections.shuffle(choices);
        return choices.get(0);
    }

    private Grid handleRep(Grid grid) {
        for(int i = 0; i < grid.getNumRows(); i++) {
            for (int j = 0; j < grid.getNumCols(); j++) {
                var node = (Node) myGrid.getNode(i, j);
                if (node != null) {
                    var up = myGrid.getUp(i, j, true);
                    var left = myGrid.getLeft(i, j, true);
                    var right = myGrid.getRight(i, j, true);
                    var down = myGrid.getDown(i, j, true);
                    if (node.getColor() == Color.WHITE) {
                        if (node.getDirection() == UP) {
                            if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                grid = breakStraightHorz(node, up, left, right, grid);
                            }
                        } else if (node.getDirection() == DOWN) {
                            if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                grid = breakStraightHorz(node, down, right, left, grid);
                            }
                        } else if (node.getDirection() == LEFT) {
                            if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else {
                                grid = breakStraightVert(node, left, down, up, grid);
                            }
                        } else if (node.getDirection() == RIGHT) {
                            if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else {
                                grid = breakStraightVert(node, right, up, down, grid);
                                }
                        }
                    } else if (node.getColor() == Color.PURPLE) {
                        if (node.getDirection() == UP) {
                            if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                node.setDirection(LEFT);
                                grid = turnHorz(node, up, left, grid);}
                        } else if (node.getDirection() == DOWN) {
                            if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                node.setDirection(RIGHT);
                                grid = turnHorz(node, down, right, grid);
                            }
                        } else if (node.getDirection() == LEFT) {
                            if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else {
                                node.setDirection(UP);
                                grid = turnVert(node, left, down, grid);
                            }
                        } else if (node.getDirection() == RIGHT) {
                            if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else {
                                node.setDirection(DOWN);
                                grid = turnVert(node, right, up, grid);
                            }
                        }
                    } else if (node.getColor() == Color.GREEN) {
                        if (node.getDirection() == UP) {
                            if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                node.setDirection(RIGHT);
                                grid = turnHorz(node, up, right, grid);
                            }
                        } else if (node.getDirection() == DOWN) {
                            if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else {
                                node.setDirection(LEFT);
                                grid = turnHorz(node, down, left, grid);
                            }
                        } else if (node.getDirection() == LEFT) {
                            if (left != null && left.getColor() == Color.RED) {
                                node.setDirection(LEFT);
                                grid = moveStraight(node, left, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else {
                                node.setDirection(DOWN);
                                grid = turnVert(node, left, up, grid);
                            }
                        } else if (node.getDirection() == RIGHT) {
                            if (right != null && right.getColor() == Color.RED) {
                                node.setDirection(RIGHT);
                                grid = moveStraight(node, right, grid);
                            } else if (down != null && down.getColor() == Color.RED) {
                                node.setDirection(DOWN);
                                grid = moveStraight(node, down, grid);
                            } else if (up != null && up.getColor() == Color.RED) {
                                node.setDirection(UP);
                                grid = moveStraight(node, up, grid);
                            } else {
                                node.setDirection(UP);
                                grid = turnVert(node, right, down, grid);
                            }
                        }
                    }else {
                            if (grid.getNode(i, j) == null) {
                                grid.addNode(node);
                            }
                        }
                    }
                }
            }

        if (cycle % 3 == 0) {
            var temp = generateNext(start_x, start_y);
            temp.setDirection(start_direction);
            grid.addNode(temp);
        }
        cycle++;
        return grid;
    }

    private Grid moveStraight(Node current, Node direction, Grid grid) {
        grid.addNode(new Node(current.getX(), current.getY(), Color.RED));
        current.setX(direction.getX());
        current.setY(direction.getY());
        grid.addNode(current);
        return grid;
    }
    private Grid turnHorz(Node current, Node straight, Node direction, Grid grid) {
        grid.addNode(new Node(current.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(current.getX(), direction.getY(), Color.RED));
        grid.addNode(new Node(straight.getX(), direction.getY(), Color.BLUE));
        if (current.getDirection() == RIGHT) {
            grid.addNode(new Node(current.getX(), myGrid.getRight(straight.getX(), direction.getY(), true).getY(), Color.BLUE));
        } else {
            grid.addNode(new Node(current.getX(), myGrid.getLeft(straight.getX(), direction.getY(), true).getY(), Color.BLUE));
        }
        return grid;
    }

    private Grid turnVert(Node current, Node straight, Node direction, Grid grid) {
        grid.addNode(new Node(current.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(direction.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(direction.getX(), straight.getY(), Color.BLUE));
        if (current.getDirection() == UP) {
            grid.addNode(new Node(myGrid.getDown(direction.getX(), direction.getY(), true).getX(), current.getY(), Color.BLUE));
        } else {
            grid.addNode(new Node(myGrid.getUp(direction.getX(), direction.getY(), true).getX(), current.getY(), Color.BLUE));
        }
        return grid;
    }

    private Grid breakStraightHorz(Node current, Node straight, Node left, Node right, Grid grid) {
        grid.addNode(new Node(current.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(straight.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(straight.getX(), left.getY(), Color.BLUE));
        grid.addNode(new Node(straight.getX(), right.getY(), Color.BLUE));
        if (current.getDirection() == UP) {
            grid.addNode(new Node(myGrid.getUp(straight.getX(), straight.getY(), true).getX(), current.getY(), Color.BLUE));
        } else {
            grid.addNode(new Node(myGrid.getDown(straight.getX(), straight.getY(), true).getX(), current.getY(), Color.BLUE));
        }
        return grid;
    }

    private Grid breakStraightVert(Node current, Node straight, Node left, Node right, Grid grid) {
        grid.addNode(new Node(current.getX(), current.getY(), Color.RED));
        grid.addNode(new Node(current.getX(), straight.getY(), Color.RED));
        grid.addNode(new Node(left.getX(), straight.getY(), Color.BLUE));
        grid.addNode(new Node(right.getX(), straight.getY(), Color.BLUE));
        if (current.getDirection() == RIGHT) {
            grid.addNode(new Node(current.getX(), myGrid.getRight(straight.getX(), straight.getY(), true).getY(), Color.BLUE));
        } else {
            grid.addNode(new Node(current.getX(), myGrid.getLeft(straight.getX(), straight.getY(), true).getY(), Color.BLUE));
        }
        return grid;
    }

    /**
     * Runs the simulation
     * @param grid current grid
     * @return updated grid
     */
    @Override
    public Grid go(Grid grid) {
        myGrid = grid;
        var temp = setGrid(myGridType, grid.getNumRows(), grid.getNumCols(), isTorus,verticesAsNeighbors,gridOutlineOn);
        temp = handleRep(temp);
        return temp;
    }
}
