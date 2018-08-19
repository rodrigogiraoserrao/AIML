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

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import resources.*;
import javax.swing.JMenuItem;

public class AppSimulator {

	private JFrame frame;
	private JTextField nrobots_input;
	private JTextField nrooms_input;
	
	private JLabel msg_label;
	private JTextArea logArea;
	
	private ArrayList<Robot> robots;
	private ArrayList<Room> rooms;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel log_panel = new JPanel();
		log_panel.setVisible(false);
		log_panel.setBounds(0, 0, 434, 201);
		frame.getContentPane().add(log_panel);
		log_panel.setLayout(null);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBounds(0, 0, 434, 201);
		logArea.setWrapStyleWord(true);
		JScrollPane logScrollPane = new JScrollPane(logArea);
		logScrollPane.setBounds(0, 0, 434, 201);
		logScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		log_panel.add(logScrollPane);
		
		JPanel animation_panel = new JPanel();
		animation_panel.setBounds(0, 0, 434, 201);
		frame.getContentPane().add(animation_panel);
		
		JPanel simulation_panel = new JPanel();
		simulation_panel.setBounds(0, 0, 434, 201);
		frame.getContentPane().add(simulation_panel);
		simulation_panel.setLayout(null);
		
		JLabel lblNumberOfRobots = new JLabel("Number of robots:");
		lblNumberOfRobots.setBounds(10, 11, 108, 14);
		simulation_panel.add(lblNumberOfRobots);
		
		JLabel lblNumberOfRooms = new JLabel("Number of rooms:");
		lblNumberOfRooms.setBounds(10, 36, 108, 14);
		simulation_panel.add(lblNumberOfRooms);
		
		nrobots_input = new JTextField();
		nrobots_input.setHorizontalAlignment(SwingConstants.RIGHT);
		nrobots_input.setText("100");
		nrobots_input.setBounds(156, 8, 86, 20);
		simulation_panel.add(nrobots_input);
		nrobots_input.setColumns(10);
		
		nrooms_input = new JTextField();
		nrooms_input.setText("100");
		nrooms_input.setHorizontalAlignment(SwingConstants.RIGHT);
		nrooms_input.setBounds(156, 33, 86, 20);
		simulation_panel.add(nrooms_input);
		nrooms_input.setColumns(10);
		
		this.msg_label = new JLabel("<html><p>Start by adjusting the settings to your preference and then run your simulation.</p></html>");
		msg_label.setBounds(0, 201, 434, 40);
		frame.getContentPane().add(msg_label);
		msg_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		mnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				animation_panel.setVisible(false);
				log_panel.setVisible(false);
				simulation_panel.setVisible(true);
			}
		});
		menuBar.add(mnSettings);
		
		JMenuItem mntmNew = new JMenuItem("New population");
		mntmNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				robots = new ArrayList<Robot>();
				int nrobots;
				try {
					nrobots = Integer.parseInt(nrobots_input.getText());
				} catch (NumberFormatException ex) {
					setMessage("Insert a valid number of robots");
					logMe("Failed to create new batch of robots because of invalid total number");
					nrobots_input.requestFocus();
					return;
				}
				if (nrobots <= 0) {
					setMessage("Number of robots must be >= 0");
					logMe("Failed to create new batch of robots because of non-positive total");
					nrobots_input.requestFocus();
					return;
				}
				for (int i = 0; i < nrobots; ++i) {
					DecisionTree tree = DecisionTree.generateRandomTree();
					robots.add(new Robot(tree));
				}
				setMessage("Created new generation of size " + nrobots + "\n");
				logMe("Created new generation of size " + nrobots);
			}
		});
		mnSettings.add(mntmNew);
		
		JMenuItem mntmLoad = new JMenuItem("Load ...");
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
						nrobots_input.setText(""+robots.size());
						setMessage("Loaded generation");
						logMe("Loaded generation of size " + robots.size() + " from file " + path);
					} catch (Exception ex) {
						setMessage("Failed to load");
						logMe("error while loading: " + ex.getMessage());
					}
				}
			}
		});
		mnSettings.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save ...");
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
						setMessage("Generation saved");
						logMe("Saved generation of size " + robots.size() + " into " + chosenFileName);
					} catch (Exception ex) {
						setMessage("Failed to save generation");
						logMe("error while saving: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});
		mnSettings.add(mntmSave);
		
		JMenu mnLog = new JMenu("Log");
		mnLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				simulation_panel.setVisible(false);
				animation_panel.setVisible(false);
				log_panel.setVisible(true);
			}
		});
		
		JMenu mnAnimation = new JMenu("Animation");
		mnAnimation.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				log_panel.setVisible(false);
				simulation_panel.setVisible(false);
				animation_panel.setVisible(true);
			}
		});
		menuBar.add(mnAnimation);
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
}
