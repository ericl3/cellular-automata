package Nodes;

import javafx.scene.paint.*;

/**
 * @author Eric Lin
 * @Author : Luke Truitt
 * Extension of node for the shark node in WaTor World
 */
public class Shark extends Node{
    private int sharkReproduction = 3;
    public static int FISH_ENERGY = 1;
    private int energy = 3;
    private int elapsedFrames = 0;

    /**
     * Initializes the shark
     * @param x column
     * @param y row
     * @param energy how many turns without food it has before it dies
     * @param reproduction how long before it reproduces
     */
    public Shark(int x, int y, int energy, int reproduction) {
        super(x, y, Color.PURPLE);
        this.energy = energy;
    }

    public void eatFish() {
        this.energy += FISH_ENERGY;
    }

    public void incrementFrames() {
        this.energy--; this.elapsedFrames++;
    }

    /**
     * @return Checks if shark should reproduce
     */
    public boolean checkAdd() {
        if(this.elapsedFrames >= sharkReproduction) {
            elapsedFrames = 0;
            return true;
        }
        return false;
    }

    /**
     * @return if the shark should die
     */
    public boolean checkDie() {
        return this.energy<=0;
    }

}
