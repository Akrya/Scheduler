package main.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class GraphParser {

    private String FilePath;
    private Graph g;

    public static Graph parseInputFile(Graph inputGraph, String dotFileName) {

        FileSource fileSource = new FileSourceDOT();
        fileSource.addSink(inputGraph);
        try {
            fileSource.readAll(dotFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputGraph;

    }

    public static double getNodeWeight(Graph inputGraph, String nodeID){
        double value = 0;
        try {
            Node n = inputGraph.getNode(nodeID);
            value = n.getAttribute("Weight");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return value;
    }

    public static double getEdgeWeight(Graph inputGraph, String firstNode, String secondNode){
        double edgeWeight = 0;
        try {
            Edge e = inputGraph.getEdge("(" + firstNode + ";" + secondNode + ")");
            edgeWeight = e.getNumber("Weight");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return edgeWeight;
    }

    public void changeAttribute(String nodeID, String attributeName, int attributeValue){
        Node n = g.getNode(nodeID);
        n.addAttribute(attributeName,attributeValue);
    }

    public void viewGraph(){
        for (Node node : g) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = g.display();
    }
}
