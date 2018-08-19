package resources;

import processing.core.PApplet;

public class WatchRobotAnimation extends PApplet {
	/* Class to watch a robot animation */
	private static Robot robot;
	private static int CELLSIZE = 20;
	private static int WIDTH;
	private static int HEIGHT;
	private boolean drawn;
	
	public static void watchAnimation(Robot robot, Room room) {
		WatchRobotAnimation.robot = robot;
		WatchRobotAnimation.robot.setRoom(room);
		WatchRobotAnimation.WIDTH = room.getWidth();
		WatchRobotAnimation.HEIGHT = room.getHeight();
		PApplet.main("resources.WatchRobotAnimation");
	}
	
	public void settings() {
		
	}
	
	public void setup() {
		surface.setSize(WatchRobotAnimation.CELLSIZE*WatchRobotAnimation.WIDTH,
						WatchRobotAnimation.CELLSIZE*WatchRobotAnimation.HEIGHT);
	}
	
	private void draw_board() {
		// draw the room
		for (int i = 0; i < WatchRobotAnimation.WIDTH; ++i) {
			for (int j = 0; j < WatchRobotAnimation.HEIGHT; ++j) {
				int c = (int)(255*(1-WatchRobotAnimation.robot.get(i, j)));
				fill(c);
				rect(i*WatchRobotAnimation.CELLSIZE, j*WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.CELLSIZE);
			}
		}
	}
	
	public void draw() {
		if (! this.drawn) {
			this.drawn = true;
			this.draw_board();
		}
		fill(255, 255, 255);
		rect(WatchRobotAnimation.robot.getXpos()*WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.robot.getYpos()*WatchRobotAnimation.CELLSIZE,
				WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.CELLSIZE);
		// move the robot
		WatchRobotAnimation.robot.move();
		// clean the dirt
		WatchRobotAnimation.robot.clean();
		// draw the room
		fill(255, 0, 0);
		rect(WatchRobotAnimation.robot.getXpos()*WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.robot.getYpos()*WatchRobotAnimation.CELLSIZE,
				WatchRobotAnimation.CELLSIZE, WatchRobotAnimation.CELLSIZE);
		
		// delay the next frame
		delay(50);
	}
}
