package simulators;

import resources.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class StatsBasicSimulator {
	private static final int ROOMWIDTH = 20;
	private static final int ROOMHEIGHT = 20;
	private static int STEPS = 400;
	private static final int NROBOTS = 60;
	private static final int NROOMS = 100;
	private static final int NGENS = 50;
	private static final int NSIMULATIONS = 500;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// create the file to write the results
		FileWriter fw;
		PrintWriter out;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm");
			LocalDateTime now = LocalDateTime.now();
			fw = new FileWriter("logs/stats_"+dtf.format(now)+".csv");
			out = new PrintWriter(fw);
		} catch (IOException e) {
			// default to writing to the console
			e.printStackTrace();
			out = new PrintWriter(System.out);
		}
		
		for (int N = 0; N < StatsBasicSimulator.NSIMULATIONS; ++N) {
			System.out.println(N);
			ArrayList<Robot> robots = new ArrayList<Robot>();
			for (int i = 0; i < StatsBasicSimulator.NROBOTS; ++i) {
				DecisionTree tree = DecisionTree.generateRandomTree();
				robots.add(new Robot(tree));
			}
	
			ArrayList<Room> rooms = new ArrayList<Room>();
			
			for (int n = 0; n < StatsBasicSimulator.NGENS; ++n) {
				// increment the available number of steps
				//if (n % 5 == 0) LoopBasicSimulator.STEPS += 5;
				
				rooms.clear();
				for (int i = 0; i < StatsBasicSimulator.NROOMS; ++i) {
					rooms.add(new Room(StatsBasicSimulator.ROOMWIDTH, StatsBasicSimulator.ROOMHEIGHT));
				}
				for (Robot r: robots) {
					for (Room room: rooms) {
						r.setRoom(room);
						r.clean();
						for (int i = 0; i < StatsBasicSimulator.STEPS; ++i) {
							r.move();
							r.clean();
						}
					}
				}
				// the robots have cleaned, sort them out
				Collections.sort(robots, new RobotComparator());
				// if this was the last generation, save their scores
				if (n == StatsBasicSimulator.NGENS-1) {
					String[] scores = new String[robots.size()];
					for (int k = 0; k < scores.length; ++k) {
						scores[k] = Double.toString(robots.get(k).getScore());
					}
					out.println(String.join(",", scores));
				}
				
				// leave the best 45% percent alone,
				// create another 45% with mutations from the first
				// create new, random 10%
				int cap = (int)(0.45*StatsBasicSimulator.NROBOTS);
				for (int i = 0; i < cap; ++i) {
					// copy the tree; reset score
					DecisionTree t = robots.get(i).getTree().getCopy();
					robots.get(i).resetScore();
					// mutate it
					while (!t.mutate());
					// create a new robot and save it
					robots.set(i+cap, new Robot(t));
				}
				for (int i = 2*cap; i < StatsBasicSimulator.NROBOTS; ++i) {
					DecisionTree t = DecisionTree.generateRandomTree();
					robots.set(i, new Robot(t));
				}
			}
		}
		
		out.close();
		System.out.println("done");
	}

}