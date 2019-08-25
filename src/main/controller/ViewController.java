package main.controller;

import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.graphstream.ui.swingViewer.ViewPanel;
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
    Label timeElapsed;

    private static HashMap<String, Double> textX;
    private static HashMap<String, Double> textY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        graphPane = new Pane();
        graphPane.toFront();
        graphPane.setMinSize(graphPane.getPrefWidth(),graphPane.getPrefWidth());
        GraphViewController graphView = new GraphViewController();
        SwingNode view = graphView.viewGraph();
        view.resize(750,600);
        graphPane.getChildren().add(view);


        GanttChartFX chart = GanttChartController.getGanttChart();
        chart.setMinSize(chartPane.getPrefWidth(),chartPane.getPrefHeight());
        chartPane.getChildren().add(chart);
        double startTime = System.currentTimeMillis();
        DecimalFormat f = new DecimalFormat("##.00");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedMillis = System.currentTimeMillis() - startTime;
                timeElapsed.setText(f.format(elapsedMillis / 1000));
            }
        };
        timer.start();

    }

    public void graphButtonClick(MouseEvent mouseEvent) {
        graphPane.toFront();
    }

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

}

