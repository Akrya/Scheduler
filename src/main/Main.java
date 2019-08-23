package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.Controller;
import main.controller.GanttChartController;

import java.util.List;

public class Main extends Application {

    private static Controller controller = new Controller();

/*    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.setTitle("Gantt Chart Sample");

        GanttChartController ganttChartController = GanttChartController.getInstance();
        ganttChartController.setPrimaryStage(primaryStage);

        List<String> inputParameters = this.getParameters().getRaw();
        String[] inputArgs = new String[inputParameters.size()];
        inputArgs = inputParameters.toArray(inputArgs);
        //controller.parseInputArguments(inputArgs);
        controller.initialise();

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("views/Main.fxml"));
//        Parent root = loader.load();
//        GanttChattController gcontroller = loader.getController();
//
//        Scene scene  = new Scene(root,620,450);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{
        // load the main menu scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/Main.fxml"));
        Parent root = loader.load();


        // show application
        primaryStage.setTitle("S");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String args[]) {

        launch(args);

    }

    public static Controller getController() {
        return controller;
    }

}

