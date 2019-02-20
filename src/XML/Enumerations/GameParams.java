package XML.Enumerations;

import java.util.List;

/**
 * @author kunalupadya
 */
public enum GameParams {
    SPREADING_FIRE(List.of("probabilityOfCatchingFire")),
    GAME_OF_LIFE(List.of("percentAlive")),
    PERCOLATION(List.of("percentOpen")),
    WATOR_WORLD(List.of("turnsForFishToBreed", "fishBeforeSharksDie","fishBeforeSharksBreed","percentFish","percentShark")),
    SEGREGATION(List.of("percentSimilarSatisfaction", "probRed", "percentEmpty")),
    ROCKPAPERSCISSORS(List.of("probRed", "probBlue","percentEmpty")),
    SELF_REPLICATION(List.of("freqRight", "freqLeft", "start_x", "start_y", "start_direction"));
    private final List<String> myParams;

    private GameParams(List<String> myParams) {
        this.myParams = myParams;
    }

    
    public List<String> getMyParams() {
        return myParams;
    }
}