package main.controller;

import main.graph.GraphParser;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.IOException;

public class Controller {

    private String dotFileName;
    private int numOfProcessors;
    private int numOfCores;
    private boolean visualizeSearch;
    private String outputFileName;

    private Graph inputGraph;

    public void initialise() {

        inputGraph = new DefaultGraph("inputGraph");

        inputGraph = GraphParser.parseInputFile(inputGraph, dotFileName);

    }

    public void parseInputFile() {

    }

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
