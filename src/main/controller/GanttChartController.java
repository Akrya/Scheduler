package main.controller;

import graph.GraphController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import main.Main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Node;
import solutiontreecreator.data.Processor;
import visualization.GanttChartFX;

import java.util.ArrayList;
import java.util.List;

public class GanttChartController {

    private static GanttChartController ourInstance = new GanttChartController();

    private static Stage primaryStage;

    private static Scene chart;

    final static NumberAxis xAxis = new NumberAxis();
    final static CategoryAxis yAxis = new CategoryAxis();

    final static GanttChartFX<Number,String> ganttChart = new GanttChartFX<>(xAxis, yAxis);

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

        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(processorTitle);

        ganttChart.setTitle("Schedule Times");
        ganttChart.setLegendVisible(false);
        ganttChart.setBlockHeight(50);

        for (int i = 0; i < processors.length; i++) {
//            currentXValue = 0;
            XYChart.Series newSeries = new XYChart.Series();
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                newSeries.getData().add(new XYChart.Data(processors[i].mapOfTasksAndStartTimes.get(n),
                        processorTitle.get(i), new GanttChartFX.ExtraData(GraphController.getNodeWeight(n.getId(),
                        Main.getController().getGraph()), "status-red")));
//                currentXValue++;
            }
            seriesList.add(newSeries);
        }

        for (XYChart.Series s: seriesList) {
            ganttChart.getData().add(s);
        }

        ganttChart.getStylesheets().add(Main.class.getClassLoader().getResource("ganttchart.css").toExternalForm());

    }

    public static void showStage() {
        chart = new Scene(ganttChart, 620, 350);
        primaryStage.setScene(chart);
        primaryStage.show();
    }



}

