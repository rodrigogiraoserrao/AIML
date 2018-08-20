package resources;

import java.util.Random;

public class QuadratureRule {
	public static double minWeight = 0;
	public static double maxWeight = 2;
	public static double minNode = -1;
	public static double maxNode = 1;
	public static int maxNodes = 3;
	public static double mutation_rate = 0.05;
	public static double mutation_size = 0.05;
	
	private int size;
	private double[] weights;
	private double[] nodes;
	
	public static final Random R = new Random();
	
	public QuadratureRule() {
		this.size = QuadratureRule.R.nextInt(QuadratureRule.maxNodes) + 1;
		this.weights = new double[this.size];
		this.nodes = new double[this.size];
		
		double nodeRange = QuadratureRule.maxNode - QuadratureRule.minNode;
		double weightRange = QuadratureRule.maxWeight - QuadratureRule.minWeight;
		for (int i = 0; i < this.size; ++i) {
			weights[i] = weightRange*QuadratureRule.R.nextDouble() + QuadratureRule.minWeight;
			nodes[i] = nodeRange*QuadratureRule.R.nextDouble() + QuadratureRule.minNode;
		}
	}
	
	private QuadratureRule(double[] nodes, double[] weights) {
		this.size = nodes.length;
		this.nodes = nodes;
		this.weights = weights;
	}
	
	public boolean mutate() {
		boolean flag = false;
		for (int i = 0; i < this.size; ++i) {
			// mutate this node/weight pair
			if (QuadratureRule.R.nextDouble() < QuadratureRule.mutation_rate) {
				flag = true;
				if (QuadratureRule.R.nextDouble() < 0.5) {
					// mutate the weight
					weights[i] *= 1 + (QuadratureRule.R.nextDouble()*2*mutation_size - mutation_size);
					weights[i] = Math.min(maxWeight, Math.max(minWeight, weights[i]));
				} else {
					nodes[i] *= 1 + (QuadratureRule.R.nextDouble()*2*mutation_size - mutation_size);
					nodes[i] = Math.min(maxNode, Math.max(minNode, nodes[i]));
				}
			}
		}
		return flag;
	}
	
	public double evaluatePolynomial(Polynomial p) {
		return this.evaluatePolynomial(p.getCoefs());
	}
	
	public double evaluatePolynomial(double[] coefs) {
		// evaluate the quadrature rule on the polynomial \sum_i coefs[i]*x^i
		double value = 0;
		double nodePower, acc;
		for (int i = 0; i < this.size; ++i) { // traverse all the nodes
			nodePower = 1;
			acc = 0;
			for (int j = 0; j < coefs.length; ++j) { // evaluate the polynomial on this node
				acc += coefs[j]*nodePower;
				nodePower *= this.nodes[i];
			}
			value += this.weights[i]*acc;
		}
		
		return value;
	}
	
	public static QuadratureRule[] crossover(QuadratureRule A, QuadratureRule B) {
		QuadratureRule[] child = new QuadratureRule[2];
		
		if (A.getSize() > B.getSize()) {
			QuadratureRule t = A;
			A = B;
			B = t;
		} // now A has the smaller quadrature rule
		// ensure every kid has something from each parent
		int cut = QuadratureRule.R.nextInt(A.getSize()+1);
		double[] nodesC = new double[A.getSize()];
		double[] weightsC = new double[A.getSize()];
		double[] nodesD = new double[B.getSize()];
		double[] weightsD = new double[B.getSize()];
		// swap two tails of the same size
		// e.g. [1,2,3,4], [a,b,c,d,e,f] -> [1,2,e,f], [a,b,c,d,3,4]
		for (int i = 0; i < A.getSize(); ++i) {
			if (i < cut) {
				nodesC[i] = A.nodes[i];
				weightsC[i] = A.weights[i];
			} else {
				nodesC[i] = B.nodes[B.getSize()-A.getSize()+i];
				weightsC[i] = B.weights[B.getSize()-A.getSize()+i];
				nodesD[B.getSize()-A.getSize()+i] = A.nodes[i];
				weightsD[B.getSize()-A.getSize()+i] = A.weights[i];
			}
		}
		for (int i = 0; i < B.getSize()-A.getSize()+cut; ++i) {
			nodesD[i] = B.nodes[i];
			weightsD[i] = B.weights[i];
		}
		child[0] = new QuadratureRule(nodesC, weightsC);
		child[1] = new QuadratureRule(nodesD, weightsD);
		
		return child;
	}
	
	public QuadratureRule getCopy() {
		return new QuadratureRule(this.getNodes(), this.getWeights());
	}
	
	public int getSize() {
		return size;
	}

	public double[] getWeights() {
		double[] ws = new double[this.size];
		for (int i = 0; i < this.size; ++i) ws[i] = this.weights[i];
		return ws;
	}

	public double[] getNodes() {
		double[] ns = new double[this.size];
		for (int i = 0; i < this.size; ++i) ns[i] = this.nodes[i];
		return ns;
	}

	public String toString() {
		String str = "";
		for (int i = 0; i < this.size-1; ++i) {
			str += "" + this.weights[i] + "*f(" + this.nodes[i] + ") + ";
		}
		str += "" + this.weights[this.size-1] + "*f(" + this.nodes[this.size-1] + ")";
		
		return str;
	}
}
