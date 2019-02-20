import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.*;

import Grid.Grid;
import Nodes.Node;
import GameSimulators.*;
import javafx.scene.paint.Color;

/**
 * CountsChart class
 *
 * Handles the animation for the population chart over time
 */
public class CountsChart extends Animation {

    public static final double MAX_HEIGHT = 200;

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart popChart;
    private XYChart.Series fishes = new XYChart.Series();
    private XYChart.Series sharks = new XYChart.Series();
    private XYChart.Series trees = new XYChart.Series();
    private XYChart.Series burning = new XYChart.Series();
    private XYChart.Series dead = new XYChart.Series();
    private XYChart.Series open = new XYChart.Series();
    private XYChart.Series water = new XYChart.Series();
    private XYChart.Series blocked = new XYChart.Series();
    private XYChart.Series empty = new XYChart.Series();
    private XYChart.Series bluePeople = new XYChart.Series();
    private XYChart.Series redPeople = new XYChart.Series();
    private XYChart.Series alive = new XYChart.Series();
    private XYChart.Series deadCell = new XYChart.Series();
    private XYChart.Series white = new XYChart.Series();
    private XYChart.Series red = new XYChart.Series();
    private XYChart.Series blue = new XYChart.Series();
    private XYChart.Series green = new XYChart.Series();
    private XYChart.Series purple = new XYChart.Series();
    private XYChart.Series black = new XYChart.Series();
    private Map<Color, Integer> colorToCount;

    /**
     * Constructs the chart for each simulation
     *
     * @param grid  Grid of the simulation
     * @param xPos  X position of the chart
     * @param yPos  Y position of the chart
     */
    public CountsChart(Grid grid, double xPos, double yPos) {
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        popChart = new LineChart(xAxis, yAxis);
        xAxis.setLabel("Time");
        yAxis.setLabel("Cell Counts");
        popChart.setCreateSymbols(false);
        popChart.setLayoutX(xPos);
        popChart.setLayoutY(yPos);
        popChart.setMaxWidth(SCREEN_WIDTH);
        popChart.setMaxHeight(MAX_HEIGHT);
    }

    private void addToSeriesAndChart(XYChart.Series series, double timeStep, double popVal){
        series.getData().add(new XYChart.Data(timeStep, popVal));
    }

    private void handleChange(List<Node> nodes) {
        colorToCount = new HashMap<>();
        for (Node node : nodes) {
            if (!colorToCount.containsKey(node.getColor())) {
                colorToCount.put(node.getColor(), 1);
            } else {
                int count = colorToCount.get(node.getColor());
                count+=1;
                colorToCount.put(node.getColor(), count);
            }
        }
    }

    /**
     * Initialize the chart with legend based on the type of simulation
     *
     * @param simulationType    Current simulation
     */
    public void initializeSeries(Simulator simulationType) {
        if (simulationType instanceof WaTorWorld) {
            fishes.setName("Fishes");
            sharks.setName("Sharks");
            popChart.getData().addAll(fishes, sharks);
        } else if (simulationType instanceof SpreadingFire) {
            trees.setName("Trees");
            burning.setName("Burning");
            dead.setName("Dead");
            popChart.getData().addAll(trees, burning, dead);
        } else if (simulationType instanceof Percolation) {
            blocked.setName("Blocked");
            water.setName("Filled");
            open.setName("Open");
            popChart.getData().addAll(blocked, water, open);
        } else if (simulationType instanceof Segregation) {
            empty.setName("Empty");
            bluePeople.setName("Blue People");
            redPeople.setName("Red People");
            popChart.getData().addAll(empty, bluePeople, redPeople);
        } else if (simulationType instanceof GameOfLife) {
            alive.setName("Alive");
            deadCell.setName("Dead");
            popChart.getData().addAll(alive, deadCell);
        } else if (simulationType instanceof RockPaperScissors) {
            white.setName("White");
            red.setName("Red");
            blue.setName("Blue");
            green.setName("Green");
            popChart.getData().addAll(white, red, blue, green);
        } else if (simulationType instanceof SelfReplication) {
            red.setName("Red");
            purple.setName("Purple");
            green.setName("Green");
            black.setName("Black");
            white.setName("White");
            blue.setName("Blue");
            popChart.getData().addAll(white, red, blue, green, purple, black);
        }
    }

    /**
     * Update the chart with new population values
     * on each time step
     *
     * @param nodes             List of nodes on the grid
     * @param simulationType    Type of simulation being played
     * @param timeStep          Current time step of the animation
     */
    public void changeChart(List<Node> nodes, Simulator simulationType, double timeStep) {
        if (simulationType instanceof WaTorWorld) {
            handleChange(nodes);
            addToSeriesAndChart(fishes, timeStep, colorToCount.get(Color.GREENYELLOW));
            if (colorToCount.containsKey(Color.PURPLE)) {
                addToSeriesAndChart(sharks, timeStep, colorToCount.get(Color.PURPLE));
            }
        } else if (simulationType instanceof SpreadingFire) {
            handleChange(nodes);
            if (colorToCount.containsKey(Color.RED)) {
                addToSeriesAndChart(burning, timeStep, colorToCount.get(Color.RED));
            }
            addToSeriesAndChart(dead, timeStep, colorToCount.get(Color.YELLOW));
            addToSeriesAndChart(trees, timeStep, colorToCount.get(Color.GREEN));
        } else if (simulationType instanceof Percolation) {
            handleChange(nodes);
            addToSeriesAndChart(blocked, timeStep, colorToCount.get(Color.BLACK));
            addToSeriesAndChart(water, timeStep, colorToCount.get(Color.CYAN));
            if (colorToCount.containsKey(Color.WHITE)) {
                addToSeriesAndChart(open, timeStep, colorToCount.get(Color.WHITE));
            }
        } else if (simulationType instanceof Segregation) {
            handleChange(nodes);
            addToSeriesAndChart(empty, timeStep, colorToCount.get(Color.WHITE));
            addToSeriesAndChart(bluePeople, timeStep, colorToCount.get(Color.BLUE));
            addToSeriesAndChart(redPeople, timeStep, colorToCount.get(Color.RED));
        } else if (simulationType instanceof GameOfLife) {
            handleChange(nodes);
            if (colorToCount.containsKey(Color.BLACK)) {
                addToSeriesAndChart(alive, timeStep, colorToCount.get(Color.BLACK));
            }
            addToSeriesAndChart(deadCell, timeStep, colorToCount.get(Color.WHITE));
        } else if (simulationType instanceof RockPaperScissors) {
            handleChange(nodes);
            if (colorToCount.containsKey(Color.WHITE)) {
                addToSeriesAndChart(white, timeStep, colorToCount.get(Color.WHITE));
            }
            addToSeriesAndChart(red, timeStep, colorToCount.get(Color.RED));
            addToSeriesAndChart(blue, timeStep, colorToCount.get(Color.BLUE));
            addToSeriesAndChart(green, timeStep, colorToCount.get(Color.GREEN));
        } else if (simulationType instanceof SelfReplication) {
            handleChange(nodes);
            addToSeriesAndChart(red, timeStep, colorToCount.get(Color.RED));
            addToSeriesAndChart(blue, timeStep, colorToCount.get(Color.BLUE));
            addToSeriesAndChart(black, timeStep, colorToCount.get(Color.BLACK));
            if (colorToCount.containsKey(Color.WHITE)) {
                addToSeriesAndChart(white, timeStep, colorToCount.get(Color.WHITE));
            }
            if (colorToCount.containsKey(Color.GREEN)) {
                addToSeriesAndChart(green, timeStep, colorToCount.get(Color.GREEN));
            }
            if (colorToCount.containsKey(Color.PURPLE)) {
                addToSeriesAndChart(purple, timeStep, colorToCount.get(Color.PURPLE));
            }
        }
    }

    /**
     * Obtain the graph
     *
     * @return      LineChart object of graph
     */
    public LineChart getLineChart() {
        return this.popChart;
    }
}
