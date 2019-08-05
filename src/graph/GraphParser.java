package graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.graphstream.ui.view.Viewer;

import java.io.IOException;

/**
 * 
 * Class that parses a graph file and provides methods for displaying graph information.
 * 
 * @author Shunji Takano
 *
 */
public class GraphParser {

    private String FilePath;
    public DefaultGraph g;

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
     * Get the weight of a specific node
     * @param nodeID id of the node
     * @return weight of node
     */
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

    /**
     * Get the weight of a specific edge
     * @param firstNode id of the first node
     * @param secondNode id of the second node
     * @return weight of link between firstNode and secondNode
     */
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

    public Graph getGraph() {
    	return g;
    }
    
    /**
     * View the graph in a graphic display for debugging purposes.
     */
    public void viewGraph(){
        for (Node node : g) {
            node.addAttribute("ui.label", node.getId());
        }
        Viewer viewer = g.display();
    }
    
    /**
     * Dump information of all nodes and edges of this graph in the console.
     */
    public void printGraphInConsole() {
    	// Node info dump
    	System.out.println("-----------------------------------");
    	System.out.println("-------------NODE DUMP-------------");
    	System.out.println("-----------------------------------");
    	for(Node n : g.getEachNode()) {
    		System.out.println(n.getId()+": "+n.getNumber("Weight"));
    	}
    	
    	// Edge info dump
    	System.out.println("-----------------------------------");
    	System.out.println("-------------EDGE DUMP-------------");
    	System.out.println("-----------------------------------");
    	for(Edge e : g.getEachEdge()) {
    		System.out.println(e.getId()+": "+e.getNumber("Weight"));
    	}
    }
}