package resources;

public enum Move {
	// This enum holds all possible moves a robot can make.
	// He can move in a cross or stand still.
	UP (0, -1),
	DOWN (0, 1),
	LEFT (-1, 0),
	RIGHT (1, 0),
	QUIET (0, 0);
	
	final int dx;
	final int dy;
	Move(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
