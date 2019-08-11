package graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class GraphController {

    private String FilePath;
    private Graph g;


    /**
     * Constructor for the GraphController class
     * Takes an input of the filepath of the dot file that we want to schedule for.
     * It creates a graph object which reflects the dot file. We can read and edit this graph.
     * @param filePath
     */
//    public GraphController(String filePath){
//        FilePath = filePath;
//        g = new DefaultGraph("g");
//        FileSource fs = new FileSourceDOT();
//        fs.addSink(g);
//        try {
//            fs.readAll(FilePath);
//        } catch(IOException e){
//            e.printStackTrace();
//        }
//    }

    /**
     * Takes a default graph and the DOT filename as input and returns a graph object with the parameters
     * specified by the DOT file.
     * @param inputGraph
     * @param dotFileName
     * @return
     */
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

    /**
     * Takes the input of the current graph and the desired file name, to output a dotfile of the current graph.
     * @param inputGraph
     * @param fileName
     */
    public static void outputGraphDotFile(Graph inputGraph, String fileName){
        FileSinkDOT dotSink = new FileSinkDOT();
        try{
            dotSink.writeAll(inputGraph,fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

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
     * @param nodeID
     * @param attributeName
     * @param attributeValue
     */
    public static void changeAttribute(String nodeID, String attributeName, int attributeValue, Graph inputGraph){
        Node n = inputGraph.getNode(nodeID);
        n.addAttribute(attributeName,attributeValue);
    }

    public static void viewGraph(Graph inputGraph){
        for (Node node : inputGraph) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = inputGraph.display();
    }

    /**
     * Dump information of all nodes and edges of this graph in the console.
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
