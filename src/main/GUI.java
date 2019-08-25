package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controller.MainViewController;

public class GUI extends Application {
    private static final String MAIN_MENU_SCENE = "/views/Main.fxml";
    private static final String APPLICATION_TITLE = "Scheduler";
    private static Stage stage;
    private MainViewController mainViewController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_SCENE));
        Parent root = loader.load();
        mainViewController = loader.getController();
        Main.getController().setMainViewController(mainViewController);

        // show application
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root, 940, 590));
        primaryStage.setResizable(false);
        primaryStage.show();

        Main.getController().startSolutionFind();

        // close the process when closing the window.
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void launchApplication(String args[]) {
        launch(args);
    }

    public MainViewController getMainViewController(){
        return mainViewController;
    }
}