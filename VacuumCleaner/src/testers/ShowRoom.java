package testers;

import resources.Room;
import processing.core.PApplet;

public class ShowRoom extends PApplet {
	static final int CELLSIZE = 40;
	static final int WIDTH = 10;
	static final int HEIGHT = 10;
	Room r;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("testers.ShowRoom");
	}
	
	public void settings() {
		
	}
	
	public void setup() {
		this.r = new Room(ShowRoom.WIDTH, ShowRoom.HEIGHT);
		surface.setSize(ShowRoom.CELLSIZE*ShowRoom.WIDTH, ShowRoom.CELLSIZE*ShowRoom.HEIGHT);
	}
	
	public void draw() {
		for (int i = 0; i < ShowRoom.WIDTH; ++i) {
			for (int j = 0; j < ShowRoom.HEIGHT; ++j) {
				fill((int)(255*(1-this.r.get(i,j))));
				rect(i*ShowRoom.CELLSIZE, j*ShowRoom.CELLSIZE, ShowRoom.CELLSIZE, ShowRoom.CELLSIZE);
			}
		}
	}

}
