package graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import graphics.GUIUpdater.LabelNames;
import logic.ComponentFactory;
import logic.ComponentFactory.ComponentSubType;
import logic.Reactor;

/*
 * TOOO: Make sure component overwriting works
 */
@SuppressWarnings("serial")
public class GraphicalUserInterface extends JFrame {
		
	public static void main(String[] args) {
		
		final Reactor reactor = new Reactor();
		
		// Open window
		GraphicalUserInterface gui = new GraphicalUserInterface();
		
		gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gui.setTitle("GregTech Reactor Planner" +"V0.1");
		gui.setSize(1000, 700);
		gui.setResizable(false);
		gui.setLayout(null);
		gui.getContentPane().setBackground(Color.LIGHT_GRAY);
		
		JComboBox<ComponentFactory.ComponentSubType> partPicker = 
				new JComboBox<>(ComponentFactory.ComponentSubType.values());
		partPicker.setLocation(700,	11);
		partPicker.setSize(200, 30);
		partPicker.setVisible(true);
		gui.add(partPicker);
		
		ReactorPanel reactorPanel = new ReactorPanel();
		reactorPanel.setLocation(10, 11);
		reactorPanel.setSize(608, 407);
		reactorPanel.setBackground(Color.LIGHT_GRAY);
		gui.add(reactorPanel);
		// INFO PANEL
		JPanel infoPanel = new JPanel();
		infoPanel.setLocation(10, 450);
		infoPanel.setSize(972, 207);
		infoPanel.setBackground(Color.LIGHT_GRAY);
		infoPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		infoPanel.setLayout(null);
		gui.add(infoPanel);
		// -> INFO LABELS
		JLabel labelHullHeat = new JLabel("Hull Heat: ");
		JLabel labelEUOutput = new JLabel("EU/t: ");
		JLabel labelHUOutput = new JLabel("Hu/s ");
		labelHullHeat.setLocation(6, 3);
		labelEUOutput.setLocation(6, 40);
		labelHUOutput.setLocation(6, 80);
		labelHullHeat.setSize(200, 20);
		labelEUOutput.setSize(200, 20);
		labelHUOutput.setSize(200, 20);
		// -> INFO TOOLTIP LABELS (n = name, d = data)
		JPanel tooltipPanel = new JPanel();
		tooltipPanel.setLayout(null);
		tooltipPanel.setLocation(704, 16);
		tooltipPanel.setSize(250, 174);
		tooltipPanel.setBackground(Color.LIGHT_GRAY);
		tooltipPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		infoPanel.add(tooltipPanel);
		
		JLabel labelTooltipName = new JLabel();
		JLabel labelTooltip1n = new JLabel();
		JLabel labelTooltip1d = new JLabel();
		JLabel labelTooltip2n = new JLabel();
		JLabel labelTooltip2d = new JLabel();
		JLabel labelTooltip3n = new JLabel();
		JLabel labelTooltip3d = new JLabel();
		JLabel labelTooltip4n = new JLabel();
		JLabel labelTooltip4d = new JLabel();
		labelTooltipName.setLocation(10, 10);
		labelTooltip1n.setLocation(10, 26);
		labelTooltip1d.setLocation(130, 26);
		labelTooltip2n.setLocation(10, 42);
		labelTooltip2d.setLocation(130, 42);
		labelTooltip3n.setLocation(10, 58);
		labelTooltip3d.setLocation(130, 58);
		labelTooltip4n.setLocation(10, 74);
		labelTooltip4d.setLocation(130, 74);
		labelTooltipName.setSize(200, 16);
		labelTooltip1n.setSize(120, 16);
		labelTooltip1d.setSize(120, 16);
		labelTooltip2n.setSize(120, 16);
		labelTooltip2d.setSize(120, 16);
		labelTooltip3n.setSize(120, 16);
		labelTooltip3d.setSize(120, 16);
		labelTooltip4n.setSize(120, 16);
		labelTooltip4d.setSize(120, 16);
		// -> REGISTER INFO ELEMENTS
		infoPanel.add(labelHullHeat);
		infoPanel.add(labelEUOutput);
		infoPanel.add(labelHUOutput);
		tooltipPanel.add(labelTooltipName);
		tooltipPanel.add(labelTooltip1n);
		tooltipPanel.add(labelTooltip1d);
		tooltipPanel.add(labelTooltip2n);
		tooltipPanel.add(labelTooltip2d);
		tooltipPanel.add(labelTooltip3n);
		tooltipPanel.add(labelTooltip3d);
		tooltipPanel.add(labelTooltip4n);
		tooltipPanel.add(labelTooltip4d);
		// BUTTONS
		JButton toggleReactorButton = new JButton("START");
		toggleReactorButton.setLocation(650, 200);
		toggleReactorButton.setSize(128, 32);
		toggleReactorButton.setBackground(Color.GREEN);
		JButton resetButton = new JButton("RESET");
		resetButton.setLocation(650, 250);
		resetButton.setSize(128, 32);
		resetButton.setBackground(Color.CYAN);
		// -> BUTTON LISTENERS
		toggleReactorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(reactor.isRunning()) {
					reactor.setIsActive(false);;
				}
				else {
					reactor.setIsActive(true);;
					reactor.hookReactor();
				}
				toggleReactorButton.setText(reactor.isRunning() ? "STOP" : "START");
				toggleReactorButton.setBackground(reactor.isRunning() ? Color.RED : Color.GREEN);
			}
		});
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reactor.setIsActive(false);
				reactor.setHullHeat(0);
				toggleReactorButton.setText(reactor.isRunning() ? "STOP" : "START");
				toggleReactorButton.setBackground(reactor.isRunning() ? Color.RED : Color.GREEN);
				reactor.removeAllComponents();
				reactorPanel.removeAllComponents();
				reactorPanel.repaint();
			}
		});
		// -> REGISTER BUTTONS
		gui.add(toggleReactorButton);
		gui.add(resetButton);
		
		gui.setVisible(true);
		
		final HashMap<LabelNames, JLabel> labels = new HashMap<>();
		labels.put(LabelNames.HullHeat, labelHullHeat);
		labels.put(LabelNames.EUOutput, labelEUOutput);
		labels.put(LabelNames.HUOutput, labelHUOutput);
		labels.put(LabelNames.ComponentName, labelTooltipName);
		labels.put(LabelNames.Line1Name, labelTooltip1n);
		labels.put(LabelNames.Line1Data, labelTooltip1d);
		labels.put(LabelNames.Line2Name, labelTooltip2n);
		labels.put(LabelNames.Line2Data, labelTooltip2d);
		labels.put(LabelNames.Line3Name, labelTooltip3n);
		labels.put(LabelNames.Line3Data, labelTooltip3d);
		labels.put(LabelNames.Line4Name, labelTooltip4n);
		labels.put(LabelNames.Line4Data, labelTooltip4d);
		final GUIUpdater guiUpdater = new GUIUpdater(reactor, reactorPanel, 
				labels);
		final Thread updaterThread = new Thread(guiUpdater);
		updaterThread.setName("GUI Updater Thread");
		updaterThread.start();
		
		// MOUSE LISTENERS
		reactorPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				final Point mousePos = reactorPanel.getMousePosition();
				final int slotX = (int) Math.min(Math.floor(mousePos.getX() / 67), 8);
				final int slotY = (int) Math.min(Math.floor(mousePos.getY() / 67), 5);
				if(me.getButton() == MouseEvent.BUTTON1) {
					reactorPanel.addComponent((ComponentSubType) partPicker.getSelectedItem(), slotX, slotY);
					reactor.insertComponent((ComponentSubType) partPicker.getSelectedItem(), slotX, slotY);
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					reactorPanel.removeComponent(slotX, slotY);
					reactor.removeComponent(reactor.getComponent(slotX, slotY), false);
				}
				reactorPanel.repaint();
			}
		});
		
		infoPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				final Point mousePos = gui.getMousePosition();
				System.out.println("Mouse pos: " +mousePos.getX() +"/" +mousePos.getY());
			}
		});
		
	}
	
}
