package simulators;

import resources.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import processing.core.PApplet;

public class BasicSimulator extends PApplet {
	private static final int ROOMWIDTH = 20;
	private static final int ROOMHEIGHT = 20;
	private static int STEPS = 400;
	private static final int NROBOTS = 100;
	private static final int NROOMS = 100;
	private static final int NGENS = 100;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// create a logging file
		FileWriter fw;
		PrintWriter out;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
			LocalDateTime now = LocalDateTime.now();
			fw = new FileWriter("logs/"+dtf.format(now)+".log");
			out = new PrintWriter(fw);
		} catch (IOException e) {
			// default to writing to the console
			e.printStackTrace();
			out = new PrintWriter(System.out);
		}
		
		ArrayList<Robot> robots = new ArrayList<Robot>();
		for (int i = 0; i < BasicSimulator.NROBOTS; ++i) {
			DecisionTree tree = DecisionTree.generateRandomTree();
			robots.add(new Robot(tree));
		}

		ArrayList<Room> rooms = new ArrayList<Room>();
		
		for (int n = 0; n < BasicSimulator.NGENS; ++n) {
			// increment the available number of steps
			//if (n % 5 == 0) BasicSimulator.STEPS += 5;
			
			rooms.clear();
			for (int i = 0; i < BasicSimulator.NROOMS; ++i) {
				rooms.add(new Room(BasicSimulator.ROOMWIDTH, BasicSimulator.ROOMHEIGHT));
			}
			for (Robot r: robots) {
				for (Room room: rooms) {
					r.setRoom(room);
					r.clean();
					for (int i = 0; i < BasicSimulator.STEPS; ++i) {
						r.move();
						r.clean();
					}
				}
			}
			// the robots have cleaned, sort them out
			Collections.sort(robots, new RobotComparator());
			// print the best scoring robot
			out.println("Gen " + n + ": score = " + robots.get(0).getScore());
			// leave the best 45% percent alone,
			// create another 45% with mutations from the first
			// create new, random 10%
			int cap = (int)(0.45*BasicSimulator.NROBOTS);
			for (int i = 0; i < cap; ++i) {
				// copy the tree; reset score
				DecisionTree t = robots.get(i).getTree().getCopy();
				robots.get(i).resetScore();
				// mutate it
				while (!t.mutate());
				// create a new robot and save it
				robots.set(i+cap, new Robot(t));
			}
			for (int i = 2*cap; i < BasicSimulator.NROBOTS; ++i) {
				DecisionTree t = DecisionTree.generateRandomTree();
				robots.set(i, new Robot(t));
			}
		}
		
		// Watch one animation of the last robot
		Robot best = robots.get(0);
		WatchRobotAnimation.watchAnimation(best, rooms.get(0));
		out.println(best.getTree());
		
		out.close();
	}

}