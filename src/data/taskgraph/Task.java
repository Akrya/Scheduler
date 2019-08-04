package data.taskgraph;

/**
 * Represents a task. Each task has a label and a duration.
 * 
 * @author Terence Qu
 * 
 */
public class Task {
	private String label;
	private String duration;
	
	public Task(String label, String duration) {
		this.label = label;
		this.duration = duration;
	}
	
	// Getters and setters
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
}
