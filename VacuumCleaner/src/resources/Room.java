package resources;

import java.util.Random;

public class Room {
	private static final Random R = new Random();
	private double[][] grid;		// level of dirt; 1 is completely filthy
	private int width, height;
	
	public Room(int w, int h) {
		this.grid = new double[w][h];
		this.width = w;
		this.height = h;
		
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j) {
				this.grid[i][j] = Room.R.nextDouble();
			}
		}
	}
	
	public double get(int x, int y) {
		return this.grid[x][y];
	}
	
	public void set(int x, int y, double c) {
		this.grid[x][y] = c;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
