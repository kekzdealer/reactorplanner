package graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import logic.ComponentFactory;
import logic.ComponentFactory.ComponentType;

@SuppressWarnings("serial")
public class GraphicalUserInterface extends JFrame {
	
	public static final HashMap<ComponentFactory.ComponentType, BufferedImage> iconData = new HashMap<>();
	public static final String REACTOR_BACKGROUND_NAME = "reactor";
	public static final String EMPTY_COMPONENT_NAME = "emptyComponent";
	public static BufferedImage reactorBackground;
	public static BufferedImage emptyComponent;
	
	public static ComponentType selectedComponent;
	
	public static void main(String[] args) {
		
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
				if(me.getButton() == MouseEvent.BUTTON1) {
					reactorPanel.addComponent(iconData.get(partPicker.getSelectedItem()), 
							(int) Math.floor(mousePos.getX() / 67), 
							(int) Math.floor(mousePos.getY() / 67));					
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					reactorPanel.addComponent(emptyComponent, 
							(int) Math.floor(mousePos.getX() / 67), 
							(int) Math.floor(mousePos.getY() / 67));
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
		gui.add(infoPanel);
		
		gui.setVisible(true);
				
	}
	
}
