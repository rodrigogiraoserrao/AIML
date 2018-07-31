package resources;

public class Robot {
	private DecisionTree tree;
	private int xpos, ypos;
	private int roomWidth, roomHeight;
	private double room[][];
	private double score;
	
	public Robot(DecisionTree t) {
		this.tree = t;
		this.score = 0;
	}
	
	public int getXpos() {
		return this.xpos;
	}
	
	public int getYpos() {
		return this.ypos;
	}
	
	public double get(int x, int y) {
		return this.room[x][y];
	}
	
	public double getScore() {
		return this.score;
	}
	
	public void resetScore() {
		this.score = 0;
	}
	
	public DecisionTree getTree() {
		return this.tree;
	}
	
	public void setRoom(Room r) {
		this.roomWidth = r.getWidth();
		this.roomHeight = r.getHeight();
		this.room = new double[this.roomWidth][this.roomHeight];
		for (int i = 0; i < this.roomWidth; ++i) {
			for (int j = 0; j < this.roomHeight; ++j) {
				this.room[i][j] = r.get(i, j);
			}
		}
		this.xpos = this.roomWidth/2;
		this.ypos = this.roomHeight/2;
	}
	
	public Move move() {
	/* Move the robot following its decision tree.*/
		Move m = this.traverseTree(this.tree);
		int dx = m.dx, dy = m.dy;
		int x = this.xpos + dx, y = this.ypos + dy;
		
		if (x < 0 || x >= this.roomWidth || y < 0 || y >= this.roomHeight) m = Move.QUIET;
		else {
			this.xpos = x;
			this.ypos = y;
		}
		
		return m;
	}
	
	private Move traverseTree(DecisionTree t) {
	/* Traverse the decision tree, looking for the correct move to make.*/
		if (t.action != null) return t.action;
		else {
			int dx = t.lookAt.dx, dy = t.lookAt.dy;
			int x = this.xpos + dx, y = this.ypos + dy;
			// if we are trying to look outside the room, i.e. if we are observing a wall,
			// we observe a 0, for "completely clean"
			double observed;
			if (x < 0 || x >= this.roomWidth || y < 0 || y >= this.roomHeight) observed = 0;
			else observed = this.room[x][y];
			
			if (observed <= t.threshold) return this.traverseTree(t.lt);
			else return this.traverseTree(t.rt);
		}
	}
	
	public double clean() {
		this.score += this.room[this.xpos][this.ypos];
		this.room[this.xpos][this.ypos] = 0;
		return this.score;
	}
}
