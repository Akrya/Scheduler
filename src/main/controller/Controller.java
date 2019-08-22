package main.controller;

import graph.GraphController;

import graph.TaskGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import solutionfinder.AStarParallelSolutionFinder;
import solutionfinder.data.Processor;
import solutionfinder.data.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    public void initialise() {

        inputGraph = new TaskGraph("inputGraph");

        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        AStarParallelSolutionFinder solutionFinder = new AStarParallelSolutionFinder(numOfProcessors, inputGraph);
        try {
            solution = solutionFinder.findOptimal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        writeOutputFile();

//        if (visualizeSearch) {
//            GraphController.viewGraph(inputGraph);
//        }

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

}
