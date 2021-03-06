package main;

import main.controller.Controller;

public class Main {

    private static Controller controller = new Controller();
    private static GUI gui;

    public static void main(String args[]) {

        // Calling the method to parse the input arguments. Starting the GUI application
        // if needed.
        controller.parseInputArguments(args);
        if (controller.isParseFine()) {
            controller.initialise();
            if (controller.getGraph() != null) {
                controller.getGraph().setUpBottomLevels();
                if (controller.isVisualizeSearch()) {
                    gui = new GUI();
                    gui.launchApplication(args);
                } else {
                    controller.startSolutionFind();
                }
            }
        }
    }

    public static GUI getGUI(){
        return gui;
    }

    public static Controller getController() {
        return controller;
    }

}