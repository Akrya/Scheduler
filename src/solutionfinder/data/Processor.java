package solutionfinder.data;

import java.util.HashMap;
import org.graphstream.graph.Node;

/**
 * Class that represents a processor. This contains a set of scheduled tasks in a specific order.
 * 
 * @author Terence Qu
 */
public class Processor {
	
	public final HashMap<Node, Double> mapOfTasksAndStartTimes;
	private double endTime;

	/**
	 * Constructor for Processor
	 * Creates a mapping for every node on the processor to the start time
	 */
	public Processor() {
		mapOfTasksAndStartTimes = new HashMap<Node, Double>();
		endTime = 0;
	}
	
	/**
	 * Adds a task to the timeline.
	 * @param task the task represented by a node
	 */
	public void addTask(Node task) {
		mapOfTasksAndStartTimes.put(task, endTime);
		endTime += (double) task.getAttribute("Weight");
	}
	
	/**
	 * Adds a task to the timeline at a specific time interval.
	 * @param task the task represented by a node
	 * @param time the time at which the task begins
	 */
	public void addTaskSpecificTime(Node task, double time) {
		mapOfTasksAndStartTimes.put(task, time);
		endTime = time + (double)task.getAttribute("Weight");
	}
	
	/**
	 * Adds a task with a delay to the timeline.
	 * @param task the task represented by a node
	 * @param delay delay before starting task
	 */
	public void addTaskWithDelay(Node task, double delay) {
		mapOfTasksAndStartTimes.put(task, endTime+delay);
		endTime += (double)task.getAttribute("Weight")+delay;
	}
	
	/**
	 * Gets the ending time of this processor.
	 * @return Time interval which all tasks on this processor have been completed.
	 */
	public double getEndTime() {
		return endTime;
	}

	/**
	 * Sets the ending time of this processor.
	 * @param endTime Time interval which all tasks on this processor have been completed.
	 */
	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
}
