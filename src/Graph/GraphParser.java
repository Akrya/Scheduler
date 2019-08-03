package Graph;

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

    public void viewGraph(){
        for (Node node : g) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = g.display();
    }
}
