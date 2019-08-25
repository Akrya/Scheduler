package main;

import com.sun.glass.ui.Application;
import main.controller.Controller;

public class Main {

    private static Controller controller = new Controller();


    public static void main(String args[]) {

        controller.parseInputArguments(args);
        controller.initializeGraph();

        // Running the visualisation only if the parsing of the input dot file was successful
        if (controller.isVisualizeSearch()) {
            if (controller.getGraph() != null) {
                GUI.launchApplication(args);
            } else {
                controller.initialiseSolutionFind();
            }
        } else {
            controller.initialiseSolutionFind();
        }

    }

    public static Controller getController() {
        return controller;
    }



}

