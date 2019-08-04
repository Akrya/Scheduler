package data.taskgraph;

import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.DefaultGraph;


/**
 * 
 * Represents a task graph, which shows the order in which tasks must be run, task duration
 * and the cost of switching to another processor.
 * 
 * @author Teren
 *
 */
public class TaskGraph extends DefaultGraph{

	public TaskGraph(String id) {
		super(id);
	}

}
