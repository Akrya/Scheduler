package test;

import main.graph.GraphTools;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGraphTools {
    private Graph Nodes_7_OutTree = new DefaultGraph("Nodes_7_OutTree.dot");

    @Before
    public void Initialise(){
        //Load all the graphs into the graphstream objects.
        Nodes_7_OutTree = GraphTools.defaultGraph(Nodes_7_OutTree,"Nodes_7_OutTree.dot");
    }

    /**
     * Tests that it can get the correct weight of each node.
     */
    @Test
    public void testNodeWeight(){
        assertEquals(GraphTools.getNodeWeight("0",Nodes_7_OutTree),5,0);
        assertEquals(GraphTools.getNodeWeight("1",Nodes_7_OutTree),6,0);
        assertEquals(GraphTools.getNodeWeight("2",Nodes_7_OutTree),5,0);
        assertEquals(GraphTools.getNodeWeight("3",Nodes_7_OutTree),6,0);
        assertEquals(GraphTools.getNodeWeight("4",Nodes_7_OutTree),4,0);
        assertEquals(GraphTools.getNodeWeight("5",Nodes_7_OutTree),7,0);
        assertEquals(GraphTools.getNodeWeight("6",Nodes_7_OutTree),7,0);
    }

    /**
     *  Tests that it can get the correct weights of each edge.
     */
    @Test
    public void testEdgeWeight(){
        assertEquals(GraphTools.getEdgeWeight("0","1",Nodes_7_OutTree),15,0);
        assertEquals(GraphTools.getEdgeWeight("0","2",Nodes_7_OutTree),11,0);
        assertEquals(GraphTools.getEdgeWeight("0","3",Nodes_7_OutTree),11,0);
        assertEquals(GraphTools.getEdgeWeight("1","4",Nodes_7_OutTree),19,0);
        assertEquals(GraphTools.getEdgeWeight("1","5",Nodes_7_OutTree),4,0);
        assertEquals(GraphTools.getEdgeWeight("1","6",Nodes_7_OutTree),21,0);
    }

    /**
     *  Tests whether changing the attribute is changed correctly.
     */
    @Test
    public void testChangeAttribute(){
        assertEquals(GraphTools.getNodeWeight("0",Nodes_7_OutTree),5,0);
        GraphTools.changeAttribute("0","Weight",100,Nodes_7_OutTree);
        assertTrue("The weight was changed correctly", GraphTools.getNodeWeight("0",Nodes_7_OutTree)==100);
    }

}
