package solutiontreecreator.data;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;

public class Solution {

	private int numProcessors;
	private Processor[] processors;
	private int currentProcessor;

	private TaskGraph taskGraph;
	private List<Node> taskList;
	private List<Node> tasksLeft;
	
	/**
	 * Represents a solution, a series of tasks scheduled amongst processors
	 * @param numProcessors
	 */
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
			if(targetProcessorIndex == currentProcessor) {
				// No delay needed
				System.out.println("Task successfully added at processor "+targetProcessorIndex+" : "+n.getId());
				processors[targetProcessorIndex].addTask(n);
				taskList.add(n);
				tasksLeft.remove(n);
				return true;
			} else {
				// Calculate how much delay needed
				double minStart = 100000;
				for(Edge dependency: dependencyLinks) {
					double cost = 0;
					cost += (double)dependency.getSourceNode().getAttribute("Weight"); // Duration of task
					cost += (double)dependency.getAttribute("Weight"); // Transmission delay
					for(Processor p: processors) {
						Double startTime = p.mapOfTasksAndStartTimes.get(dependency.getSourceNode());
						if(startTime != null) {
							cost += startTime.doubleValue();
						}
					}
					if(cost < minStart) {
						minStart = cost;
					}
				}
				
				System.out.println("Task successfully added with delay at processor "+targetProcessorIndex+" : "+n.getId());
				processors[targetProcessorIndex].addTaskSpecificTime(n, Math.min(minStart, processors[targetProcessorIndex].getEndTime()));
				taskList.add(n);
				tasksLeft.remove(n);
				return true;
			}
		} else {
			// Task not added.
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
	
	public String stringData() {
		String data = "  ";
		for(int i = 0; i < numProcessors; i++) {
			data += "{PROCESSOR: "+i+"}";
			for(Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
				data += "|Node "+n.getId()+"| Start time:"+processors[i].mapOfTasksAndStartTimes.get(n);
			}
		}
		
		return data+"   ";
	}
}
