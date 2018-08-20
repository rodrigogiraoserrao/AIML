package simulators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import resources.Polynomial;
import resources.QuadratureRule;

public class GaussSimulator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<QuadratureRule> pop = new ArrayList<QuadratureRule>();
		ArrayList<Double> scores = new ArrayList<Double>();
		ArrayList<Holder> holders = new ArrayList<Holder>();
		ArrayList<Polynomial> polys = new ArrayList<Polynomial>();
		ArrayList<Double> integrals = new ArrayList<Double>();
		int popSize = 1000;
		int degree = 5;
		
		// populate the population of QRs
		for (int i = 0; i < popSize; ++i) {
			pop.add(new QuadratureRule());
			System.out.println(pop.get(i));
		}
		// create the polynomials of all degrees up to the max
		for (int i = 0; i <= degree; ++i) {
			double[] poly = new double[i+1];
			poly[i] = 1;
			polys.add(new Polynomial(poly));
			integrals.add(1/(i+1.));
		}
		
		for (int i = 0; i < 10000; ++i) {
			// populate the polynomials and integrate them
			// reset scores and holders
			scores.clear();
			holders.clear();
			// evaluate each quadrature rule in each polynomial
			double val = 0;
			QuadratureRule qr;
			for (int q = 0; q < popSize; ++q) {
				qr = pop.get(q);
				scores.add(0.);
				for (int p = 0; p < polys.size(); ++p) {
					val = qr.evaluatePolynomial(polys.get(p));
					scores.set(q, scores.get(q) + Math.abs(val - integrals.get(p)));
				}
				holders.add(new Holder(qr.getCopy(), scores.get(q)));
			}
			// sort everything
			Collections.sort(holders, new HolderComparator());
			// print the best score and rule
			System.out.println(holders.get(0).score);
			// take the best third, produce offspring and mutate it
			int cap = (int)(0.45*popSize);
			pop.clear();
			QuadratureRule qr1 = null, qr2 = null;
			for (int j = 0; j < cap; ++j) {
				qr2 = qr1;
				qr1 = holders.get(j).qr;
				while (!qr1.mutate());
				pop.add(qr1);
				
				if (j % 2 == 1) {
					// we added two more QRs so we can reproduce them
					QuadratureRule[] child = QuadratureRule.crossover(qr1, qr2);
					pop.add(child[0]);
					pop.add(child[1]);
				}
			}
			while (pop.size() < popSize) {
				pop.add(new QuadratureRule());
			}
		}
	}

}

class Holder {
	QuadratureRule qr;
	double score;
	public Holder(QuadratureRule qr, double score) {
		this.qr = qr;
		this.score = score;
	}
	public String toString() {
		return "<"+score+","+qr.toString()+">";
	}
}

class HolderComparator implements Comparator<Holder> {
	public int compare(Holder h1, Holder h2) {
		if (h1.score < h2.score) {
			return -1;
		} else if (h1.score == h2.score) {
			return 0;
		} else {
			return 1;
		}
	}
}