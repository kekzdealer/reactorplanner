package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ItemContainer extends JPanel {
	
	private final BufferedImage icon;
	
	public ItemContainer(BufferedImage icon) {
		//this.setSize(64, 64);
		
		this.icon = icon;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(icon, 0, 0, this);
	}
}
