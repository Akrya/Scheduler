package solutiontreecreator.data;

/**
 * 
 * Represents a solution tree.
 * 
 * @author Terence
 *
 */
public class SolutionTree {
	
	public final int numProcessors;
	public SolutionNode root;
	
	public SolutionTree(int numProcessors) {
		this.numProcessors = numProcessors;
	}
	
}