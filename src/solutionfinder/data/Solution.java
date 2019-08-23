package solutionfinder.data;

import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import graph.TaskGraph;

/**
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
	 * Adds a task to a processor. If two tasks are on the same processor,
	 * one task must finish before the other starts.
	 * If on different processors, for each dependency, 
	 * start time >= start time of dependency + dependency's weight + edge weight 
	 * @param n task represented as a node in the graph
	 * @param targetProcessorIndex
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
			for(int i = 0; i < processors.length; i++) {
				for(Edge dependencyLink: dependencyLinks) {
					double tempTime = 0;
					if(processors[i].mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode())!=null) {
						tempTime = processors[i].mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode()) + (double)dependencyLink.getSourceNode().getAttribute("Weight");
						// Dependency on another processor
						if(targetProcessorIndex != i){
							tempTime += (double)dependencyLink.getAttribute("Weight");
						}
						if(tempTime > latestTime) {
							latestTime = tempTime;
						}
					}
				}
			}

			processors[targetProcessorIndex].addTaskSpecificTime(n, Math.max(latestTime, processors[targetProcessorIndex].getEndTime()));
			taskList.add(n);
			tasksLeft.remove(n);
			currentProcessor = targetProcessorIndex;
			return true;
		} else {
			// System.out.println("Task unsuccessfully added at processor "+targetProcessorIndex+" : "+n.getId());
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
			System.out.println("- - - - - - - - - - - -");
			System.out.println("{Processor number: "+i+"}");
			System.out.println("- - - - - - - - - - - -");
			for(Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
				System.out.println("Node " + n.getId() + "| Start time:" + processors[i].mapOfTasksAndStartTimes.get(n));
			}
		}
		System.out.println("- - - - - - - - - - - -");
		System.out.println(this.getTotalTime());
		System.out.println("- - - - - - - - - - - -");
	}

	/**
	 * Method for generating a string representation of the solution.
	 * @return string containing the processor, node and start time
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
	
	/**
	 * Find the total time taken for this solution's schedules.
	 * @return the longest time taken by any of the processors
	 */
	public double getTotalTime() {
		double longestTime = 0;
		for(Processor p: this.getProcessors()) {
			if(p.getEndTime() > longestTime) {
				p.getEndTime();
				longestTime = p.getEndTime();
			}
		}
		
		return longestTime;
	}

	/**
	 * Returns the sum of total idle time of each processor.
	 * @return
	 */
	public double getIdleTime(){
		return this.getTotalTime()-this.getProcessingTime();
	}

	/**
	 * Returns the total sum of time spent with processors being active.
	 * @return
	 */
	public double getProcessingTime(){
		double time = 0;
		for(Processor p: this.getProcessors()){
			for(double tvalue: p.mapOfTasksAndStartTimes.values()){
				time += tvalue;
			}
		}
		return time;
	}

	/**
	 * Calculates the heuristic value of the encased solution.
	 * This needs to be an underestimate.
	 */
	public double getHeuristic(){
		// Hieuristic in lectures
		// return ((this.getProcessingTime()+this.getIdleTime())/this.getNumProcessors());
		return this.getProcessingTime()/(this.getTotalTime()*this.getNumProcessors());
	}

	/**
	 * Get number of potential children from this solution, if expanded fully.
	 * @return Number of potential children.
	 */
	public long getNumberOfChildren(){
		int totalTaskNum = this.getTaskList().size()+this.getTasksLeft().size();
		long num = 0;
		for(int i = 0; i < this.getTasksLeft().size(); i++){
			num += (totalTaskNum*numProcessors)^(i+1);
		}
		return num;
	}
}
