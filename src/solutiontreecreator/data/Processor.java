package solutiontreecreator.data;

import java.util.HashMap;

import org.graphstream.graph.Node;

/**
 * 
 * Class that represents a processor. This contains a set of scheduled tasks in a specific order.
 * 
 * @author Terence Qu
 */
public class Processor {
	
	public final HashMap<Node, Double> mapOfTasksAndStartTimes;
	private double endTime;
	
	public Processor() {
		mapOfTasksAndStartTimes = new HashMap<Node, Double>();
		endTime = 0;
	}
	
	/**
	 * Adds a task to the timeline
	 * @param task
	 * @param offset
	 */
	public void addTask(Node task) {
		mapOfTasksAndStartTimes.put(task, endTime);
		endTime += (double) task.getAttribute("Weight");
	}
	
	public void addTaskSpecificTime(Node task, double time) {
		mapOfTasksAndStartTimes.put(task, time);
		endTime = time + (double)task.getAttribute("Weight");
	}
	
	/**
	 * Adds a task with a delay to the timeline
	 * @param task
	 * @param offset
	 */
	public void addTaskWithDelay(Node task, double delay) {
		mapOfTasksAndStartTimes.put(task, endTime+delay);
		endTime += (double)task.getAttribute("Weight")+delay;
	}
	
	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
}
