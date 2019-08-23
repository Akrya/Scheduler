package main.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import visualization.GanttChartFX;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    @FXML
    Pane graphPane, chartPane;
    @FXML
    Button graphButton, chartButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void graphButtonClick(MouseEvent mouseEvent) {
        graphPane.toFront();
    }

    public void chartButtonClick(MouseEvent mouseEvent) {
        chartPane.toFront();
    }
}

