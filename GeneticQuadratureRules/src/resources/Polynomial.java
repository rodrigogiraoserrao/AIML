package resources;

import java.util.Random;

public class Polynomial {
	private static final Random R = new Random();
	
	private double[] coefs;
	
	public Polynomial(int deg, double minCoef, double maxCoef) {
		this.coefs = new double[deg+1];
		for (int i = 0; i < deg; ++i) {
			this.coefs[i] = Polynomial.R.nextDouble()*(maxCoef-minCoef) - minCoef;
		}
	}
	
	public Polynomial(double[] coefs) {
		this.coefs = coefs;
	}
	
	public double integrate(double a, double b) {
		return Polynomial.integrate(this.coefs, a, b);
	}
	
	public static double integrate(double[] poly, double a, double b) {
		// integrate the polynomial poly in [a, b]
		double[] primitive = Polynomial.primitive(poly);
		double acca = 0, accb = 0;
		double auxa = 1, auxb = 1;
		for (double coef : primitive) {
			acca += coef*auxa;
			accb += coef*auxb;
			auxa *= a;
			auxb *= b;
		}
		
		return accb - acca;
	}
	
	public static double[] primitive(double[] poly) {
		double[] primitive = new double[poly.length+1];
		for (int i = 0; i < poly.length; ++i) {
			primitive[i+1] = poly[i]/(i+1);
		}
		return primitive;
	}
	
	public double[] getCoefs() {
		double[] c = new double[this.coefs.length];
		for (int i = 0; i < c.length; ++i) c[i] = this.coefs[i];
		return c;
	}
}