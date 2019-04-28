package graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

import logic.ComponentFactory;
import logic.ComponentFactory.ComponentSubType;
import logic.Reactor;

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
		reactorPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				final Point mousePos = reactorPanel.getMousePosition();
				final int slotX = (int) Math.floor(mousePos.getX() / 67);
				final int slotY = (int) Math.floor(mousePos.getY() / 67);
				if(me.getButton() == MouseEvent.BUTTON1) {
					reactorPanel.addComponent((ComponentSubType) partPicker.getSelectedItem(), slotX, slotY);
					reactor.insertComponent((ComponentSubType) partPicker.getSelectedItem(), slotX, slotY);
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					reactorPanel.removeComponent(slotX, slotY);
					reactor.removeComponent(reactor.getComponent(slotX, slotY));
				}
				reactorPanel.repaint();
			}
		});
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
		labelHullHeat.setLocation(3, 3);
		labelEUOutput.setLocation(3, 50);
		labelHUOutput.setLocation(3, 100);
		labelHullHeat.setSize(200, 30);
		labelEUOutput.setSize(200, 30);
		labelHUOutput.setSize(200, 30);
		
		JProgressBar progressBar = new JProgressBar(0, 10000);
		progressBar.setLocation(500, 30);
		progressBar.setSize(100, 30);
		progressBar.setValue(3000);
		
		
		// -> REGISTER INFO ELEMENTS
		infoPanel.add(labelHullHeat);
		infoPanel.add(labelEUOutput);
		infoPanel.add(labelHUOutput);
		infoPanel.add(progressBar);
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
		
		final GUIUpdater guiUpdater = new GUIUpdater(reactor, reactorPanel, 
				labelHullHeat, labelEUOutput, labelHUOutput);
		final Thread updaterThread = new Thread(guiUpdater);
		updaterThread.setName("GUI Updater Thread");
		updaterThread.start();
		
	}
	
}
