package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import component_blueprints.FuelRod;
import component_blueprints.HeatManagementComponent;
import component_blueprints.NeutronReflector;
import component_blueprints.ReactorComponent;
import logic.ComponentFactory;
import logic.ComponentFactory.ComponentSubType;

@SuppressWarnings("serial")
public class ReactorPanel extends JPanel {
	
	private BufferedImage reactorBackground;
	private BufferedImage reactorOverlay;
	private BufferedImage emptyComponent;
	private final HashMap<ComponentFactory.ComponentSubType, BufferedImage> iconData = new HashMap<>();

	private final BufferedImage[][] components = new BufferedImage[9][6];	
	private final boolean[][] componentValidation = new boolean[9][6];
	private final int[][] durabilityBarLengths = new int[9][6];

	
	public ReactorPanel() {
		// Load resources
		try {
			reactorBackground = ImageIO.read(Class.class.getResourceAsStream(
					"/images/" +"reactor" + ".png"));
			reactorOverlay = ImageIO.read(Class.class.getResourceAsStream(
					"/images/" +"reactorOverlay" + ".png"));
			emptyComponent = ImageIO.read(Class.class.getResourceAsStream(
					"/images/" +"emptyComponent" + ".png"));
		} catch (FileNotFoundException e) {
			System.err.println("FNFE - Couldn't find background ressoruces");
		} catch(IllegalArgumentException e) {
			System.err.println("IAE - Couldn't load background resources");
		} catch (IOException e) {
			System.err.println("IOE - Couldn't load background resources");
		}
		for(ComponentSubType subType : ComponentSubType.values()) {
			try {
				final BufferedImage image = ImageIO.read(
						Class.class.getResourceAsStream("/images/" + subType.getStringName() + ".png"));
				if(image != null) {
					iconData.put(subType, image);					
				}
			} catch (FileNotFoundException e) {
				System.err.println("FNFE - Couldn't find resource: " +subType.getStringName());
			} catch (IllegalArgumentException e) {
				System.err.println("IAE - Couldn't load resource: " +subType.getStringName());
			} catch (IOException e) {
				System.err.println("IOE - Couldn't load resource: " +subType.getStringName());
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw reactor background
		g.drawImage(reactorBackground, 1, 1, this);
		for(int x = 0; x < 9; x++) {
			for(int y = 0; y < 6; y++) {
				// Draw all the components
				// Remove no longer existing ones
				if(componentValidation[x][y]) {
					g.drawImage(components[x][y], 
							4 + x * 67,
							4 + y * 67,
							this);					
				} else {
					g.drawImage(emptyComponent, 
							4 + x * 67,
							4 + y * 67,
							this);
				}
			}
		}
		// Draw reactor overlay
		g.drawImage(reactorOverlay, 1, 1, this);
		for(int x = 0; x < components.length; x++) {
			for(int y = 0; y < components[0].length; y++) {
				// -> reset bars to gray
				g.setColor(new Color(198, 198, 198, 255));
				g.fillRect((4 + x * 67), (66 + y * 67), 64, 3);
				// -> draw new bars
				if(componentValidation[x][y] == true) {
					if(durabilityBarLengths[x][y] >= 32) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if((durabilityBarLengths[x][y] >= 16) && (durabilityBarLengths[x][y] < 32)) {
						g.setColor(new Color(255, 127, 40, 255));
					} else {
						g.setColor(new Color(255, 0, 0, 255));
					}
					final int startX = 4 + x * 67;
					final int startY = 66 + y * 67;
					g.fillRect(startX, startY, durabilityBarLengths[x][y], 3);					
				}
			}
		}
		g.dispose();
	}
	
	public void updateDurabilities(HashSet<ReactorComponent> rcs) {
		// validate components (used in paintComponent() to check for outdated state)
		for(int x = 0; x < componentValidation.length; x++) {
			for(int y = 0; y < componentValidation[0].length; y++) {
				componentValidation[x][y] = false;
			}
		}
		for(ReactorComponent rc : rcs) {
			componentValidation[rc.getX()][rc.getY()] = true;
		}
		
		// Calculate pixel lengths for bars
		for(ReactorComponent rc : rcs) {
			double durability = 0;
			int durabilityMax = 0;
			switch(rc.getType()) {
			case HeatVent:
			case HeatExchanger:
			case CoolantCell:
				HeatManagementComponent hmc = (HeatManagementComponent) rc;
				durability = hmc.getHEAT_CAPACITY() - hmc.getHeat();
				durabilityMax = hmc.getHEAT_CAPACITY();
				break;
			case FuelRod:
				FuelRod fr = (FuelRod) rc;
				durability = fr.getRemainingLifetime();
				durabilityMax = fr.getLIFETIME();
				break;
			case NeutronReflector:
				NeutronReflector nr = (NeutronReflector) rc;
				durability = nr.getDurability();
				durabilityMax = nr.getDURABILITY();
			default:
				durability = 0;
				break;
			}
			double granularity = durabilityMax / 64.0D;
			int pixels = (int) Math.floor(durability / granularity);
			
			durabilityBarLengths[rc.getX()][rc.getY()] = pixels;	
		}
	}
	
	public void addComponent(ComponentSubType subType, int posX, int posY) {
		components[posX][posY] = iconData.get(subType);
	}
	
	public void removeComponent(int posX, int posY) {
		components[posX][posY] = emptyComponent;
	}
	
	public void removeAllComponents() {
		for(int x = 0; x < components.length; x++) {
			for(int y = 0; y < components[0].length; y++) {
				removeComponent(x, y);
			}
		}
	}
	
}
