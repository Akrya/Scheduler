package main.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class GraphController {

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

    public static void changeAttribute(Graph inputGraph, String nodeID, String attributeName, int attributeValue){
        Node n = inputGraph.getNode(nodeID);
        n.addAttribute(attributeName,attributeValue);
    }

    public static void viewGraph(Graph inputGraph){
        for (Node node : inputGraph) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = inputGraph.display();
        viewer.enableAutoLayout();
    }

}
