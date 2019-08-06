package Graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

public class GraphParser {

    private String FilePath;
    private Graph g;


    /**
     * Constructor for the GraphController class
     * Takes an input of the filepath of the dot file that we want to schedule for.
     * It creates a graph object which reflects the dot file. We can read and edit this graph.
     * @param filePath
     */
    public GraphParser(String filePath){
        FilePath = filePath;
        g = new DefaultGraph("g");
        FileSource fs = new FileSourceDOT();
        fs.addSink(g);
        try {
            fs.readAll(FilePath);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Takes the input of the current graph and the desired file name, to output a dotfile of the current graph.
     * @param graph
     * @param fileName
     */
    public static void outputGraphDotFile(GraphParser graph, String fileName){
        FileSinkDOT dotSink = new FileSinkDOT();
        try{
            dotSink.writeAll(graph.g,fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public double getNodeWeight(String nodeID){
        double value = 0;
        try {
            Node n = g.getNode(nodeID);
            value = n.getAttribute("Weight");
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return value;
    }

    public double getEdgeWeight(String firstNode, String secondNode){
        double edgeWeight = 0;
        try {
            Edge e = g.getEdge("(" + firstNode + ";" + secondNode + ")");
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
