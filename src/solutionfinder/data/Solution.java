package solutionfinder.data;

import main.graph.TaskGraph;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a solution, a series of tasks scheduled amongst multiple processors.
 *
 * @author Terence Qu
 */
public class Solution implements Serializable {

	private int numProcessors;
	private Processor[] processors;
	private int currentProcessor;

	private TaskGraph taskGraph;
	private List<Node> taskList;
	private List<Node> tasksLeft;
	private Node latestTask;

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
			latestTask = n;
			return true;
		} else {
			// System.out.println("Task unsuccessfully added at processor "+targetProcessorIndex+" : "+n.getId());
			return false;
		}

	}

	/**
	 * Get the earliest data ready time.
	 * @param n
	 * @return
	 */
	public double getMinimumDataReadyTime(Node n){
		List<Edge> dependencyLinks = new ArrayList<>(n.getEnteringEdgeSet());

		double minDataReadyTime = Double.POSITIVE_INFINITY;
		// Calculate latest dependency end time
		for(int targetProcessorIndex = 0; targetProcessorIndex < processors.length; targetProcessorIndex++){
			double latestTime = 0;
			for(int i = 0; i < processors.length; i++) {
				for(Edge dependencyLink: dependencyLinks) {
					double tempTime = 0;
					if(processors[i].mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode())!=null) {
						tempTime = processors[i].mapOfTasksAndStartTimes.get(dependencyLink.getSourceNode()) + (double)dependencyLink.getSourceNode().getAttribute("Weight");
						// Time on another processor
						if(targetProcessorIndex != i){
							tempTime += (double)dependencyLink.getAttribute("Weight");
						}
						if(tempTime > latestTime) {
							latestTime = tempTime;
						}
					}
				}
			}
			latestTime = Math.max(latestTime, processors[targetProcessorIndex].getEndTime());

			if(latestTime < minDataReadyTime){
				minDataReadyTime = latestTime;
			}
		}

		return minDataReadyTime;
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

	public Node getLatestTask(){
		return latestTask;
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
				data += "\n|Node "+n.getId()+"| Start time:"+processors[i].mapOfTasksAndStartTimes.get(n);
			}
			data+="\n";
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
		double time = 0;
		for(Processor p: this.getProcessors()){
			time += p.getEndTime();
		}
		time -= getProcessingTime();
		return time;
	}

	/**
	 * Returns the total sum of time spent with processors being active.
	 * @return
	 */
	public double getProcessingTime(){
		double time = 0;
		for(Processor p: this.getProcessors()){
			for(Node task: p.mapOfTasksAndStartTimes.keySet()){
				time += (double)task.getAttribute("Weight");
			}
		}
		return time;
	}

	/**
	 * Calculates the heuristic value of the encased solution.
	 * This needs to be an underestimate.
	 * We are using a proposed f(s) function in a University of Auckland research paper by Oliver Sinnen.
	 */
	public double getHeuristic(){
		List<Double> heuristicCandidates = new ArrayList<>();

		heuristicCandidates.add(calculateFIdleTime());
		heuristicCandidates.add(calculateFBottomLevel());
		heuristicCandidates.add(calculateFDataReadyTime());

		return Collections.max(heuristicCandidates);
	}

	// All heuristic methods

	/**
	 * Calculate f-idle-time(s) for this solution.
	 * @return
	 */
	public double calculateFIdleTime(){
		return (this.getProcessingTime()+this.getIdleTime())/this.getNumProcessors();
	}

	/**
	 * Calculate f-bl(s) for this solution.
	 * @return
	 */
	public double calculateFBottomLevel(){
		List<Double> bottomLevelCandidates = new ArrayList<>();

		for(Node n: this.getTaskList()){
			double bottomLevel = 0;
			for(Processor p : processors){
				if(p.mapOfTasksAndStartTimes.containsKey(n)){
					bottomLevel = this.taskGraph.nodesAndBottomLevels.get(n)+p.mapOfTasksAndStartTimes.get(n);
					bottomLevelCandidates.add(bottomLevel);
				}
			}
		}

		if(bottomLevelCandidates.isEmpty()){
			return 0;
		} else {
			return Collections.max(bottomLevelCandidates);
		}
	}

	/**
	 * Calculate f-DRT(s) for this solution.
	 * @return
	 */
	public double calculateFDataReadyTime(){
		List<Double> dataReadyTimeCandidates = new ArrayList<>();

		for(Node n: this.getTasksLeft()){
			List<Edge> dependencyLinks = new ArrayList<>(n.getEnteringEdgeSet());

			// Create dependency node list
			List<Node> dependencyTasks = new ArrayList<Node>();
			for(Edge dependency: dependencyLinks) {
				dependencyTasks.add(dependency.getSourceNode());
			}

			if(!taskList.containsAll(dependencyTasks)) {
				double dataReadyTime = this.getMinimumDataReadyTime(n)+this.taskGraph.nodesAndBottomLevels.get(n);
				dataReadyTimeCandidates.add(dataReadyTime);
			}
		}

		if(dataReadyTimeCandidates.isEmpty()){
			return 0.0;
		} else {
			return Collections.max(dataReadyTimeCandidates);
		}
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

	/**
	 * Two tasks are equal if all of their tasks and start times are equal.
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o){
		Solution other = (Solution) o;

		PriorityQueue<Map.Entry<Node, Double>> tasksForThis = new PriorityQueue<>(100, new Comparator<Map.Entry<Node, Double>>() {
			@Override
			public int compare(Map.Entry<Node, Double> e1, Map.Entry<Node, Double> e2) {
				if(e1.getValue() > e2.getValue()){
					return 1;
				} else if (e1.getValue() < e2.getValue()){
					return -1;
				} else {
					return e1.getKey().getId().compareTo(e2.getKey().getId());
				}
			}
		});

		PriorityQueue<Map.Entry<Node, Double>> tasksForOther = new PriorityQueue<>(100, new Comparator<Map.Entry<Node, Double>>() {
			@Override
			public int compare(Map.Entry<Node, Double> e1, Map.Entry<Node, Double> e2) {
				if(e1.getValue() > e2.getValue()){
					return 1;
				} else if (e1.getValue() < e2.getValue()){
					return -1;
				} else {
					return e1.getKey().getId().compareTo(e2.getKey().getId());
				}
			}
		});

		// Add tasks to tasksForThis
		for(Processor pThis: this.processors){
			tasksForThis.addAll(pThis.mapOfTasksAndStartTimes.entrySet());
		}

		// Add tasks to tasksForOther
		for(Processor pOther: other.processors){
			tasksForOther.addAll(pOther.mapOfTasksAndStartTimes.entrySet());
		}

		if(tasksForThis.size() != tasksForOther.size()){
			return false;
		}

		// Compare each entry in tasksForThis and tasksForOther
		while(!tasksForOther.isEmpty()){
			Map.Entry<Node, Double> thisTask = tasksForThis.remove();
			Map.Entry<Node, Double> otherTask = tasksForOther.remove();

			if(!(thisTask.getValue().equals(otherTask.getValue()) && thisTask.getKey().equals(otherTask.getKey()))){
				return false;
			}
		}
		return true;
	}
}