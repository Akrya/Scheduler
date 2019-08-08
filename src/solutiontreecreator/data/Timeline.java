package solutiontreecreator.data;

import java.util.HashMap;

import org.graphstream.graph.Node;

/**
 * 
 * Represents a processor task timeline
 * @author Terence
 *
 */
public class Timeline {
	
	public final HashMap<Node, Double> mapOfTasksAndStartTimes;
	private double endTime;
	
	public Timeline() {
		mapOfTasksAndStartTimes = new HashMap<Node, Double>();
	}
	
	/**
	 * Adds a task to the timeline
	 * @param task
	 * @param offset
	 */
	public void addTask(Node task) {
		mapOfTasksAndStartTimes.put(task, endTime);
		endTime += (double)task.getAttribute("Weight");
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
}
