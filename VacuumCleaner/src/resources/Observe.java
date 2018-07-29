package resources;

public enum Observe {
	// This enum holds all the places that the robot can observe.
	// The prefixes L, C and R are used to modify the "up" and "down"
	//	sensors, respectively when we look to the Left, Centre or Right.
	LUP (-1, -1),
	CUP (0, -1),
	RUP (1, -1),
	LEFT (-1, 0),
	CENTRE (0, 0),
	RIGHT (0, 0),
	LDOWN (-1, 1),
	CDOWN (0, 1),
	RDOWN (1, 1);
	
	final int dx;
	final int dy;
	Observe(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
