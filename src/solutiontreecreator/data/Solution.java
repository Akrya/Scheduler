package solutiontreecreator.data;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class Solution{
	
	public final int numProcessors;
	public final Timeline[] processors;
	public int currentProcessor;
	
	/**
	 * Represents a solution, a series of tasks scheduled amongst processors
	 * @param numProcessors
	 */
	public Solution(int numProcessors) {
		this.numProcessors = numProcessors;
		processors = new Timeline[numProcessors];
		currentProcessor = 0;
	}
	
	/**
	 * Adds a task to a target processor, determining whether communication delay is needed or not
	 * @param n
	 * @param predecessorLink
	 * @param processorIndex
	 */
	public void addTask(Node n, Edge predecessorLink, int processorIndex) {
		if(processorIndex == currentProcessor) {
			processors[processorIndex].addTask(n);
		} else {
			processors[processorIndex].addTaskWithDelay(n, predecessorLink.getAttribute("Weight"));
			currentProcessor = processorIndex;
		}
	}
	
	/**
	 * Adds a task to a target processor without any delayW
	 * @param n
	 * @param processorIndex
	 */
	public void addTaskNoDelay(Node n, int processorIndex) {
		processors[processorIndex].addTask(n);
	}
}
