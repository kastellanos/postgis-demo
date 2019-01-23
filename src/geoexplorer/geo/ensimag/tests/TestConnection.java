package geo.ensimag.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.Utils;

// Question 10
class TestConnection {

  public static void main(String[] args){
	  // Connection to sql database using the Utils class
	  Connection c = Utils.getConnection();
	  try {
		// Create a basic request allowing to show the table name in the database
		Statement st = c.createStatement();
		ResultSet rs = st.executeQuery("SELECT table_name\n" + 
				"  FROM information_schema.tables\n" + 
				" WHERE table_schema='public'\n" + 
				"   AND table_type='BASE TABLE';");
		
		System.out.println("Table names command \\dt sur psql");
		System.out.println("*********************************");
		while (rs.next()) {
			  
			  System.out.println(rs.getObject("table_name"));
		}
		System.out.println("*********************************");
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  


  }


}
