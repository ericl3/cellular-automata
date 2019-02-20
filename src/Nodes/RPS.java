package Nodes;

import XML.Cell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @Author : Luke Truitt
 * Specific node for the RPS game
 */
public class RPS extends Node {
    private int shade;

    public RPS(int x, int y, Color color, int shade) {
        super(x, y, color);
        this.shade = shade;
    }


    public int getShade() { return shade; }

    public void decrementShade() {
        if(this.shade>0) {
            this.shade--;
        }
        this.color = setOpacity();
    }

    public void incrementShade() {
            if(this.shade < 9) {
                this.shade++;
            }
            this.color = setOpacity();
    }

    private Color setOpacity() {
        var red = this.color.getRed(); var green = this.color.getGreen(); var blue = this.color.getBlue();
        return new Color(red, green, blue, 1 - this.shade * .1);
    }

    /**
     * Check to see whether or not it is equal a passed in node.
     * @param replace node to check
     * @return true if they are equal, false otherwise.
     */
    public boolean colorsEqual(Node replace) {
        return (this.color.getRed() == replace.getColor().getRed()
                && (this.color.getGreen() == replace.getColor().getGreen())
                && (this.color.getBlue() == replace.getColor().getBlue()));
    }

    /**
     * Check to see whether or not it beats the passed in node
     * @param replace node to check
     * @return true if current node beats the passed in node
     */
    public boolean currentWin(Node replace) {
        return ((this.color.getRed() > replace.getColor().getRed() && this.color.getBlue() < replace.getColor().getBlue())
                || (this.color.getGreen() > replace.getColor().getGreen() && this.color.getRed() < replace.getColor().getRed())
                || (this.color.getBlue() > replace.getColor().getBlue() && this.color.getGreen() < replace.getColor().getGreen()));
    }

    /**
     * Check to see whether or not it loses to a passed in node
     * @param replace node to check
     * @return true if the passed in node should win
     */
    public boolean replaceWin(Node replace) {
        return ((this.color.getRed() < replace.getColor().getRed() && this.color.getBlue() > replace.getColor().getBlue())
                || (this.color.getGreen() < replace.getColor().getGreen() && this.color.getRed() > replace.getColor().getRed())
                || (this.color.getBlue() < replace.getColor().getBlue() && this.color.getGreen() > replace.getColor().getGreen()));

    }

    public boolean isWhite() {
        return this.color.getBlue() == 1.0 && this.color.getGreen() == 1.0  && this.color.getRed() == 1.0;
    }
}
