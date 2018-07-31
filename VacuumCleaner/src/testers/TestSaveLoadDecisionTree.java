package testers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import resources.DecisionTree;
import resources.Move;
import resources.Observe;

public class TestSaveLoadDecisionTree {

	public static void main(String[] args) {
		DecisionTree t, t1, t2, t3, t4;
		t1 = new DecisionTree(Observe.CUP, 0.5, new DecisionTree(Move.DOWN), new DecisionTree(Move.UP));
		t2 = new DecisionTree(Observe.CDOWN, 0.5, new DecisionTree(Move.LEFT), new DecisionTree(Move.RIGHT));
		t3 = new DecisionTree(Observe.CENTRE, 0.5, new DecisionTree(Move.DOWN), new DecisionTree(Move.QUIET));
		t4 = t3.getCopy();
		t = new DecisionTree(Observe.RDOWN, 0.3,
					new DecisionTree(Observe.LDOWN, 0.2, t1, t2),
					new DecisionTree(Observe.RUP, 0.7, t3, t4));
		
		String s1 = t.toString();
		String s2 = "";
		
		// try to save
		try {
			FileOutputStream fos = new FileOutputStream("saves/test.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(t);
			oos.close();
		} catch (Exception e) {
			System.out.println("Failed to save the tree");
			e.printStackTrace();
		}
		// try to load
		
		try {
			FileInputStream fin = new FileInputStream("saves/test.dat");
			ObjectInputStream ois = new ObjectInputStream(fin);
			DecisionTree tree = (DecisionTree) ois.readObject();
			ois.close();
			s2 = tree.toString();
		} catch (Exception e) {
			System.out.println("Failed to load tree");
			e.printStackTrace();
		}
		
		if (!s1.equals(s2)) {
			System.out.println(s1);
			System.out.println(s2);
		} else System.out.println("Everything alright ;)");
		
	}

}
