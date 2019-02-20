package Nodes;

import javafx.scene.paint.*;

/**
 * @Author Eric Lin
 * @Author: Luke Truitt
 * The fish sub-class of Node that holds the information specific to Fish in WaTorWorld
 */
public class Fish extends Node{
    private int fishReproduction = 3;
    private int elapsedFrames;

    /**
     * Sets up the fish
     * @param x column
     * @param y row
     * @param reproduction iterations before fish reproduces
     */
    public Fish(int x, int y, int reproduction) {
        super(x, y, Color.GREENYELLOW);
        this.elapsedFrames = 0;
        this.fishReproduction = reproduction;
    }

    public void incrementFrames() {
        this.elapsedFrames++;
    }

    /**
     * @return if a fish is ready to reproduce
     */
    public boolean checkAdd() {
        if(this.elapsedFrames >= fishReproduction) {

            this.elapsedFrames = 0;
            return true;
        }
        return false;
    }
}
