package solutiontreecreator.data;

import java.util.HashMap;

import org.graphstream.graph.Node;

/**
 * 
 * Represents a processor task timeline
 * @author Terence
 *
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
		calculateEndTime();
	}
	
	public void addTaskSpecificTime(Node task, double time) {
		mapOfTasksAndStartTimes.put(task, time);
		calculateEndTime();
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
	
	/**
	 * Helper function to update the end time.
	 */
	private void calculateEndTime() {
		double maxTime = 0;
		for(Node n: mapOfTasksAndStartTimes.keySet()) {
			double tempTime = mapOfTasksAndStartTimes.get(n) + (double)n.getAttribute("Weight");
			if(tempTime > maxTime) {
				maxTime = tempTime;
			}
		}
		
		endTime = maxTime;
	}
	
	public double getEndTime() {
		return endTime;
	}
}
