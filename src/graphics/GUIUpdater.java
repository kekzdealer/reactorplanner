package graphics;

import javax.swing.JLabel;

import logic.Reactor;

public class GUIUpdater implements Runnable {

	private final Reactor reactor;
	private final ReactorPanel reactorPanel;
	
	private final JLabel labelHullHeat;
	private final JLabel labelEUOutput;
	private final JLabel labelHUOutput;
		
	public GUIUpdater(Reactor reactor, ReactorPanel reactorPanel, 
			JLabel labelHullHeat, JLabel labelEUOutput, JLabel labelHUOutput) {
		this.reactor = reactor;
		this.reactorPanel = reactorPanel;
		
		this.labelHullHeat = labelHullHeat;
		this.labelEUOutput = labelEUOutput;
		this.labelHUOutput = labelHUOutput;
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			// Update info panel stuff
			labelHullHeat.setText("Hull Heat: " + reactor.getHullHeat());
			labelEUOutput.setText("EU/t: " + reactor.getEUOutput());
			labelHUOutput.setText("Hu/s " + reactor.getHUOutput());
			// Update reactor panel stuff
			reactorPanel.updateDurabilities(reactor.getAllComponents());
			reactorPanel.repaint();
			// Get some sleep
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("Updater thread got interrupted");
				e.printStackTrace();
			}
		}
	}
	
}
