package main;

import main.controller.Controller;

public class Main {

    private static Controller controller = new Controller();

    public static void main(String args[]) {

        controller.parseInputArguments(args);
        controller.initialise();

        // Running the visualisation only if the parsing of the input dot file was successful
        if (controller.isVisualizeSearch()) {
            if (controller.getGraph() != null) {
                GUI.launchApplication(args);
            }
        }

    }

    public static Controller getController() {
        return controller;
    }

}

