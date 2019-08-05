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
	
	public final HashMap<Node, Integer> mapOfTasksAndStartTimes;
	private int endTime;
	
	public Timeline() {
		mapOfTasksAndStartTimes = new HashMap<Node, Integer>();
	}
	
	/**
	 * Adds a task to the timeline
	 * @param task
	 * @param offset
	 */
	public void addTask(Node task) {
		mapOfTasksAndStartTimes.put(task, endTime);
		endTime += (int)task.getAttribute("Weight");
	}
	
	/**
	 * Adds a task with a delay to the timeline
	 * @param task
	 * @param offset
	 */
	public void addTaskWithDelay(Node task, int delay) {
		mapOfTasksAndStartTimes.put(task, endTime+delay);
		endTime += (int)task.getAttribute("Weight")+delay;
	}
}
