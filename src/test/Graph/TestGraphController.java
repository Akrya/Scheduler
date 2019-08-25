package test.Graph;

import graph.GraphController;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestGraphController {
    private Graph Nodes_7_OutTree = new DefaultGraph("Nodes_7_OutTree.dot");
    private Graph Nodes_8_Random = new DefaultGraph("Nodes_8_Random.dot");
    private Graph Nodes_9_SeriesParallel = new DefaultGraph("Nodes_9_SeriesParallel.dot");
    private Graph Nodes_10_Random = new DefaultGraph("Nodes_10_Random.dot");
    private Graph Nodes_11_OutTree = new DefaultGraph("Nodes_11_OutTree.dot");

    @Before
    public void Initialise(){
        //Load all the graphs into the graphstream objects.

        Nodes_7_OutTree = GraphController.defaultGraph(Nodes_7_OutTree,"Nodes_7_OutTree.dot");
//        Nodes_8_Random = GraphController.defaultGraph(Nodes_8_Random,"Nodes_8_Random.dot");
//        Nodes_9_SeriesParallel = GraphController.defaultGraph(Nodes_9_SeriesParallel, "Nodes_9_SeriesParallel.dot");
//        Nodes_10_Random = GraphController.defaultGraph(Nodes_10_Random,"Nodes_10_Random.dot");
//        Nodes_11_OutTree = GraphController.defaultGraph(Nodes_11_OutTree,"Nodes_11_OutTree.dot");
    }

    @Test
    public void testNodeWeight(){
        assertEquals(GraphController.getNodeWeight("0",Nodes_7_OutTree),5,0);
        assertEquals(GraphController.getNodeWeight("1",Nodes_7_OutTree),6,0);
        assertEquals(GraphController.getNodeWeight("2",Nodes_7_OutTree),5,0);
        assertEquals(GraphController.getNodeWeight("3",Nodes_7_OutTree),6,0);
        assertEquals(GraphController.getNodeWeight("4",Nodes_7_OutTree),4,0);
        assertEquals(GraphController.getNodeWeight("5",Nodes_7_OutTree),7,0);
        assertEquals(GraphController.getNodeWeight("6",Nodes_7_OutTree),7,0);
    }

    @Test
    public void testEdgeWeight(){
//        assertEquals(GraphController.getEdgeWeight());
    }
}
