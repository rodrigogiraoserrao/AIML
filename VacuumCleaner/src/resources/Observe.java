package resources;

import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Observe {
	/* This enum holds all the places that the robot can observe.
	 * The prefixes L, C and R are used to modify the "up" and "down"
	 * 	sensors, respectively when we look to the Left, Centre or Right */
	LUP (-1, -1),
	CUP (0, -1),
	RUP (1, -1),
	LEFT (-1, 0),
	CENTRE (0, 0),
	RIGHT (0, 0),
	LDOWN (-1, 1),
	CDOWN (0, 1),
	RDOWN (1, 1);
	
	public final int dx;
	public final int dy;
	private static final Random R = new Random();
	private static final List<Observe> OBSERVES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = OBSERVES.size();
	
	Observe(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public static Observe getRandomObserve() {
		// https://stackoverflow.com/questions/1972392/java-pick-a-random-value-from-an-enum
		return OBSERVES.get(R.nextInt(SIZE));
	}
}
