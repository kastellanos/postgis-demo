package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import database.Utils;

public class TestLikeRequest {
	public static void likeRequet(String s) {
		// Connection to sql database using the Utils class
		
		  Connection c = Utils.getConnection();
		  String nom=s;
		  try {
			// Create a basic request allowing to show the table name in the database
			
			PreparedStatement pstmt = c.prepareStatement(
				      "SELECT tags->'name' as nom, ST_X(geom) as longitude, ST_Y(geom) as latitude FROM nodes WHERE tags->'name' like ? ;");
			pstmt.setString(1, nom);
			ResultSet rs = pstmt.executeQuery();
			
			System.out.println("Requete like "+nom);
			System.out.println("*********************************");
			while (rs.next()) {
				  
				  System.out.println(rs.getObject("nom")+": Coordonées ("+rs.getObject("longitude")+","+rs.getObject("longitude")+")");
			}
			System.out.println("*********************************");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		// Connection to sql database using the Utils class
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			TestLikeRequest.likeRequet(line);
		}
		  

	}
}
