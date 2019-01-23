package geo.ensimag.tests;

import java.awt.Color;

import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.Point;

public class TestGui {
	public static void main(String[] args) {
		
		Point p = new Point(5.7436787, 5.7436787,Color.BLUE);
		MapPanel mp = new MapPanel(5.7436787, 5.7436787, 500);
		mp.addPrimitive(p);
		GeoMainFrame g = new GeoMainFrame("g", mp);
		

	}
}
