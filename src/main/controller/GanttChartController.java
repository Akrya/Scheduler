package main.controller;

import graph.GraphController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.Main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Node;
import solutiontreecreator.data.Processor;
import visualization.GanttChartFX;

import java.util.*;

public class GanttChartController {

    private static GanttChartController ourInstance = new GanttChartController();

    private static Stage primaryStage;

    private static Scene chart;

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

    public static GanttChartController getInstance() {
        return ourInstance;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Gantt Chart");
        primaryStage.setResizable(false);
    }


    public static void initialiseChart() {

        Processor[] processors = Main.getController().getSolution().getProcessors();
        ObservableList<String> processorTitle = FXCollections.observableArrayList();
        List<XYChart.Series> seriesList = new ArrayList<>();

        for (int i = 0; i < processors.length; i++) {
            processorTitle.add("Processor " + i);
        }

        xAxis.setLabel("Time");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("Processors");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
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

    public static void setTextX(String nodeId, Double xVal) {
        textX.put(nodeId, xVal);
    }

    public static void setTextY(String nodeId, Double yVal) {
        textY.put(nodeId, yVal);
    }

    public GanttChartFX getGanttChart() {
        return ganttChart;
    }

//    public static void showStage() {
//        chart = new Scene(ganttChart, 620, 350);
//        primaryStage.setScene(chart);
//        primaryStage.show();
//    }

}

