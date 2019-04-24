package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class GraphicalUserInterface extends JFrame {
		
	public static void main(String[] args) {
		GraphicalUserInterface gui = new GraphicalUserInterface();
		
		gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		gui.setTitle("GregTech Reactor Planner" +"V0.1");
		gui.setSize(1000, 700);
		gui.setResizable(false);
		gui.setLayout(null);
		gui.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		final BufferedImage image = loadImage("reactor");
		ReactorPanel reactorPanel = new ReactorPanel(image);
		reactorPanel.setLocation(10, 11);
		reactorPanel.setSize(608, 407);
		reactorPanel.setBackground(Color.LIGHT_GRAY);
		reactorPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				final Point mousePos = reactorPanel.getMousePosition();
				if(me.getButton() == MouseEvent.BUTTON1) {
					reactorPanel.addComponent(loadImage("uraniumFuelRod"), 
							(int) Math.floor(mousePos.getX() / 67), 
							(int) Math.floor(mousePos.getY() / 67));					
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					reactorPanel.addComponent(loadImage("emptyComponent"), 
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
	
	public static BufferedImage loadImage(String fileName) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Class.class.getResourceAsStream("/images/" +fileName + ".png"));
		} catch (IOException e) {
			System.err.println("Couldn't load resource: " +fileName);
		}
		return image;
	}
	
	public static void drawImage(BufferedImage image) {
		final Graphics g = image.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 614, 410);
		
		g.dispose();
		File file = new File("C://Users/Kekzdealer/eclipse-workspace/ReactorPlanner/bin/images/reactor.png");
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
