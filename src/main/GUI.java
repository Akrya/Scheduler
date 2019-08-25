package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    private static final String MAIN_MENU_SCENE = "/views/Main.fxml";
    private static final String APPLICATION_TITLE = "Scheduler";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.getController().startGanttVisualise();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_MENU_SCENE));
        Parent root = loader.load();

        // show application
        primaryStage.setTitle(APPLICATION_TITLE);
        primaryStage.setScene(new Scene(root, 910, 600));
        primaryStage.setResizable(false);
        primaryStage.show();

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
