package solutiontreecreator.data;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;

/**
 * 
 * This class represents a solution, a series of tasks scheduled amongst multiple processors.
 * 
 * @author Terence Qu
 */
public class Solution {

	private int numProcessors;
	private Processor[] processors;
	private int currentProcessor;

	private TaskGraph taskGraph;
	private List<Node> taskList;
	private List<Node> tasksLeft;


	public Solution(TaskGraph taskGraph, int numProcessors) {
		this.taskGraph = taskGraph;
		this.numProcessors = numProcessors;
		processors = new Processor[numProcessors];

		// Populate the processor list
		for(int i = 0; i < numProcessors; i++) {
			processors[i] = new Processor();
		}

		currentProcessor = 0;

		taskList = new ArrayList<Node>();
		tasksLeft = new ArrayList<Node>();
		tasksLeft.addAll(taskGraph.getNodeSet());
	}

	/**
	 * Adds a task to a processor.
	 * @param n
	 * @param predecessorLink
	 * @param processorIndex
	 */
	public boolean addTask(Node n, int targetProcessorIndex) {
		// Create dependency edge list
		List<Edge> dependencyLinks = new ArrayList<Edge>(n.getEnteringEdgeSet());

		// Create dependency node list
		List<Node> dependencyTasks = new ArrayList<Node>();
		for(Edge dependency: dependencyLinks) {
			dependencyTasks.add(dependency.getSourceNode());
		}

		// All dependencies must've been added already.
		if(taskList.containsAll(dependencyTasks)) {
			
			// Calculate latest dependency end time
			double latestTime = 0;
			double latestTimeWithDelay = 0;
			for(Processor p: processors) {
				for(Edge dependencyLink: dependencyLinks) {
					double tempTime = 0;
					if(p.mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode())!=null) {
						tempTime = p.mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode()) + (double)dependencyLink.getSourceNode().getAttribute("Weight");
					} else {
						tempTime = 0;
					}
					double tempTimeWithDelay = tempTime + (double)dependencyLink.getAttribute("Weight");
					if(tempTime > latestTime) {
						latestTime = tempTime;
					}
					if(tempTimeWithDelay > latestTimeWithDelay) {
						latestTimeWithDelay = tempTimeWithDelay;
					}
				}
			}
			
			if(currentProcessor == targetProcessorIndex) {
				System.out.println("Task successfully added at processor "+targetProcessorIndex+" : "+n.getId());
				processors[targetProcessorIndex].addTaskSpecificTime(n, latestTime);
				taskList.add(n);
				tasksLeft.remove(n);
				currentProcessor = targetProcessorIndex;
				return true;
			} else {
				// Need to work on this more
				System.out.println("Task successfully added at another processor processor "+targetProcessorIndex+" : "+n.getId());
				processors[targetProcessorIndex].addTaskSpecificTime(n, latestTimeWithDelay);
				taskList.add(n);
				tasksLeft.remove(n);
				currentProcessor = targetProcessorIndex;
				return true;
			}
		} else {
			System.out.println("Task unsuccessfully added at processor "+targetProcessorIndex+" : "+n.getId());
			return false;
		}

	}


	// Getters and setters
	public List<Node> getTaskList(){
		return taskList;
	}

	public void setTaskList(List<Node> taskList) {
		this.taskList = taskList;
	}

	public List<Node> getTasksLeft(){
		return tasksLeft;
	}

	public void setTasksLeft(List<Node> tasksLeft) {
		this.tasksLeft = tasksLeft;
	}

	public Processor[] getProcessors() {
		return processors;
	}

	public int getNumProcessors() {
		return numProcessors;
	}

	public Processor getProcessor(int index) {
		return processors[index];
	}

	public int getCurrentProcessor() {
		return currentProcessor;
	}

	public void setCurrentProcessor(int currentProcessor) {
		this.currentProcessor = currentProcessor;
	}
	/**
	 * Print the data of this solution to the console.
	 */
	public void printData() {
		for(int i = 0; i < numProcessors; i++) {
			System.out.println("Processor number: "+i);
			for(Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
				System.out.println("Node "+n.getId()+"| Start time:"+processors[i].mapOfTasksAndStartTimes.get(n));
			}
		}
	}

	/**
	 * Method for generating a string representation of the solution.
	 * @return 
	 */
	public String stringData() {
		String data = "";
		for(int i = 0; i < numProcessors; i++) {
			data += "{PROCESSOR: "+i+"}";
			for(Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
				data += "|Node "+n.getId()+"| Start time:"+processors[i].mapOfTasksAndStartTimes.get(n);
			}
		}

		return data+"";
	}
}
