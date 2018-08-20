package simulators;

import resources.*;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
		BasicSimulator.saveRobots(
			BasicSimulator.makeBasicSimulation(
					BasicSimulator.ROOMWIDTH,
					BasicSimulator.ROOMHEIGHT,
					BasicSimulator.STEPS,
					BasicSimulator.NROBOTS,
					BasicSimulator.NROOMS,
					BasicSimulator.NGENS
			)
		);
	}
	
	public static boolean saveRobots(ArrayList<Robot> robots) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		LocalDateTime now = LocalDateTime.now();
		String saveFile = "saves/"+dtf.format(now)+".gen";
		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(robots);
			oos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static ArrayList<Robot> makeBasicSimulation(int ROOMWIDTH, int ROOMHEIGHT, int STEPS, int NROBOTS, int NROOMS, int NGENS) {
		// TODO Auto-generated method stub
		
		// create a logging file
		FileWriter fw;
		PrintWriter out;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		LocalDateTime now = LocalDateTime.now();
		try {
			fw = new FileWriter("logs/"+dtf.format(now)+".log");
			out = new PrintWriter(fw);
		} catch (IOException e) {
			// default to writing to the console
			e.printStackTrace();
			out = new PrintWriter(System.out);
		}
		
		ArrayList<Robot> robots = new ArrayList<Robot>();
		ArrayList<Robot> bestRobots = new ArrayList<Robot>();
		for (int i = 0; i < NROBOTS; ++i) {
			DecisionTree tree = DecisionTree.generateRandomTree();
			robots.add(new Robot(tree));
		}

		ArrayList<Room> rooms = new ArrayList<Room>();
		
		for (int n = 0; n < NGENS; ++n) {
			rooms.clear();
			for (int i = 0; i < NROOMS; ++i) {
				rooms.add(new Room(ROOMWIDTH, ROOMHEIGHT));
			}
			for (Robot r: robots) {
				for (Room room: rooms) {
					r.setRoom(room);
					r.clean();
					for (int i = 0; i < STEPS; ++i) {
						r.move();
						r.clean();
					}
				}
			}
			// the robots have cleaned, sort them out
			Collections.sort(robots, new RobotComparator());
			// print the best scoring robot and save it for later
			out.println("Gen " + n + ": score = " + robots.get(0).getScore());
			// create a deep copy of the robot
			bestRobots.add(new Robot(robots.get(0).getTree().getCopy()));
			bestRobots.get(bestRobots.size()-1).setScore(robots.get(0).getScore());
			
			// leave the best 45% percent alone,
			// create another 45% with mutations from the first
			// create new, random 10%
			int cap = (int)(0.45*NROBOTS);
			for (int i = 0; i < cap; ++i) {
				// copy the tree; reset score
				DecisionTree t = robots.get(i).getTree().getCopy();
				robots.get(i).resetScore();
				// mutate it
				while (!t.mutate());
				// create a new robot and save it
				robots.set(i+cap, new Robot(t));
			}
			for (int i = 2*cap; i < NROBOTS; ++i) {
				DecisionTree t = DecisionTree.generateRandomTree();
				robots.set(i, new Robot(t));
			}
		}
		
		// Watch one animation of the last robot
		Robot best = robots.get(0);
		WatchRobotAnimation.watchAnimation(best, rooms.get(0));
		out.println(best.getTree());
		
		out.close();
		
		return bestRobots;
	}

}