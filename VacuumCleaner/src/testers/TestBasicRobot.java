package testers;

import processing.core.PApplet;
import resources.*;

public class TestBasicRobot extends PApplet {
	static final int CELLSIZE = 20;
	static final int WIDTH = 3;
	static final int HEIGHT = 20;
	private Room room;
	private Robot robot;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("testers.TestBasicRobot");
	}
	
	public void settings() {
		
	}
	
	public void setup() {
		this.room = new Room(TestBasicRobot.WIDTH, TestBasicRobot.HEIGHT);
		for (int i = 0; i < TestBasicRobot.WIDTH; ++i) {
			for (int j = 0; j < TestBasicRobot.HEIGHT; ++j) {
				this.room.set(i, j, 1);
			}
		}
		DecisionTree t = new DecisionTree(Move.UP);
		this.robot = new Robot(t);
		this.robot.setRoom(this.room);
		surface.setSize(TestBasicRobot.CELLSIZE*TestBasicRobot.WIDTH, TestBasicRobot.CELLSIZE*TestBasicRobot.HEIGHT);
	}
	
	public void draw() {
		// move the robot
		this.robot.move();
		// clean the dirt
		this.robot.clean();
		// draw the room
		for (int i = 0; i < TestBasicRobot.WIDTH; ++i) {
			for (int j = 0; j < TestBasicRobot.HEIGHT; ++j) {
				int c = (int)(255*(1-this.robot.get(i, j)));
				fill(c);
				rect(i*TestBasicRobot.CELLSIZE, j*TestBasicRobot.CELLSIZE, TestBasicRobot.CELLSIZE, TestBasicRobot.CELLSIZE);
			}
		}
		fill(255, 0, 0);
		rect(this.robot.getXpos()*TestBasicRobot.CELLSIZE, this.robot.getYpos()*TestBasicRobot.CELLSIZE,
				TestBasicRobot.CELLSIZE, TestBasicRobot.CELLSIZE);
		
		// delay the next draw
		delay(300);
	}

}
