package main;

import main.controller.Controller;

import java.util.Arrays;

public class Main {

    private static Controller controller = new Controller();

    public static void main(String args[]) {

        // Parsing the input arguments and assigning necessary fields in the controller class

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
                System.out.println(numOfProcessors);
                controller.setNumOfProcessors(numOfProcessors);
            } catch (NumberFormatException e) {
                printInputArgumentsError();
            }

            System.out.println("This is executed before");

            if (totalArgs > 2) {
                System.out.println("This is executed");
                String[] remainingArgs = Arrays.copyOfRange(args, 2,totalArgs);
                int remainingArgsLength = remainingArgs.length;
                boolean[] valuesSet = new boolean[3];

                for (int i = 0; i < remainingArgsLength; i++) {
                    if (remainingArgs[i].contains("-p")) {
                        System.out.println(remainingArgs[i]);
                        try {
                            int numOfCores = Integer.parseInt(remainingArgs[i+1]);
                            System.out.println(numOfCores);
                            controller.setNumOfCores(numOfCores);
                            valuesSet[0] = true;
                            i++;
                        } catch (NumberFormatException e) {
                            printInputArgumentsError();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            printInputArgumentsError();
                        }
                    } else if (remainingArgs[i].contains("-v")) {
                        System.out.println(remainingArgs[i]);
                        controller.setVisualizeSearch(true);
                        valuesSet[1] = true;
                    } else if (remainingArgs[i].contains("-o")) {
                        System.out.println(remainingArgs[i]);
                        try {
                            String outputFileName = remainingArgs[i+1];
                            System.out.println(outputFileName);
                            valuesSet[2] = true;
                            i++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            printInputArgumentsError();
                        }
                    } else {
                        printInputArgumentsError();
                    }
                }

                if (valuesSet[0] != true) {
                    controller.setNumOfCores(1);
                }
                if (valuesSet[1] != true) {
                    controller.setVisualizeSearch(false);
                }
                if (valuesSet[2] != true) {
                    String inputFileName = controller.getGraphFilename();
                    controller.setOutputFileName(inputFileName.replace(".dot","") + "-output.dot");
                }

            } else {
                controller.setNumOfCores(1);
                controller.setVisualizeSearch(false);
                String inputFileName = controller.getGraphFilename();
                controller.setOutputFileName(inputFileName.replace(".dot","") + "-output.dot");
                System.out.println(controller.getOutputFileName());
            }

            controller.initialise();

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

