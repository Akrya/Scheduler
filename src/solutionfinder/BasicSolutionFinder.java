package solutionfinder;

import java.util.ArrayList;
import java.util.List;

import solutiontreecreator.data.Solution;
import solutiontreecreator.data.SolutionNode;

public class BasicSolutionFinder {
	
	public static List<Solution> solutions = new ArrayList<Solution>();
	public static double minCost = 10000000;
	
	public static List<Solution> findOptimalSolution(SolutionNode rootOfTree) {
		solutions = new ArrayList<Solution>();
		
		searchTree(rootOfTree);
		
		return solutions;
	}

	private static void searchTree(SolutionNode rootOfTree) {
		for(SolutionNode node: rootOfTree.children) {
			if(node.isSolutionComplete()) {
				if(node.solution.getTotalTime() < minCost) {
					System.out.println("Found a better solution with time "+node.solution.getTotalTime());
					minCost = node.solution.getTotalTime();
					solutions.clear();
					solutions.add(node.solution);
				} else if(node.solution.getTotalTime() == minCost) {
					System.out.println("Found a similar solution with time "+node.solution.getTotalTime());
					solutions.add(node.solution);
				}
			}
		}
		
		for(SolutionNode node: rootOfTree.children) {
			searchTree(node);
		}
	}
	
	
}
