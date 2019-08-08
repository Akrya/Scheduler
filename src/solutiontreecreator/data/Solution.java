package solutiontreecreator.data;

import org.graphstream.graph.Node;

public class Solution {
	
	public final int numProcessors;
	public final Timeline[] processors;
	public int currentProcessor;
	private Node lastTask;
	
	/**
	 * Represents a solution, a series of tasks scheduled amongst processors
	 * @param numProcessors
	 */
	public Solution(int numProcessors) {
		this.numProcessors = numProcessors;
		processors = new Timeline[numProcessors];
		
		// Populate the processor list
		for(int i = 0; i < numProcessors; i++) {
			processors[i] = new Timeline();
		}
		
		currentProcessor = 0;
	}
	
	/**
	 * Adds a task to a target processor, determining whether communication delay is needed or not
	 * @param n
	 * @param predecessorLink
	 * @param processorIndex
	 */
	public void addTask(Node n, double weightOfDelay, int processorIndex) {
		System.out.println("Adding task "+n.getId()+" to processor number "+processorIndex+".");
		lastTask = n;
		if(processorIndex == currentProcessor) {
			processors[processorIndex].addTask(n);
		} else {
			processors[processorIndex].addTaskWithDelay(n, weightOfDelay);
			currentProcessor = processorIndex;
		}
	}
	
	/**
	 * Adds a task to a target processor without any delay
	 * @param n
	 * @param processorIndex
	 */
	public void addTaskNoDelay(Node n, int processorIndex) {
		processors[processorIndex].addTask(n);
	}
	
	public Node getLastTask() {
		return lastTask;
	}
}
