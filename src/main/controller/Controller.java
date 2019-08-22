package main.controller;

import graph.GraphController;

import graph.TaskGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import solutionfinder.BasicSolutionFinder;
import solutiontreecreator.SolutionTreeCreator;
import solutiontreecreator.data.Processor;
import solutiontreecreator.data.Solution;
import visualization.GanttChart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public void initialise() {

        inputGraph = new TaskGraph("inputGraph");

        inputGraph = GraphController.parseInputFile(inputGraph, dotFileName);

        SolutionTreeCreator solutionTreeCreator = new SolutionTreeCreator(numOfProcessors, inputGraph);
        solutionTreeCreator.buildSolutionTree();

        findSolution(solutionTreeCreator);

        writeOutputFile();

        GanttChart ganttChart = new GanttChart("Times");
        ganttChart.pack();
        RefineryUtilities.centerFrameOnScreen(ganttChart);
        ganttChart.setVisible(true);

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
