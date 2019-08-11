package solutionfinder;

import java.util.ArrayList;
import java.util.List;

import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;

/**
 * A class with static methods that allow for the search of an optimal solution within a solution tree.
 * 
 * @author Terence Qu
 */
public class BasicSolutionFinder {
	
	public static List<Solution> solutions = new ArrayList<Solution>();
	public static double minCost = Double.POSITIVE_INFINITY;
	
	/**
	 * A brute force search through the solution tree.
	 * @param rootOfTree Root of the solution tree to search through.
	 * @return
	 */
	public static List<Solution> findOptimalSolution(SolutionNode rootOfTree) {
		solutions = new ArrayList<Solution>();
		
		searchTree(rootOfTree);
		minCost = Double.POSITIVE_INFINITY;
		
		return solutions;
	}

	/**
	 * Recursive brute force search algorithm.
	 * @param rootOfTree
	 */
	private static void searchTree(SolutionNode rootOfTree) {
		for(SolutionNode node: rootOfTree.children) {
			if(node.isSolutionComplete()) {
				if(node.solution.getTotalTime() < minCost) {
//					System.out.println("Found a better solution with time "+node.solution.getTotalTime());
					minCost = node.solution.getTotalTime();
					solutions.clear();
					solutions.add(node.solution);
				} else if(node.solution.getTotalTime() == minCost) {
//					System.out.println("Found a similar solution with time "+node.solution.getTotalTime());
					solutions.add(node.solution);
				}
			}
		}
		
		for(SolutionNode node: rootOfTree.children) {
			searchTree(node);
		}
	}
	
	
}
