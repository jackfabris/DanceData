package database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

import org.apache.commons.io.FileUtils;

public class Update {
	
	public static boolean update() throws IOException {
		URL url = new URL("http://media.strathspey.org/scddata/scddata.db");
		File dbFile = new File("database/scddata.db");
		FileUtils.copyURLToFile(url, dbFile);
		return true;
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		/* Update the database */
		System.out.print("Updating Database... ");
		Update.update();
		System.out.println("Done!\n");
		
		/* get info on a person by id number */
		System.out.println("Get Person");
		Database db = new Database();
		ResultSet resultSet = db.getPerson(2975);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search for dance */
		System.out.println("Search dance");
		resultSet = db.searchDanceByTitle("dolphin");
		db.printResults(resultSet);
	}
}
