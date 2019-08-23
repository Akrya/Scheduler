package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.controller.GanttChartController;

public class GUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GanttChartController ganttChartController = GanttChartController.getInstance();
        ganttChartController.setPrimaryStage(primaryStage);
        Main.getController().initialise();
        Main.getController().startGanttVisualise();
    }

    public static void launchApplication(String args[]) {
        launch(args);
    }
}
