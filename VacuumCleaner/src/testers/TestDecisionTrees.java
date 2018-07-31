package testers;

import resources.DecisionTree;

public class TestDecisionTrees {

	public static void main(String[] args) {
		//TestDecisionTrees.testRandomGeneration();
		TestDecisionTrees.testMutations();
	}
	
	public static void testRandomGeneration() {
	/* Generate some random trees and print them. */
		int N = 10;
		for (int i = 0; i < N; ++i) {
			System.out.println(i);
			DecisionTree t = DecisionTree.generateRandomTree();
			System.out.println(t);
		}
	}
	
	public static void testMutations() {
	/* Generate random trees and try to mutate them until a mutation is found. */
		int count = 0;
		boolean foundMutation = false;
		DecisionTree t;
		String s = "";
		while (!foundMutation) {
			++count;
			t = DecisionTree.generateRandomTree();
			s = t.toString();
			if (t.mutate()) {
				foundMutation = true;
				System.out.println(count);
				System.out.println(s);
				System.out.println(t);
			}
		}
	}

}
