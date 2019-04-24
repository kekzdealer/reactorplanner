package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ReactorPanel extends JPanel {
	
	private final BufferedImage background;
	private final BufferedImage[][] components = new BufferedImage[9][6];
	
	public ReactorPanel(BufferedImage background) {
		this.background = background;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 1, 1, this);
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 6; y++) {
				g.drawImage(components[x][y], 
						4 + x * 67,
						4 + y * 67,
						this);
			}
		}
	}
	
	public void addComponent(BufferedImage image, int posX, int posY) {
		components[posX][posY] = image;
	}
	
}
