package resources;

import java.io.Serializable;
import java.util.Random;

public class DecisionTree implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Random R = new Random();		// random generator
	private static final double MUTATION_RATE = 0.01;	// rate at which nodes mutate
	private static final double GROWTH_RATE = 0.1;		// rate at which leafs grow, assuming they will mutate
	private static final double AMPUTATION_RATE = 0.03;	// rate at which trees become leafs, assuming they will mutate
	public Move action;			// tree with only an "action" node
	public Observe lookAt;			// where to look at to decide how to act
	public DecisionTree lt, rt;	// left and right branches
	public double threshold;		// threshold for the binary decision tree
	
	public DecisionTree(Observe lookAt, double threshold, DecisionTree lt, DecisionTree rt) {
		this.lookAt = lookAt;
		this.threshold = threshold;
		this.lt = lt;
		this.rt = rt;
		this.action = null;
	}
	
	public DecisionTree(Move action) {
		this.action = action;
		this.lookAt = null;
		this.lt = null;
		this.rt = null;
	}
	
	public static DecisionTree generateRandomTree() {
		return DecisionTree.generateRandomTree(1);
	}
	
	private static DecisionTree generateRandomTree(int lvl) {
		if (R.nextDouble() < 1./(Math.pow(2., lvl))) {
			// branch this tree out
			DecisionTree lt = DecisionTree.generateRandomTree(lvl + 1);
			DecisionTree rt = DecisionTree.generateRandomTree(lvl + 1);
			double threshold = R.nextDouble();
			Observe lookAt = Observe.getRandomObserve();
			return new DecisionTree(lookAt, threshold, lt, rt);
		} else {
			// the tree ends here
			return new DecisionTree(Move.getRandomMove());
		}
	}
	
	public boolean mutate() {
		boolean flag = false;
		if (DecisionTree.R.nextDouble() < DecisionTree.MUTATION_RATE) {
			flag = true;
			// mutate this node; is it a leaf or not?
			if (this.action != null) {	// leaf node
				if (DecisionTree.R.nextDouble() < DecisionTree.GROWTH_RATE) {
					this.lt = DecisionTree.generateRandomTree();
					this.rt = DecisionTree.generateRandomTree();
					this.threshold = R.nextDouble();
					this.lookAt = Observe.getRandomObserve();
					this.action = null;
				} else {
					Move old = this.action;
					do {
						this.action = Move.getRandomMove();
					} while (old == this.action);
				}
			}
			else {	// regular node
				if (DecisionTree.R.nextDouble() < DecisionTree.AMPUTATION_RATE) { // amputate the subtree
					this.threshold = 0;
					this.lookAt = null;
					this.lt = null;
					this.rt = null;
					this.action = Move.getRandomMove();
				} else { // do not amputate, just change something
					double r = DecisionTree.R.nextDouble();
					if (r < 1./3) {	// new threshold
						this.threshold = DecisionTree.R.nextDouble();
					} else if (r < 2./3) { // new observation cell
						Observe old = this.lookAt;
						do {
							this.lookAt = Observe.getRandomObserve();
						} while (old == this.lookAt);
					} else { // swap left with right subtrees
						DecisionTree temp = this.lt;
						this.lt = this.rt;
						this.rt = temp;
					}
				}
			}
		}
		// try to mutate the children as well
		if (this.action == null) {
			boolean f1 = this.lt.mutate();
			boolean f2 = this.rt.mutate();
			return flag || f1 || f2;
		} else {
			return flag;
		}
	}
	
	public DecisionTree getCopy() {
		if (this.action != null) {
			return new DecisionTree(this.action);
		} else {
			return new DecisionTree(this.lookAt, this.threshold, this.lt.getCopy(), this.rt.getCopy());
		}
	}
	
	public String toString() {
		return this.toString(0);
	}
	
	private String toString(int lvl) {
		String tabs = "";
		for (int i = 0; i < lvl; ++i) tabs = tabs + "    ";
		if (this.action != null) {
			return tabs + "go to " + this.action + "\n";
		} else {
			return tabs + "look at " + this.lookAt + " (" + this.threshold + ")\n" +
						this.lt.toString(lvl+1) + this.rt.toString(lvl+1);
		}
	}
}
