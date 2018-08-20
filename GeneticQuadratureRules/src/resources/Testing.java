package resources;

import java.util.Arrays;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QuadratureRule qr = new QuadratureRule();
		QuadratureRule qr2 = new QuadratureRule();
		QuadratureRule[] q = QuadratureRule.crossover(qr, qr2);
		System.out.println(qr);
		System.out.println(qr2);
		System.out.println(q[0]);
		System.out.println(q[1]);
		
		System.out.println(Arrays.toString(Polynomial.primitive(new double[] {1,1,1})));
		
		System.out.println(Polynomial.integrate(new double[] {1}, 0, 1));
		System.out.println(Polynomial.integrate(new double[] {0,1}, 0, 1));
		System.out.println(Polynomial.integrate(new double[] {0,1}, -1, 1));
		System.out.println(Polynomial.integrate(new double[] {0,0,1}, 0, 1));
		System.out.println(Polynomial.integrate(new double[] {0,0,1}, -1, 1));
	}

}
