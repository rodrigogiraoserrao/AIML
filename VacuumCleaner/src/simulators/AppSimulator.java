package simulators;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import resources.*;
import javax.swing.JMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JList;
import java.awt.ComponentOrientation;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JTree;

public class AppSimulator {

	private JFrame frame;
	private JTextField tf_nrobots;
	private JTextField tf_nrooms;
	
	private JLabel msg_label;
	private JTextArea logArea;
	
	private ArrayList<Robot> robots;
	private ArrayList<Room> rooms;
	private JTextField tf_room_width;
	private JTextField tf_room_height;
	private JTextField tf_starting_nsteps;
	private JTextField tf_dnsteps;
	private JTextField tf_dgens;
	private JTextField tf_ngens;
	private JTextField tf_nsteps;
	private JRadioButton rb_fixed;
	private JRadioButton rb_incremental;
	private JProgressBar progressBar;
	private JTextArea robot_tree;
	private JScrollPane treeView;
	private JPanel trees_panel;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppSimulator window = new AppSimulator();
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
	public AppSimulator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 438, 374);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel log_panel = new JPanel();
		log_panel.setVisible(false);
		
		JPanel simulation_panel = new JPanel();
		simulation_panel.setBounds(0, 0, 434, 285);
		frame.getContentPane().add(simulation_panel);
		simulation_panel.setLayout(null);
		
		JLabel lblNumberOfRobots = new JLabel("Number of robots:");
		lblNumberOfRobots.setBounds(10, 11, 108, 14);
		simulation_panel.add(lblNumberOfRobots);
		
		JLabel lblNumberOfRooms = new JLabel("Number of rooms:");
		lblNumberOfRooms.setBounds(10, 36, 108, 14);
		simulation_panel.add(lblNumberOfRooms);
		
		tf_nrobots = new JTextField();
		tf_nrobots.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_nrobots.setText("100");
		tf_nrobots.setBounds(156, 8, 86, 20);
		simulation_panel.add(tf_nrobots);
		tf_nrobots.setColumns(10);
		
		tf_nrooms = new JTextField();
		tf_nrooms.setText("100");
		tf_nrooms.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_nrooms.setBounds(156, 33, 86, 20);
		simulation_panel.add(tf_nrooms);
		tf_nrooms.setColumns(10);
		
		JButton btnSimulate = new JButton("Simulate");
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!validateInputs()) {
					logMe("Could not perform simulation because of invalid input.");
					return;
				}
				
				progressBar.setValue(0);
				
				// define all variables (almost all are constants) for the simulation
				int width = Integer.parseInt(tf_room_width.getText());
				int height = Integer.parseInt(tf_room_height.getText());
				int ngens = Integer.parseInt(tf_ngens.getText());
				progressBar.setMaximum(ngens);
				int nrobots = Integer.parseInt(tf_nrobots.getText());
				int nrooms = Integer.parseInt(tf_nrooms.getText());
				// take special care of constants related to default/incremental learning
				int nsteps;
				int d_steps;
				int dgens;
				boolean is_incremental = rb_incremental.isSelected();
				if (is_incremental) {
					nsteps = Integer.parseInt(tf_starting_nsteps.getText());
					d_steps = Integer.parseInt(tf_dnsteps.getText());
					dgens = Integer.parseInt(tf_dgens.getText());
				} else {
					nsteps = Integer.parseInt(tf_nsteps.getText());
					d_steps = 0;
					dgens = ngens + 1;
				}
				int increment_steps_counter = dgens;
				double score = 0;
				
				// check if we have a population of robots or not
				if (robots != null) {
					if (robots.size() != nrobots) {
						setMessage("Population of robots has size " + robots.size() + " and we expected " + nrobots + " robots.");
						logMe("Failed to perform simulation because expected number of robots (" + nrobots + ") was not met by previously loaded population (size " + robots.size()+ ")");
						tf_nrobots.requestFocus();
						return;
					}
				} else {
					robots = new ArrayList<Robot>();
					for (int i = 0; i < nrobots; ++i) robots.add(new Robot(DecisionTree.generateRandomTree()));
				}
				rooms = new ArrayList<Room>();
				
				// loop for the right number of generations
				for (int n = 1; n <= ngens; ++n) {
					progressBar.setValue(n);
					// create the batch of rooms for this generation
					rooms.clear();
					for (int i = 0; i < nrooms; ++i) {
						rooms.add(new Room(width, height));
					}
					// clean all rooms with all robots
					for (Robot r: robots) {
						for (Room room: rooms) {
							r.setRoom(room);
							r.clean();
							for (int i = 0; i < nsteps; ++i) {
								r.move();
								r.clean();
							}
						}
					}
					// the robots have cleaned, sort them out
					Collections.sort(robots, new RobotComparator());
					// print the best scoring robot and save it for later
					score = robots.get(0).getScore();
					logMe("Gen " + n + ": score = " + score);
					// mutate the robots! Leave the best 45% percent alone,
					// create another 45% with mutations from the first
					// create new, random 10%
					int cap = (int)(0.45*nrobots);
					for (int i = 0; i < cap; ++i) {
						// copy the tree; reset score
						DecisionTree t = robots.get(i).getTree().getCopy();
						robots.get(i).resetScore();
						// mutate it
						while (!t.mutate());
						// create a new robot and save it
						robots.set(i+cap, new Robot(t));
					}
					// if we got rounding problems, this loop fills the rest of the array with new random robots
					for (int i = 2*cap; i < nrobots; ++i) {
						DecisionTree t = DecisionTree.generateRandomTree();
						robots.set(i, new Robot(t));
					}
					
					if (--increment_steps_counter == 0) {
						increment_steps_counter = dgens;
						nsteps += d_steps;
						logMe("Incremented number of steps to " + nsteps + " for Gen " + (n+1) + " and onwards.");
					}
				}
				setMessage("Best performing robot ended with a score of " + score/nrooms + " pts/room");
				logMe("Best performing robot ended with: total score " + score + ", " + score/nrooms + " pts/room and " + score/(nrooms*nsteps) + " pts/(room*step)");
			}
		});
		btnSimulate.setBounds(33, 251, 89, 23);
		simulation_panel.add(btnSimulate);
		
		JLabel lblNumberOfSteps = new JLabel("Number of steps:");
		lblNumberOfSteps.setToolTipText("The number of steps is the number of movements each robot can do in each room.");
		lblNumberOfSteps.setBounds(10, 72, 108, 14);
		simulation_panel.add(lblNumberOfSteps);
		
		JLabel lblRoom = new JLabel("Room:");
		lblRoom.setBounds(261, 11, 46, 14);
		simulation_panel.add(lblRoom);
		
		JLabel lblWidth = new JLabel("width");
		lblWidth.setBounds(302, 11, 46, 14);
		simulation_panel.add(lblWidth);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(302, 36, 46, 14);
		simulation_panel.add(lblHeight);
		
		tf_room_width = new JTextField();
		tf_room_width.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_room_width.setText("20");
		tf_room_width.setBounds(358, 8, 66, 20);
		simulation_panel.add(tf_room_width);
		tf_room_width.setColumns(10);
		
		tf_room_height = new JTextField();
		tf_room_height.setText("20");
		tf_room_height.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_room_height.setColumns(10);
		tf_room_height.setBounds(358, 33, 66, 20);
		simulation_panel.add(tf_room_height);
		
		JLabel lblStartWith = new JLabel("Start with:");
		lblStartWith.setBounds(156, 102, 86, 14);
		simulation_panel.add(lblStartWith);
		
		tf_starting_nsteps = new JTextField();
		tf_starting_nsteps.setEnabled(false);
		tf_starting_nsteps.setToolTipText("Starting number of steps for the robots when the incremental mode is active.");
		tf_starting_nsteps.setText("40");
		tf_starting_nsteps.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_starting_nsteps.setColumns(10);
		tf_starting_nsteps.setBounds(261, 100, 56, 20);
		simulation_panel.add(tf_starting_nsteps);
		
		JLabel lblNewLabel = new JLabel("then increase by");
		lblNewLabel.setBounds(156, 123, 104, 14);
		simulation_panel.add(lblNewLabel);
		
		tf_dnsteps = new JTextField();
		tf_dnsteps.setEnabled(false);
		tf_dnsteps.setToolTipText("This is the increase in steps the robots get every time they get more steps.");
		tf_dnsteps.setText("20");
		tf_dnsteps.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_dnsteps.setColumns(10);
		tf_dnsteps.setBounds(261, 120, 56, 20);
		simulation_panel.add(tf_dnsteps);
		
		JLabel lblEveryGens = new JLabel("every # gens:");
		lblEveryGens.setBounds(156, 143, 104, 14);
		simulation_panel.add(lblEveryGens);
		
		tf_dgens = new JTextField();
		tf_dgens.setEnabled(false);
		tf_dgens.setToolTipText("This is the number of generations the simulation must go through before increasing the available number of steps");
		tf_dgens.setText("10");
		tf_dgens.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_dgens.setColumns(10);
		tf_dgens.setBounds(261, 140, 56, 20);
		simulation_panel.add(tf_dgens);
		
		JLabel lblNewLabel_1 = new JLabel("Generations:");
		lblNewLabel_1.setBounds(261, 72, 105, 14);
		simulation_panel.add(lblNewLabel_1);
		
		tf_ngens = new JTextField();
		tf_ngens.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_ngens.setText("100");
		tf_ngens.setBounds(358, 69, 66, 20);
		simulation_panel.add(tf_ngens);
		tf_ngens.setColumns(10);
		
		tf_nsteps = new JTextField();
		tf_nsteps.setToolTipText("This is the number of steps each robot has when the step mode is fixed.");
		tf_nsteps.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_nsteps.setText("400");
		tf_nsteps.setBounds(156, 69, 86, 20);
		simulation_panel.add(tf_nsteps);
		tf_nsteps.setColumns(10);
		
		rb_fixed = new JRadioButton("fixed");
		rb_fixed.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				boolean flag = arg0.getStateChange() == ItemEvent.SELECTED;
				tf_nsteps.setEnabled(flag);
			}
		});
		rb_fixed.setToolTipText("If the number of steps is fixed, then it will be the same for every robot (of every generation) in each room.");
		rb_fixed.setSelected(true);
		rb_fixed.setBounds(10, 93, 86, 23);
		simulation_panel.add(rb_fixed);
		
		rb_incremental = new JRadioButton("incremental");
		rb_incremental.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				boolean flag = arg0.getStateChange() == ItemEvent.SELECTED;
				tf_starting_nsteps.setEnabled(flag);
				tf_dnsteps.setEnabled(flag);
				tf_dgens.setEnabled(flag);
			}
		});
		rb_incremental.setToolTipText("If the number of steps is incremental, then the robots start with less freedom and gain more freedom as the simulation progresses. Use this for incremental learning.");
		rb_incremental.setBounds(10, 119, 104, 23);
		simulation_panel.add(rb_incremental);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(156, 253, 268, 19);
		simulation_panel.add(progressBar);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 61, 434, 2);
		simulation_panel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 239, 434, 2);
		simulation_panel.add(separator_1);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(0, 168, 434, 2);
		simulation_panel.add(separator_3);
		
		trees_panel = new JPanel();
		trees_panel.setBounds(0, 0, 434, 285);
		frame.getContentPane().add(trees_panel);
		trees_panel.setLayout(null);
		JScrollPane robotsScrollPane = new JScrollPane();
		robotsScrollPane.setBounds(0, 0, 125, 250);
		robotsScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		trees_panel.add(robotsScrollPane);
		
		JList l_robotList = new JList();
		robotsScrollPane.setViewportView(l_robotList);
		l_robotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		l_robotList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		JButton btnViewTree = new JButton("View tree");
		btnViewTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int idx = l_robotList.getSelectedIndex();
				if (idx < 0) return;
				Robot robot = robots.get(idx);
				robot_tree.setText(robot.getTree().toString());
				String msg = "Viewing decision tree of robot " + (idx+1);
				setMessage(msg);
				logMe(msg);
			}
		});
		btnViewTree.setBounds(20, 255, 89, 23);
		trees_panel.add(btnViewTree);
		
		robot_tree = new JTextArea();
		robot_tree.setBounds(125, 1, 309, 249);
		treeView = new JScrollPane(robot_tree);
		treeView.setBounds(125, 1, 309, 249);
		treeView.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		treeView.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		trees_panel.add(treeView);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rb_fixed);
		bg.add(rb_incremental);
		
		log_panel.setBounds(0, 0, 434, 285);
		frame.getContentPane().add(log_panel);
		log_panel.setLayout(null);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBounds(0, 0, 434, 275);
		logArea.setWrapStyleWord(true);
		JScrollPane logScrollPane = new JScrollPane(logArea);
		logScrollPane.setBounds(0, 0, 434, 285);
		logScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		log_panel.add(logScrollPane);
		
		this.msg_label = new JLabel("<html><p>Start by adjusting the settings to your preference and then run your simulation.</p></html>");
		msg_label.setBounds(10, 285, 414, 40);
		frame.getContentPane().add(msg_label);
		msg_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 285, 434, 2);
		frame.getContentPane().add(separator_2);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnSettings = new JMenu("Simulation");
		mnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				log_panel.setVisible(false);
				trees_panel.setVisible(false);
				simulation_panel.setVisible(true);
			}
		});
		menuBar.add(mnSettings);
		
		JMenu mnLog = new JMenu("Log");
		mnLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				simulation_panel.setVisible(false);
				trees_panel.setVisible(false);
				log_panel.setVisible(true);
			}
		});
		
		JMenu mnPopulation = new JMenu("Population");
		menuBar.add(mnPopulation);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mnPopulation.add(mntmNew);
		mntmNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				robots = new ArrayList<Robot>();
				int nrobots;
				try {
					nrobots = Integer.parseInt(tf_nrobots.getText());
				} catch (NumberFormatException ex) {
					setMessage("Insert a valid number of robots");
					logMe("Failed to create new batch of robots because of invalid total number");
					tf_nrobots.requestFocus();
					return;
				}
				if (nrobots <= 0) {
					setMessage("Number of robots must be >= 0");
					logMe("Failed to create new batch of robots because of non-positive total");
					tf_nrobots.requestFocus();
					return;
				}
				for (int i = 0; i < nrobots; ++i) {
					DecisionTree tree = DecisionTree.generateRandomTree();
					robots.add(new Robot(tree));
				}
				setMessage("Created new population of size " + nrobots + "\n");
				logMe("Created new population of size " + nrobots);
			}
		});
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		mnPopulation.add(mntmLoad);
		mntmLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog((Component)e.getSource());
				if (result==JFileChooser.APPROVE_OPTION){
					File chosenFile = fileChooser.getSelectedFile();
					String path = chosenFile.getAbsolutePath();
					
					try {
						FileInputStream fin = new FileInputStream(path);
						ObjectInputStream ois = new ObjectInputStream(fin);
						robots = (ArrayList<Robot>) ois.readObject();
						ois.close();
						tf_nrobots.setText(""+robots.size());
						setMessage("Loaded population");
						logMe("Loaded population of size " + robots.size() + " from file " + path);
					} catch (Exception ex) {
						setMessage("Failed to load");
						logMe("error while loading: " + ex.getMessage());
					}
				}
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnPopulation.add(mntmSave);
		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog((Component)e.getSource());
				if (result == JFileChooser.APPROVE_OPTION) {
					String chosenFileName = fileChooser.getSelectedFile().getAbsolutePath();
					try {
						System.out.println(chosenFileName);
						FileOutputStream fos = new FileOutputStream(chosenFileName);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						System.out.println(oos);
						System.out.println(robots);
						oos.writeObject(robots);
						oos.close();
						setMessage("population saved");
						logMe("Saved population of size " + robots.size() + " into " + chosenFileName);
					} catch (Exception ex) {
						setMessage("Failed to save population");
						logMe("error while saving: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});
		
		JMenu mnRobots = new JMenu("Trees");
		mnRobots.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				simulation_panel.setVisible(false);
				log_panel.setVisible(false);
				trees_panel.setVisible(true);
				// populate the list
				if (robots == null) return; // nothing more to be done
				String[] robotNames = new String[robots.size()];
				for (int i = 0; i < robotNames.length; ++i) {
					robotNames[i] = "Robot " + (i+1);
				}
				l_robotList.setListData(robotNames);
			}
		});
		menuBar.add(mnRobots);
		menuBar.add(mnLog);
		
	}
	
	public void setMessage(String msg) {
		this.msg_label.setText("<html><p>"+msg+"</p></html>");
	}
	
	public void logMe(String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now);
		logArea.append(time + ". " + msg + "\n");
	}
	
	public boolean hasPositiveInt(JTextField tf) {
		try {
			int i = Integer.parseInt(tf.getText());
			return i > 0;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean validateInputs() {
		JTextField[] tfs;
		if (rb_fixed.isSelected()) {
			tfs = new JTextField[] {tf_ngens, tf_room_width, tf_room_height, tf_nrobots, tf_nrooms, tf_nsteps};
		} else {
			tfs = new JTextField[] {tf_ngens, tf_room_width, tf_room_height, tf_nrobots, tf_nrooms, tf_dgens, tf_starting_nsteps, tf_dnsteps};
		}
		for (JTextField tf : tfs) {
			if (! this.hasPositiveInt(tf)) {
				tf.requestFocus();
				setMessage("Change this value to a positive integer please.");
				logMe("Invalid settings; non-(positive integer) inserted.");
				return false;
			}
		}
		
		return true;
	}
	
	public DefaultMutableTreeNode build_robot_tree(DecisionTree tree) {
		String info;
		if (tree.action != null) {
			info = tree.action.toString();
			return new DefaultMutableTreeNode(info);
		} else {
			info = tree.lookAt.toString() + " | " + tree.threshold;
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(info);
			root.add(build_robot_tree(tree.lt));
			root.add(build_robot_tree(tree.rt));
			return root;
		}
	}
}

class Simulation extends SwingWorker<Double, Integer> {
	// TODO read this link below and implement the simulation in a separate thread
	// javacreed.com/swing-worker-example
	@Override
	protected Double doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}