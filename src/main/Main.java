package main;

import main.controller.Controller;

import java.io.File;
import java.util.Arrays;

public class Main {

    private static Controller controller = new Controller();

    public static void main(String args[]) {

        int totalArgs = args.length;

        System.out.println(totalArgs);

        if (totalArgs < 2) {
            printInputArgumentsError();
        } else {

            if (args[0].contains(".dot")) {
                System.out.println("There is a dot file");
                controller.setGraphFilename(args[0]);
            } else {
                printInputArgumentsError();
            }

            try {
                int numOfProcessors = Integer.parseInt(args[1]);
                controller.setNumOfProcessors(numOfProcessors);
            } catch (NumberFormatException e) {
                printInputArgumentsError();
            }

            if (totalArgs > 2) {
                String[] remainingArgs = Arrays.copyOfRange(args, 2,totalArgs-1);

                for (String arg: remainingArgs) {
                    System.out.println(arg);
                }
            } else {
                controller.setNumOfCores(1);
                controller.setVisualizeSearch(false);
                String inputFileName = controller.getGraphFilename();
                controller.setOutputFileName(inputFileName.replace(".dot","") + "-output.dot");
                System.out.println(controller.getOutputFileName());
            }


        }

    }

    private static void printInputArgumentsError() {
        System.out.println("Invalid input");
        System.out.println("Please run the jar file using the following interface ->\n");
        System.out.println("java−jar scheduler . jar INPUT.dot P [OPTION]");
        System.out.println("INPUT. dot   a task graph with integer weights in dot format");
        System.out.println("P            number of  processors  to  schedule  the INPUT graph on");
        System.out.println("Optional:");
        System.out.println("−p N use N cores for execution in parallel (default  is  sequential)");
        System.out.println("−v visualise the search");
        System.out.println("−o OUTPUT   output file  is named OUTPUT (default  is INPUT−output.dot)");
    }

}
