package graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class GraphController {

    /**
     * Changes the input TaskGraph object to adhere to the constraints given in the input DOT file.
     * @param inputGraph A blank TaskGraph object.
     * @param dotFileName Name of the DOT File you want to convert to TaskGraph into.
     * @return The TaskGraph which now contains the information from the DOT File.
     */
    public static TaskGraph parseInputFile(TaskGraph inputGraph, String dotFileName) {

        FileSource fileSource = new FileSourceDOT();
        fileSource.addSink(inputGraph);
        try {
            fileSource.readAll(dotFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputGraph;

    }

    /**
     * A getter which returns the weight of a node.
     * @param nodeID Unique string identifying the node from which you want to find the node weight from.
     * @param inputGraph The graph you want to read from.
     * @return A double of the weight of the desired node.
     */
    public static double getNodeWeight(String nodeID, Graph inputGraph){
        double value = 0;
        try {
            Node n = inputGraph.getNode(nodeID);
            value = n.getAttribute("Weight");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return value;
    }

    /**
     * A getter which returns the weight between two nodes.
     * @param firstNode Unique string identifying the first node.
     * @param secondNode Unique string identifying the second node.
     * @param inputGraph The graph you want to read from.
     * @return A double value of the weight between the first node and the second node.
     */
    public static double getEdgeWeight(String firstNode, String secondNode, Graph inputGraph){
        double edgeWeight = 0;
        try {
            Edge e = inputGraph.getEdge("(" + firstNode + ";" + secondNode + ")");
            edgeWeight = e.getNumber("Weight");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return edgeWeight;
    }

    /**
     * Can change or add an attribute to any node given its id.
     * @param nodeID Unique string identifying the node.
     * @param attributeName Name of the attribute you want to add/change.
     * @param attributeValue An integer value of the attribute you want to add.
     */
    public static void changeAttribute(String nodeID, String attributeName, int attributeValue, Graph inputGraph){
        Node n = inputGraph.getNode(nodeID);
        n.addAttribute(attributeName,attributeValue);
    }

    /**
     * Creates a visualisation of a graph.
     * @param inputGraph Graph you want to visualise.
     */
    public static void viewGraph(Graph inputGraph){
        for (Node node : inputGraph) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = inputGraph.display();
    }

    /**
     * Dump information of all nodes and edges of this graph in the console.
     * @param inputGraph Graph you want to read from.
     */
    public static void printGraphInConsole(Graph inputGraph) {
        // Node info dump
        System.out.println("-----------------------------------");
        System.out.println("-------------NODE DUMP-------------");
        System.out.println("-----------------------------------");
        for(Node n : inputGraph.getEachNode()) {
            System.out.println(n.getId()+": "+n.getNumber("Weight"));
        }

        // Edge info dump
        System.out.println("-----------------------------------");
        System.out.println("-------------EDGE DUMP-------------");
        System.out.println("-----------------------------------");
        for(Edge e : inputGraph.getEachEdge()) {
            System.out.println(e.getId()+": "+e.getNumber("Weight"));
        }
    }

}
