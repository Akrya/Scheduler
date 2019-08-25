package main.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import main.Main;
import visualization.GanttChartFX;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    Pane graphPane;
    @FXML
    Pane chartPane;
    @FXML
    Button graphButton, chartButton;
    @FXML
    Label timeElapsed, solutionsPruned, solutionsExplored, stackSize;
    @FXML
    ProgressIndicator progressSpin;

    private static HashMap<String, Double> textX;
    private static HashMap<String, Double> textY;

    private static GraphViewController graphView = new GraphViewController();

    private static AnimationTimer timer;

    /**
     * ViewController is the controller for the main.fxml and is initialized when the fxml is loaded
     * in GUI.
     * The
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        graphPane.toFront();
        graphPane.setMinSize(graphPane.getPrefWidth(),graphPane.getPrefWidth());
        graphView.setProcessorColours(Main.getController().getNumOfProcessors());
        SwingNode view = graphView.viewGraph();
        graphPane.getChildren().add(view);

        GanttChartFX chart = GanttChartController.getGanttChart();
        chart.setMinSize(chartPane.getPrefWidth(),chartPane.getPrefHeight());
        chartPane.getChildren().add(chart);
        double startTime = System.currentTimeMillis();
        DecimalFormat f = new DecimalFormat("##.00");

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedMillis = System.currentTimeMillis() - startTime;
                timeElapsed.setText(f.format(elapsedMillis / 1000));
            }
        };
        timer.start();

    }

    /**
     * Shows the graph visualisation when the graph button is pressed.
     * @param mouseEvent mouse click
     */
    public void graphButtonClick(MouseEvent mouseEvent) {
        graphPane.toFront();
    }

    /**
     * Loads and displays the Gantt chart when the chart button is pressed.
     * @param mouseEvent mouse click
     */
    public void chartButtonClick(MouseEvent mouseEvent) {
        chartPane.toFront();
        textX = GanttChartController.getTextX();
        textY = GanttChartController.getTextY();
        for (String s : textX.keySet()) {
            Label l = new Label();
            l.setText(s);
            l.setStyle("-fx-text-fill: white;" +
                    "-fx-font-size: 15px");
            l.setLayoutX(textX.get(s)+125);
            l.setLayoutY(textY.get(s)+30);
            chartPane.getChildren().add(l);
        }
    }

    public static GraphViewController getGraphViewController() {
        return graphView;
    }

    public void finish() {
        timer.stop();
        progressSpin.setProgress(100);
    }
    public void setPruned(int pruned){
        solutionsPruned.setText(Integer.toString(pruned));
    }
    public void setExplored(int explored){
        solutionsExplored.setText(Integer.toString(explored));
    }
    public void setStackSize(int stack){
        stackSize.setText(Integer.toString(stack));
    }

}

