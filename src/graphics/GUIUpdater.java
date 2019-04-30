package graphics;

import java.awt.Point;
import java.util.HashMap;

import javax.swing.JLabel;

import component_blueprints.CoolantCell;
import component_blueprints.DepletedFuelRod;
import component_blueprints.EmergencyFoam;
import component_blueprints.FuelRod;
import component_blueprints.HeatExchanger;
import component_blueprints.HeatVent;
import component_blueprints.NeutronReflector;
import component_blueprints.ReactorComponent;
import logic.Reactor;

public class GUIUpdater implements Runnable {
	
	public enum LabelNames {
		// tool tip
		ComponentName,
		Line1Name,
		Line1Data,
		Line2Name,
		Line2Data,
		Line3Name,
		Line3Data,
		Line4Name,
		Line4Data,
		// reactor information
		HullHeat,
		EUOutput,
		HUOutput
	}
	
	private final Reactor reactor;
	private final ReactorPanel reactorPanel;

	private final HashMap<LabelNames, JLabel> labels;
	
	public GUIUpdater(Reactor reactor, ReactorPanel reactorPanel, 
			HashMap<LabelNames, JLabel> labels) {
		this.reactor = reactor;
		this.reactorPanel = reactorPanel;
		this.labels = labels;
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			// Update info panel stuff
			labels.get(LabelNames.HullHeat).setText("Hull Heat: " + reactor.getHullHeat());
			labels.get(LabelNames.EUOutput).setText("EU/t: " + reactor.getEUOutput());
			labels.get(LabelNames.HUOutput).setText("Hu/s " + reactor.getHUOutput());
			// Update tool tip stuff
			final Point mousePos = reactorPanel.getMousePosition();
			if(mousePos == null) {
				// clear tooltip
			} else {
				final int slotX = (int) Math.min(Math.floor(mousePos.getX() / 67), 8);
				final int slotY = (int) Math.min(Math.floor(mousePos.getY() / 67), 5);
				final ReactorComponent rc = reactor.getComponent(slotX, slotY);	
				if(rc != null) {
					switch(rc.getType()) {
					case HeatVent:
						HeatVent hv = (HeatVent) rc;
						labels.get(LabelNames.ComponentName).setText("" +hv.getType());
						labels.get(LabelNames.Line1Name).setText("Heat Capacity:");
						labels.get(LabelNames.Line1Data).setText((int)hv.getHeat() +"/" +(int)hv.getHEAT_CAPACITY());
						labels.get(LabelNames.Line2Name).setText("Comp. heat vented:");
						labels.get(LabelNames.Line2Data).setText((int)hv.getComponentHeatVented() +"/" +(int)hv.getCOMPONENT_VENT_RATE());
						labels.get(LabelNames.Line3Name).setText("Hull heat drawn:");
						labels.get(LabelNames.Line3Data).setText((int)hv.getHullHeatDrawn() +"/" +(int)hv.getHULL_VENT_RATE());
						labels.get(LabelNames.Line4Name).setText("Self heat vented:");
						labels.get(LabelNames.Line4Data).setText((int)hv.getSelfHeatVented() +"/" +(int)hv.getSELF_VENT_RATE());
						break;
					case HeatExchanger:
						HeatExchanger he = (HeatExchanger) rc;
						labels.get(LabelNames.ComponentName).setText("" +he.getType());
						labels.get(LabelNames.Line1Name).setText("Heat Capacity:");
						labels.get(LabelNames.Line1Data).setText((int)he.getHeat() +"/" +(int)he.getHEAT_CAPACITY());
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					case CoolantCell:
						CoolantCell cc = (CoolantCell) rc;
						labels.get(LabelNames.ComponentName).setText("" +cc.getType());
						labels.get(LabelNames.Line1Name).setText("Heat Capacity:");
						labels.get(LabelNames.Line1Data).setText((int)cc.getHeat() +"/" +(int)cc.getHEAT_CAPACITY());
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					case FuelRod:
						FuelRod fr = (FuelRod) rc;
						labels.get(LabelNames.ComponentName).setText("" +fr.getType());
						labels.get(LabelNames.Line1Name).setText("Lifetime:");
						labels.get(LabelNames.Line1Data).setText((int)fr.getRemainingLifetime() +"/" +(int)fr.getLIFETIME());
						labels.get(LabelNames.Line2Name).setText("EU produced:");
						labels.get(LabelNames.Line2Data).setText("" +(int)fr.getEUProduced());
						labels.get(LabelNames.Line3Name).setText("Heat produced:");
						labels.get(LabelNames.Line3Data).setText("" +(int)fr.getHeatProduced());
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					case NeutronReflector:
						NeutronReflector nr = (NeutronReflector) rc;
						labels.get(LabelNames.ComponentName).setText("" +nr.getType());
						labels.get(LabelNames.Line1Name).setText("Durability:");
						labels.get(LabelNames.Line1Data).setText((int)nr.getDurability() +"/" +(int)nr.getDURABILITY());
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					case DepletedFuelRod:
						DepletedFuelRod dfr = (DepletedFuelRod) rc;
						labels.get(LabelNames.ComponentName).setText("" +dfr.getDepletedRod());
						labels.get(LabelNames.Line1Name).setText("");
						labels.get(LabelNames.Line1Data).setText("");
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					case EmergencyFoam:
						EmergencyFoam ef = (EmergencyFoam) rc;
						labels.get(LabelNames.ComponentName).setText("" +ef.getType());
						labels.get(LabelNames.Line1Name).setText("Threshold:");
						labels.get(LabelNames.Line1Data).setText((int)(ef.getHULL_HEAT_THRESHOLD() * 100) +"%");
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					default: 
						labels.get(LabelNames.ComponentName).setText("");
						labels.get(LabelNames.Line1Name).setText("");
						labels.get(LabelNames.Line1Data).setText("");
						labels.get(LabelNames.Line2Name).setText("");
						labels.get(LabelNames.Line2Data).setText("");
						labels.get(LabelNames.Line3Name).setText("");
						labels.get(LabelNames.Line3Data).setText("");
						labels.get(LabelNames.Line4Name).setText("");
						labels.get(LabelNames.Line4Data).setText("");
						break;
					}
				} else {
					labels.get(LabelNames.ComponentName).setText("");
					labels.get(LabelNames.Line1Name).setText("");
					labels.get(LabelNames.Line1Data).setText("");
					labels.get(LabelNames.Line2Name).setText("");
					labels.get(LabelNames.Line2Data).setText("");
					labels.get(LabelNames.Line3Name).setText("");
					labels.get(LabelNames.Line3Data).setText("");
					labels.get(LabelNames.Line4Name).setText("");
					labels.get(LabelNames.Line4Data).setText("");
				}
			}
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
