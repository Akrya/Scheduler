package main;

import main.controller.Controller;

public class Main {

    private static Controller controller = new Controller();

    public static void main(String args[]) {

        controller.parseInputArguments(args);
        controller.initialise();

        if (controller.isVisualizeSearch()) {
            GUI.launchApplication(args);
        }

    }

    public static Controller getController() {
        return controller;
    }

}

