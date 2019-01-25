package geo.ensimag;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Utils;
import geoexplorer.gui.GeoMainFrame;
import geoexplorer.gui.LineString;
import geoexplorer.gui.MapPanel;
import geoexplorer.gui.Point;
import geoexplorer.gui.Polygon;

public class GrenobleMap {

	public static LineString LinefromText(String text, Color color) {
		//String srid= text.split(";")[0].split("=")[1];
		  String[] polygon = text.split(";")[1].substring(11, text.split(";")[1].length()-1).split(",");
		  LineString po = new LineString(color);
		  for (String string : polygon) {
			  String longitud = string.split(" ")[0];
			  String latitude = string.split(" ")[1];
			  Point p = new Point(Float.parseFloat(longitud), Float.parseFloat(latitude),Color.BLUE);
			  po.addPoint(p);
		}
		return po;
	}
	public static Polygon PolygonfromText(String text, Color color){
		//String srid= text.split(";")[0].split("=")[1];
		  String[] polygon = text.split(";")[1].substring(9, text.split(";")[1].length()-2).split(",");
		  Polygon po = new Polygon(Color.BLACK, color);
		  for (String string : polygon) {
			  String longitud = string.split(" ")[0];
			  String latitude = string.split(" ")[1];
			  Point p = new Point(Float.parseFloat(longitud), Float.parseFloat(latitude),Color.BLUE);
			  po.addPoint(p);
		}
		return po;
	}

public static void main(String[] args) {
	MapPanel mp = new MapPanel(918058.0882352941, 6457083.5882352935
, 5000);
	Connection c = Utils.getConnection();
	  String grenoble="ST_GeomFromText('POLYGON((5.8 45.1, 5.7 45.1,5.7 45.2, 5.8 45.2, 5.8 45.1))',4326)";
	  String q = " select ST_TRANSFORM(bbox,2154) as bboxx , tags->'highway' as hw, ST_TRANSFORM(linestring,2154) as ls from ways where ST_Contains("+ grenoble +", bbox )";
	  System.out.println(q);
	  try {
		  Statement st = c.createStatement();

		ResultSet rs = st.executeQuery(q);

		while (rs.next()) {
			String ls = rs.getObject("ls").toString();
			// if(rs.getObject("hw") != null) {
			// 	mp.addPrimitive(LinefromText(ls, Color.GREEN));
			// }else {
				mp.addPrimitive(LinefromText(ls, Color.BLACK));
			// }
		}
		GeoMainFrame g = new GeoMainFrame("g", mp);
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
}
