package resources;

import java.util.Comparator;

public class RobotComparator implements Comparator<Robot> {
	public int compare(Robot r1, Robot r2) {
		// we want the robots with higher scores in the front
		if (r1.getScore() < r2.getScore()) return 1;
		else if (r1.getScore() == r2.getScore()) return 0;
		else return -1;
	}
}