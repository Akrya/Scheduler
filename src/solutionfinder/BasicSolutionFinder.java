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
				if(node.getTotalTime() < minCost) {
					solutions.clear();
					solutions.add(node.solution);
				} else if(node.getTotalTime() == minCost) {
					solutions.add(node.solution);
				}
			}
		}
		
		for(SolutionNode node: rootOfTree.children) {
			searchTree(node);
		}
	}
	
	
}
