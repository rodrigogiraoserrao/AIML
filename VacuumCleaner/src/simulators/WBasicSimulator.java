package simulators;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import resources.DecisionTree;
import resources.Robot;
import resources.RobotComparator;
import resources.Room;
import resources.WatchRobotAnimation;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ActionEvent;

public class WBasicSimulator {

	private JFrame frame;
	private JTextArea logArea;
	private JTextField tf_robots;
	private JTextField tf_rooms;
	private JTextField tf_generations;
	
	private static final int ROOM_WIDTH = 20;
	private static final int ROOM_HEIGHT = 20;
	private static final int NSTEPS = 400;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WBasicSimulator window = new WBasicSimulator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WBasicSimulator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		logArea = new JTextArea();
		logArea.setEditable(false);;
		logArea.setBounds(199, 0, 235, 262);
		logArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBounds(199, 0, 235, 262);
		scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane);
		
		JButton btnSimulate = new JButton("Simulate");
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField[] tfs = {tf_robots, tf_rooms, tf_generations};
				int[] vars = new int[tfs.length];
				for (int i = 0; i < tfs.length; ++i) {
					try {
						vars[i] = Integer.parseInt(tfs[i].getText());
						if (vars[i] <= 0) throw new Exception("Must have positive integer");
					} catch (Exception e) {
						e.printStackTrace();
						tfs[i].requestFocus();
						return;
					}
				}
				int nrobots = vars[0];
				int nrooms = vars[1];
				int ngens = vars[2];
				
				ArrayList<Robot> best = BasicSimulator.makeBasicSimulation(
						WBasicSimulator.ROOM_WIDTH,
						WBasicSimulator.ROOM_HEIGHT,
						WBasicSimulator.NSTEPS,
						nrobots,
						nrooms,
						ngens
				);
				
				for (int i = 0; i < best.size(); ++i) {
					logArea.append("Gen " + (i+1) + ": score " + best.get(i).getScore() + "\n");
				}
			}
		});
		btnSimulate.setBounds(53, 202, 89, 23);
		frame.getContentPane().add(btnSimulate);
		
		JLabel lblNumberOfRobots = new JLabel("Number of robots:");
		lblNumberOfRobots.setBounds(22, 22, 167, 14);
		frame.getContentPane().add(lblNumberOfRobots);
		
		tf_robots = new JTextField();
		tf_robots.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_robots.setText("100");
		tf_robots.setBounds(103, 45, 86, 20);
		frame.getContentPane().add(tf_robots);
		tf_robots.setColumns(10);
		
		JLabel lblNumberOfRooms = new JLabel("Number of rooms:");
		lblNumberOfRooms.setBounds(22, 77, 167, 14);
		frame.getContentPane().add(lblNumberOfRooms);
		
		tf_rooms = new JTextField();
		tf_rooms.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_rooms.setText("100");
		tf_rooms.setBounds(103, 100, 86, 20);
		frame.getContentPane().add(tf_rooms);
		tf_rooms.setColumns(10);
		
		JLabel lblNumberOfGenerations = new JLabel("Number of generations:");
		lblNumberOfGenerations.setBounds(22, 132, 167, 14);
		frame.getContentPane().add(lblNumberOfGenerations);
		
		tf_generations = new JTextField();
		tf_generations.setText("50");
		tf_generations.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_generations.setBounds(103, 155, 86, 20);
		frame.getContentPane().add(tf_generations);
		tf_generations.setColumns(10);
	}
}
