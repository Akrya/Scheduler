package main;

import main.controller.Controller;

public class Main {

    private static Controller controller = new Controller();

    public static void main(String args[]) {

        // Calling the method to parse the input arguments. Starting the GUI application
        // if needed.
        controller.parseInputArguments(args);
        if (controller.isParseFine()) {
            controller.initialise();
            if (controller.getGraph() != null) {
                if (controller.isVisualizeSearch()) {
                    GUI.launchApplication(args);
                } else {
                    controller.startSolutionFind();
                }
            }
        }

    }

    public static Controller getController() {
        return controller;
    }

}