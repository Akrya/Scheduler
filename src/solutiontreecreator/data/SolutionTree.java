package solutiontreecreator.data;

/**
 * 
 * Represents a solution tree.
 * 
 * @author Teren
 *
 */
public class SolutionTree {
	
	public final int numProcessors;
	public SolutionNode root;
	
	public SolutionTree(int numProcessors) {
		this.numProcessors = numProcessors;
	}
	
}