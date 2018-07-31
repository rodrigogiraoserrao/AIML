package simulators;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WBasicSimulator {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

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
		
		JPanel simulation_panel = new JPanel();
		simulation_panel.setVisible(false);
		simulation_panel.setBounds(0, 0, 434, 241);
		frame.getContentPane().add(simulation_panel);
		
		JLabel lblNewLabel = new JLabel("This test");
		simulation_panel.add(lblNewLabel);
		
		JPanel settings_panel = new JPanel();
		settings_panel.setVisible(false);
		settings_panel.setBounds(0, 0, 434, 241);
		frame.getContentPane().add(settings_panel);
		settings_panel.setLayout(null);
		
		JLabel lblNumberOfRobots = new JLabel("Number of robots:");
		lblNumberOfRobots.setBounds(10, 11, 108, 14);
		settings_panel.add(lblNumberOfRobots);
		
		JLabel lblNumberOfRooms = new JLabel("Number of rooms:");
		lblNumberOfRooms.setBounds(10, 36, 108, 14);
		settings_panel.add(lblNumberOfRooms);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText("100");
		textField.setBounds(156, 8, 86, 20);
		settings_panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("100");
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setBounds(156, 33, 86, 20);
		settings_panel.add(textField_1);
		textField_1.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		mnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				simulation_panel.setVisible(false);
				settings_panel.setVisible(true);
			}
		});
		menuBar.add(mnSettings);
		
		JMenu mnSimulation = new JMenu("Simulation");
		mnSimulation.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				settings_panel.setVisible(false);
				simulation_panel.setVisible(true);
			}
		});
		menuBar.add(mnSimulation);
	}
}
