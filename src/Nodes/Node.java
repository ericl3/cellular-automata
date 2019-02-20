package Nodes;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Polygon;

/**
 * @author Eric Lin
 * @Author: Luke Truitt
 * Generic Node class that sets up the initial properties for most simulations
 */
public class Node {
    public static double PROB_RIGHT = .25;
    public static double PROB_LEFT = .25;
    public static double PROB_UP = .25;
    public static double PROB_DOWN = .25;
    private int x;
    private int y;
    protected Color color;
    protected Shape shape;
    private Polygon polygon;
    private int direction;

    public Node() {}

    public Node(int x, int y, Color color) {
        this.x = x; this.y = y; this.color = color;
    }

    public Node(Color color) {
        this.color = color;
    }

    /**
     * set the color of the Node whenever in case it needs to be changed
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
        this.polygon.setFill(color);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setNode(Node n) {
        this.x = n.x;
        this.y = n.y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public Color getColor() {
        return color;
    }

    public void setPolygon(Polygon polygon) {
        polygon.setFill(this.color);
        this.polygon = polygon;
    }

    public Polygon getPolygon() { return this.polygon; }

    /**
     * Move that lets the Node decide where to move.
     * @param direction a random number
     * @return a string of which direction it needs to go.
     */
    public String move(double direction) {
        var cumProb = PROB_RIGHT;
        if(cumProb > direction) {
            return "right";
        }
        cumProb += PROB_DOWN;
        if (cumProb > direction) {
            return "down";
        }
        cumProb += PROB_LEFT;
        if (cumProb > direction) {
            return "left";
        }
        return "up";
    }

    public String getType() {
        return this.getClass().getName();
    }

    public boolean isShark() {
        return this.getType().equals("Nodes.Shark");
    }

    public boolean isFish() {
        return this.getType().equals("Nodes.Fish");
    }

    public int getDirection() {return this.direction;}

    public void setDirection(int direction) {this.direction = direction;}
}
