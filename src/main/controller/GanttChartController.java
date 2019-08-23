package main.controller;

import graph.GraphController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
    @FXML
    static Pane graphPane, chartPane;
    @FXML
    static Button graphButton, chartButton;

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

/*        for (int i = 0; i < processors.length; i++) {
//            currentXValue = 0;
            XYChart.Series newSeries = new XYChart.Series();
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                newSeries.getData().add(new XYChart.Data(processors[i].mapOfTasksAndStartTimes.get(n),
                        processorTitle.get(i), new GanttChartFX.ExtraData(GraphController.getNodeWeight(n.getId(),
                        Main.getController().getGraph()), "status-blue")));
//                currentXValue++;
            }
            seriesList.add(newSeries);
        }

        for (XYChart.Series s: seriesList) {
            ganttChart.getData().add(s);
        }*/
        String[] machines = new String[] { "Machine 1", "Machine 2", "Machine 3" };
        String machine;

        machine = machines[0];
        XYChart.Series series1 = new XYChart.Series();
        series1.getData().add(new XYChart.Data(0, machine, new GanttChartFX.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data(1, machine, new GanttChartFX.ExtraData( 1, "status-green")));
        series1.getData().add(new XYChart.Data(2, machine, new GanttChartFX.ExtraData( 1, "status-red")));
        series1.getData().add(new XYChart.Data(3, machine, new GanttChartFX.ExtraData( 1, "status-green")));

        machine = machines[1];
        XYChart.Series series2 = new XYChart.Series();
        series2.getData().add(new XYChart.Data(0, machine, new GanttChartFX.ExtraData( 1, "status-green")));
        series2.getData().add(new XYChart.Data(1, machine, new GanttChartFX.ExtraData( 1, "status-green")));
        series2.getData().add(new XYChart.Data(2, machine, new GanttChartFX.ExtraData( 2, "status-red")));

        machine = machines[2];
        XYChart.Series series3 = new XYChart.Series();
        series3.getData().add(new XYChart.Data(0, machine, new GanttChartFX.ExtraData( 1, "status-blue")));
        series3.getData().add(new XYChart.Data(1, machine, new GanttChartFX.ExtraData( 1, "status-red")));
        series3.getData().add(new XYChart.Data(3, machine, new GanttChartFX.ExtraData( 1, "status-green")));

        ganttChart.getData().addAll(series1, series2, series3);

        ganttChart.getStylesheets().add(GanttChartController.class.getClassLoader().getResource("ganttchart.css").toExternalForm());

    }

    public static void showStage(Scene scene) {
        chartPane.getChildren().add(ganttChart);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}

