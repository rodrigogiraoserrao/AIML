package resources;

public class Room {
	float[][] grid;
	
	public Room(int w, int h) {
		this.grid = new float[w][h];
		
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				this.grid[i][j] = (float) Math.random();
			}
		}
	}
	
	public float get(int i, int j) {
		return this.grid[i][j];
	}
}
