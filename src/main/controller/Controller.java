package main.controller;

import graph.GraphController;
import graph.TaskGraph;
import main.Main;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import solutionfinder.AStarParallelSolutionFinder;
import solutionfinder.AStarSolutionFinder;
import solutionfinder.data.Processor;
import solutionfinder.data.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;
    private boolean parseFine;

    private TaskGraph inputGraph;

    private Solution solution;
    private Solution optimalSolution;

    private ViewController viewController;

    /**
     * Method that handles the parsing of the dot file and makes a graph object using the
     * GraphStream library. Builds the solution tree and then calls the find solution method
     * on the tree.
     *  Method that handles the parsing of the dot file and makes a graph object using the
     *  GraphStream library.
     */
    public void initialise() {
        inputGraph = new TaskGraph("inputGraph");
        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);
        solution = new Solution(inputGraph, numOfProcessors);
        optimalSolution = new Solution(inputGraph, numOfProcessors);
    }

    /**
     * Starts the A star algorithm either sequentially or parallelized depending upon the input.
     * Writes the result to the output file.
     */
    public void startSolutionFind() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                inputGraph.setUpBottomLevels();

                System.out.println("Starting to find solution");
                if (inputGraph != null) {
                    if (numOfCores != 1) {
                        AStarParallelSolutionFinder solutionFinder = new AStarParallelSolutionFinder(numOfProcessors, inputGraph, numOfCores);

                        try {
                            optimalSolution = solutionFinder.findOptimal();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        AStarSolutionFinder solutionFinder = new AStarSolutionFinder(numOfProcessors, inputGraph);
                        try {
                            optimalSolution = solutionFinder.findOptimal();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(optimalSolution.stringData());
                }
            }
        });

        thread.start();
    }

    public void finalize(){
        if (isVisualizeSearch()) {
            writeOutputFile();
            viewController.finish();
            ViewController.getGraphViewController().setGraphColours(ViewController.getGraphViewController().getGraph(), optimalSolution);
            GanttChartController.initialiseChart();

        } else {
            writeOutputFile();
        }
    }

    /**
     * Constructs strings that need to be written onto the dot file.
     */
    private void writeOutputFile() {

        File outputFile = new File(outputFileName);

        // Deletes an existing dot file with the same file name if there exists
        if (outputFile.exists()) {
            outputFile.delete();
        }

        String initialLine = "digraph \"" + outputFileName.replace(".dot", "") + "\" {\n";

        fileWriter(outputFile, initialLine);

        Processor[] processors = solution.getProcessors();

        // Writing the nodes to the file
        for (int i = 0; i < processors.length; i++) {
            for (Node n : processors[i].mapOfTasksAndStartTimes.keySet()) {
                String nextLine = "\t " + n.getId() + "\t " + "[Weight=" + GraphController.getNodeWeight(n.getId(),
                        inputGraph) + ",Start=" + processors[i].mapOfTasksAndStartTimes.get(n) + ",Processor="
                        + i + "];\n";
                fileWriter(outputFile, nextLine);
            }
        }

        // Writing the edges onto the file
        for (Edge e : inputGraph.getEdgeSet()) {
            String nextLine = "\t " + e.getSourceNode().getId() + " -> " + e.getTargetNode().getId() + "\t" +
                    "[Weight=" + GraphController.getEdgeWeight(e.getSourceNode().getId(), e.getTargetNode().getId(), inputGraph) + "];\n";
            fileWriter(outputFile, nextLine);
        }

        // Writing the final line
        String finalLine = "}\n";
        fileWriter(outputFile, finalLine);

    }

    /**
     * Writes the strings provided as the parameter to the output file using BufferedWriter
     * @param outputFile - The output dot file
     * @param line       - The line that needs to be written in that file
     */
    private void fileWriter(File outputFile, String line) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
            writer.append(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the input arguments that are passed in through the command line. Assigns the necessary
     * fields in the controller class depending upon the input arguments.
     * @param inputArgs - An array of string containing the input arguments
     */
    public void parseInputArguments(String[] inputArgs) {
        // Parsing the input arguments and assigning necessary fields in the controller class
        int totalArgs = inputArgs.length;

        // Checks if input contains the correct number of arguments
        if (totalArgs < 2) {
            printInputArgumentsError();
            parseFine = false;
        } else {
            if (inputArgs[0].contains(".dot")) {
                setGraphFilename(inputArgs[0]);
            } else {
                printInputArgumentsError();
                parseFine = false;
            }
            try {
                int numOfProcessors = Integer.parseInt(inputArgs[1]);
                setNumOfProcessors(numOfProcessors);
            } catch (NumberFormatException e) {
                printInputArgumentsError();
                parseFine = false;
            }

            if (totalArgs > 2) {
                String[] remainingArgs = Arrays.copyOfRange(inputArgs, 2, totalArgs);
                int remainingArgsLength = remainingArgs.length;
                boolean[] valuesSet = new boolean[3];

                for (int i = 0; i < remainingArgsLength; i++) {
                    if (remainingArgs[i].contains("-p")) {
                        try {
                            int numOfCores = Integer.parseInt(remainingArgs[i + 1]);
                            setNumOfCores(numOfCores);
                            valuesSet[0] = true;
                            i++;
                        } catch (NumberFormatException e) {
                            printInputArgumentsError();
                            parseFine = false;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            printInputArgumentsError();
                            parseFine = false;
                        }
                    } else if (remainingArgs[i].contains("-v")) {
                        setVisualizeSearch(true);
                        valuesSet[1] = true;
                    } else if (remainingArgs[i].contains("-o")) {
                        try {
                            String outputFileName = remainingArgs[i + 1];
                            setOutputFileName(outputFileName + ".dot");
                            valuesSet[2] = true;
                            i++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            printInputArgumentsError();
                            parseFine = false;
                        }
                    } else {
                        printInputArgumentsError();
                        parseFine = false;
                    }
                }

                if (valuesSet[0] != true) {
                    setNumOfCores(1);
                }
                if (valuesSet[1] != true) {
                    setVisualizeSearch(false);
                }
                if (valuesSet[2] != true) {
                    String inputFileName = getGraphFilename();
                    setOutputFileName(inputFileName.replace(".dot", "") + "-output.dot");
                }

                parseFine = true;

            } else {
                setNumOfCores(1);
                setVisualizeSearch(false);
                String inputFileName = getGraphFilename();
                setOutputFileName(inputFileName.replace(".dot", "") + "-output.dot");
                parseFine = true;
            }

        }
    }


    /**
     * Prints help message if incorrect number of arguments is detected.
     */
    private void printInputArgumentsError() {
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


// Getters and Setters for the various fields for this class

    public String getGraphFilename() {
        return dotFileName;
    }

    public void setGraphFilename(String graphFilename) {
        this.dotFileName = graphFilename;
    }

    public int getNumOfProcessors() {
        return numOfProcessors;
    }

    public void setNumOfProcessors(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
    }

    public int getNumOfCores() {
        return numOfCores;
    }

    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public boolean isVisualizeSearch() {
        return visualizeSearch;
    }

    public void setVisualizeSearch(boolean visualizeSearch) {
        this.visualizeSearch = visualizeSearch;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution){
        this.solution = solution;
    }

    public TaskGraph getGraph() {
        return inputGraph;
    }

    public boolean isParseFine() {
        return parseFine;
    }

    public void setOptimalSolution(Solution optimalSolution) {
        this.optimalSolution = optimalSolution;
    }

    public Solution getOptimalSolution() {
        return optimalSolution;
    }

    public ViewController getViewController() {
        return viewController;
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}