package resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Move {
	/* This enum holds all possible moves a robot can make.
	 * He can move in a cross or stand still. */
	UP (0, -1),
	DOWN (0, 1),
	LEFT (-1, 0),
	RIGHT (1, 0),
	QUIET (0, 0);
	
	public final int dx;
	public final int dy;
	private static final Random R = new Random();
	private static final List<Move> MOVES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = MOVES.size();
	
	Move(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public static Move getRandomMove() {
		// https://stackoverflow.com/questions/1972392/java-pick-a-random-value-from-an-enum
		return MOVES.get(R.nextInt(SIZE));
	}
}
