package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.GanttChartController;
import main.controller.ViewController;

public class GUI extends Application {
    private static final String MAIN_MENU_SCENE = "/views/Main.fxml";
    private static final String APPLICATION_TITLE = "Scheduler";
    private static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Main.getController().startGanttVisualise();
        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_MENU_SCENE));
        Parent root = loader.load();
        ViewController vcontroller = loader.getController();
        // show application
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root, 940, 590));
        primaryStage.setResizable(false);
        primaryStage.show();
        Main.getController().startSolutionFind();
        vcontroller.finish();

        Main.getController().initialiseSolutionFind();

        // close the process when closing the window.
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void launchApplication(String args[]) {
        launch(args);
    }
}
