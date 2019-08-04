package data.taskgraph;

import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AbstractNode;


/**
 * 
 * Represents a task graph, which shows the order in which tasks must be run, task duration
 * and the cost of switching to another processor.
 * 
 * @author Teren
 *
 */
public class TaskGraph extends AbstractGraph{

	public TaskGraph(String id) {
		super(id);
	}

	@Override
	public <T extends Node> T getNode(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Node> T getNode(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Edge> T getEdge(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Edge> T getEdge(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEdgeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends Node> Iterator<T> getNodeIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Edge> Iterator<T> getEdgeIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addNodeCallback(AbstractNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addEdgeCallback(AbstractEdge edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeNodeCallback(AbstractNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeEdgeCallback(AbstractEdge edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void clearCallback() {
		// TODO Auto-generated method stub
		
	}

}
