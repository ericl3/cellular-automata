package XML.Enumerations;

import java.util.List;

/**
 * @author kunalupadya
 */
public enum CellStates {
    SPREADING_FIRE(List.of("empty","tree","burning")){
        @Override
        public String defaultStateIs() {
            return "tree";
        }
    },
    GAME_OF_LIFE(List.of("alive","dead")){
        @Override
        public String defaultStateIs() {
            return "dead";
        }
    },
    SELF_REPRODUCTION(List.of("channel","wall", "straight", "turn")){
        @Override
        public String defaultStateIs() {
            return "wall";
        }
    },
    PERCOLATION(List.of("blocked", "open")){
        @Override
        public String defaultStateIs() {
            return "blocked";
        }
    },
    WATOR_WORLD(List.of("fish","shark","water")){
        @Override
        public String defaultStateIs() {
            return "water";
        }
    },
    SEGREGATION(List.of("red","blue", "empty")){
        @Override
        public String defaultStateIs() {
            return "empty";
        }
    },
    ROCKPAPERSCISSORS(List.of("red","blue", "green", "empty")){
        @Override
        public String defaultStateIs() { return "empty"; }
    };

    private final List<String> myPossibleStates;

    CellStates(List<String> possibleStates) {
        this.myPossibleStates = possibleStates;
    }

    public List<String> getMyPossibleStates() {
        return myPossibleStates;
    }

    public abstract String defaultStateIs();
}
