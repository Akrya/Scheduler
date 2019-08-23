package main.controller;

import graph.GraphController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.controller.GanttChartController;

import graph.TaskGraph;
import main.Main;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import solutionfinder.BasicSolutionFinder;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.Processor;
import solutiontreecreator.data.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;

    private TaskGraph inputGraph;

    private Solution solution;

    /**
     *  Method that handles the parsing of the dot file and makes a graph object using the
     *  GraphStream library. Builds the solution tree and then calls the find solution method
     *  on the tree.
     */
    public void initialise() throws IOException {

        //inputGraph = new TaskGraph("inputGraph");

        //inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        //SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(numOfProcessors, inputGraph);
        //solutionTreeCreator.buildSolutionTree();

        //findSolution(solutionTreeCreator);

        //writeOutputFile();


        if (visualizeSearch) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("views/Main.fxml"));
            Parent root = loader.load();

            GanttChartController.initialiseChart();
            Scene scene  = new Scene(root,620,450);
            GanttChartController.showStage(scene);
        }

    }

    /**
     * Constructs strings that need to be written onto the dot file.
     */
    private void writeOutputFile() {

        File outputFile = new File(outputFileName);

        if (outputFile.exists()) {
            outputFile.delete();
        }

        String initialLine = "digraph \"" + outputFileName.replace(".dot", "") + "\" {\n";

        fileWriter(outputFile, initialLine);

        Processor[] processors = solution.getProcessors();

        // Writing the nodes to the file
        for (int i = 0; i < processors.length; i++) {
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                String nextLine = "\t " + n.getId() + "\t " + "[Weight=" + GraphController.getNodeWeight(n.getId(),
                        inputGraph) + ",Start=" + processors[i].mapOfTasksAndStartTimes.get(n) + ",Processor="
                        + i + "];\n";
                fileWriter(outputFile, nextLine);
            }
        }

        // Writing the edges onto the file
        for (Edge e: inputGraph.getEdgeSet()) {
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
     * @param line - The line that needs to be written in that file
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
     * Calls the findOptimalSolution method from the BasicSolutionFinder Class onto the
     * solution tree that was the input parameter. Assigns a random solution from the list
     * of minimal solutions found to the solution parameter for the controller class
     * @param solutionTreeCreator - A tree that represents the search space of the algorithm
     */
    private void findSolution(SolutionTreeCreator solutionTreeCreator) {
        List<Solution> solutions = BasicSolutionFinder.findOptimalSolution(solutionTreeCreator.getTreeRoot());

        Random rand = new Random();
        int randomSolution = rand.nextInt((solutions.size() - 1));

        solution = solutions.get(randomSolution);

    }

    public void parseInputArguments(String[] inputArgs) {
        // Parsing the input arguments and assigning necessary fields in the controller class
        int totalArgs = inputArgs.length;

//        System.out.println(totalArgs);

        // checks if input contains the correct number of arguments
        if (totalArgs < 2) {
            printInputArgumentsError();
        } else {
            if (inputArgs[0].contains(".dot")) {
//                System.out.println("There is a dot file");
                setGraphFilename(inputArgs[0]);
            } else {
                printInputArgumentsError();
            }
            try {
                int numOfProcessors = Integer.parseInt(inputArgs[1]);
//                System.out.println(numOfProcessors);
                setNumOfProcessors(numOfProcessors);
            } catch (NumberFormatException e) {
                printInputArgumentsError();
            }

//            System.out.println("This is executed before");

            if (totalArgs > 2) {
//                System.out.println("This is executed");
                String[] remainingArgs = Arrays.copyOfRange(inputArgs, 2,totalArgs);
                int remainingArgsLength = remainingArgs.length;
                boolean[] valuesSet = new boolean[3];

                for (int i = 0; i < remainingArgsLength; i++) {
                    if (remainingArgs[i].contains("-p")) {
//                        System.out.println(remainingArgs[i]);
                        try {
                            int numOfCores = Integer.parseInt(remainingArgs[i+1]);
//                            System.out.println(numOfCores);
                            setNumOfCores(numOfCores);
                            valuesSet[0] = true;
                            i++;
                        } catch (NumberFormatException e) {
                            printInputArgumentsError();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            printInputArgumentsError();
                        }
                    } else if (remainingArgs[i].contains("-v")) {
//                        System.out.println(remainingArgs[i]);
                        setVisualizeSearch(true);
                        valuesSet[1] = true;
                    } else if (remainingArgs[i].contains("-o")) {
//                        System.out.println(remainingArgs[i]);
                        try {
                            String outputFileName = remainingArgs[i+1];
//                            System.out.println(outputFileName);
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
                    setNumOfCores(1);
                }
                if (valuesSet[1] != true) {
                    setVisualizeSearch(false);
                }
                if (valuesSet[2] != true) {
                    String inputFileName = getGraphFilename();
                    setOutputFileName(inputFileName.replace(".dot","") + "-output.dot");
                }

            } else {
                setNumOfCores(1);
                setVisualizeSearch(false);
                String inputFileName = getGraphFilename();
                setOutputFileName(inputFileName.replace(".dot","") + "-output.dot");
//                System.out.println(controller.getOutputFileName());
            }

        }
    }

    /**
     * Prints help message if incorrect number of arguments is detected.
     */
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

    public TaskGraph getGraph() {
        return inputGraph;
    }

}
