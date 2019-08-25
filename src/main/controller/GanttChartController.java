package main.controller;

import graph.GraphController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import main.Main;
import org.graphstream.graph.Node;
import solutionfinder.data.Processor;
import visualization.GanttChartFX;

import java.util.*;

public class GanttChartController {

    private static GanttChartController ourInstance = new GanttChartController();

    private static List<String> colours = new ArrayList<>(
            Arrays.asList("status-red", "status-blue", "status-green", "status-orange", "status-darkGreen"));
    private static String currentColour = null;
    private static String previousColour = null;

    final static NumberAxis xAxis = new NumberAxis();
    final static CategoryAxis yAxis = new CategoryAxis();

    final static GanttChartFX<Number,String> ganttChart = new GanttChartFX<>(xAxis, yAxis);

    private static HashMap<String, Double> textX = new HashMap<>();
    private static HashMap<String, Double> textY = new HashMap<>();

    private GanttChartController() {

    }

    /**
     * Initialises the chart that needs to be displayed. The values for the blocks are taken
     * from the final solution that is found by the solution finder. The colours for the boxes are also
     * randomised.
     */
    public static void initialiseChart() {
        System.out.println("Gantt chart controller initialized!");
        Processor[] processors = Main.getController().getOptimalSolution().getProcessors();
        ObservableList<String> processorTitle = FXCollections.observableArrayList();
        List<XYChart.Series> seriesList = new ArrayList<>();

        for (int i = 0; i < processors.length; i++) {
            processorTitle.add("Processor " + i);
        }

        xAxis.setLabel("Time");
        xAxis.setTickLabelFill(Color.BLACK);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("Processors");
        yAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(processorTitle);

        ganttChart.setTitle("Schedule Times");
        ganttChart.setLegendVisible(false);
        ganttChart.setBlockHeight(50);

        for (int i = 0; i < processors.length; i++) {
            XYChart.Series newSeries = new XYChart.Series();
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                if (currentColour == null) {
                    Random rand = new Random();
                    int randomColour = rand.nextInt((colours.size() - 1));
                    currentColour = colours.get(randomColour);
                } else {
                    Random rand = new Random();
                    int randomColour = rand.nextInt((colours.size() - 1));
                    currentColour = colours.get(randomColour);
                    colours.add(previousColour);
                }

                newSeries.getData().add(new XYChart.Data(processors[i].mapOfTasksAndStartTimes.get(n),
                        processorTitle.get(i), new GanttChartFX.ExtraData(GraphController.getNodeWeight(n.getId(),
                        Main.getController().getGraph()), currentColour, n.getId())));

                newSeries.setName(n.getId());

                previousColour = currentColour;
                colours.remove(currentColour);
            }
            seriesList.add(newSeries);
        }

        for (XYChart.Series s: seriesList) {
            ganttChart.getData().add(s);
        }
        ganttChart.getStylesheets().add(Main.class.getClassLoader().getResource("ganttchart.css").toExternalForm());
    }

    // Getters and setters for the fields for this class
    public static void setTextX(String nodeId, Double xVal) {
        textX.put(nodeId, xVal);
    }

    public static void setTextY(String nodeId, Double yVal) {
        textY.put(nodeId, yVal);
    }

    public static GanttChartFX getGanttChart() {
        return ganttChart;
    }
    public static HashMap getTextX() {
        return textX;
    }
    public static HashMap getTextY() {
        return textY;
    }

}

