package graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import logic.ComponentFactory;
import logic.ComponentFactory.ComponentType;
import logic.Reactor;
import logic.StatusReport;

@SuppressWarnings("serial")
public class GraphicalUserInterface extends JFrame {
	
	public static final HashMap<ComponentFactory.ComponentType, BufferedImage> iconData = new HashMap<>();
	public static final String REACTOR_BACKGROUND_NAME = "reactor";
	public static final String EMPTY_COMPONENT_NAME = "emptyComponent";
	public static BufferedImage reactorBackground;
	public static BufferedImage emptyComponent;
	
	public static ComponentType selectedComponent;
	
	public static void main(String[] args) {
		
		final Reactor reactor = new Reactor();
		
		// Load resources
		try {
			reactorBackground = ImageIO.read(Class.class.getResourceAsStream(
					"/images/" +REACTOR_BACKGROUND_NAME + ".png"));
			emptyComponent = ImageIO.read(Class.class.getResourceAsStream(
					"/images/" +EMPTY_COMPONENT_NAME + ".png"));
		} catch (IOException e) {
			System.err.println("Couldn't load background resources");
		}
		for(ComponentType type : ComponentType.values()) {
			try {
				System.out.println("Loading icon: " +type.getStringName());
				final BufferedImage image = ImageIO.read(
						Class.class.getResourceAsStream("/images/" + type.getStringName() + ".png"));
				if(image != null) {
					iconData.put(type, image);					
				}
			} catch (IOException e) {
				System.err.println("Couldn't load resource: " + type.getStringName());
			}
		}
		
		// Open window
		GraphicalUserInterface gui = new GraphicalUserInterface();
		
		gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gui.setTitle("GregTech Reactor Planner" +"V0.1");
		gui.setSize(1000, 700);
		gui.setResizable(false);
		gui.setLayout(null);
		gui.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		JComboBox<ComponentFactory.ComponentType> partPicker = 
				new JComboBox<>(ComponentFactory.ComponentType.values());
		partPicker.setLocation(700,	11);
		partPicker.setSize(200, 30);
		partPicker.setVisible(true);
		gui.add(partPicker);
		
		ReactorPanel reactorPanel = new ReactorPanel(reactorBackground);
		reactorPanel.setLocation(10, 11);
		reactorPanel.setSize(608, 407);
		reactorPanel.setBackground(Color.LIGHT_GRAY);
		reactorPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				final Point mousePos = reactorPanel.getMousePosition();
				final int slotX = (int) Math.floor(mousePos.getX() / 67);
				final int slotY = (int) Math.floor(mousePos.getY() / 67);
				if(me.getButton() == MouseEvent.BUTTON1) {
					reactorPanel.addComponent(iconData.get(partPicker.getSelectedItem()), slotX, slotY);
					reactor.insertComponent((ComponentType) partPicker.getSelectedItem(), slotX, slotY);
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					reactorPanel.addComponent(emptyComponent, slotX, slotY);
					// TODO : remove component from reactor when removed from UI
				}
				reactorPanel.repaint();
			}
		});
		gui.add(reactorPanel);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLocation(10, 450);
		infoPanel.setSize(972, 207);
		infoPanel.setBackground(Color.LIGHT_GRAY);
		infoPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		infoPanel.setLayout(null);
		gui.add(infoPanel);
		JLabel labelHullHeat = new JLabel("Hull Heat: ");
		JLabel labelEUOutput = new JLabel("EU/t: ");
		JLabel labelHUOutput = new JLabel("Hu/s ");
		labelHullHeat.setSize(200, 30);
		labelEUOutput.setSize(200, 30);
		labelHUOutput.setSize(200, 30);
		labelHullHeat.setLocation(3, 3);
		labelEUOutput.setLocation(3, 50);
		labelHUOutput.setLocation(3, 100);
		infoPanel.add(labelHullHeat);
		infoPanel.add(labelEUOutput);
		infoPanel.add(labelHUOutput);
		
		JButton toggleReactorButton = new JButton("START");
		toggleReactorButton.setSize(128, 32);
		toggleReactorButton.setLocation(650, 200);
		toggleReactorButton.setBackground(Color.GREEN);
		toggleReactorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleReactorButton.setText(reactor.isRunning() ? "STOP" : "START");
				toggleReactorButton.setBackground(reactor.isRunning() ? Color.RED : Color.GREEN);
				if(reactor.isRunning()) {
					reactor.setOff();
				}
				else {
					reactor.setOn();
					reactor.hookReactor(new StatusReport(labelHullHeat, labelEUOutput, labelHUOutput));
				}
			}
		});
		gui.add(toggleReactorButton);
		
		gui.setVisible(true);
				
	}
	
}
