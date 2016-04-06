package database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;

import org.apache.commons.io.FileUtils;

public class Database {
	
	private Connection connection;
	
	public Database() throws SQLException {
		connection = connect();
	}
	
	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:database/scddata.db");
	}
	
	public int update() {
		try {
			URL url = new URL("http://media.strathspey.org/scddata/scddata.db");
			File dbFile = new File("database/scddata.db");
			FileUtils.copyURLToFile(url, dbFile);
			return 1;
		} catch(UnknownHostException e) {
			return 0;
		} catch(Exception e) {
			return -1;
		}
	}
	
	public ResultSet getPerson(int id) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM person WHERE id="+ id + ";";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet searchDanceByName(String searchString) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM dance WHERE name like '%" + searchString + "%'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet searchAlbumByName(String searchString) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM album WHERE name like '%" + searchString + "%'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet searchRecordingByName(String searchString) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM recording WHERE name like '%" + searchString + "%'";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet getNameByIdFromTable(String table, int id) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT name FROM '" + table +"' WHERE id=" + id;
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet getRecordingsByAlbum(int album_id) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT r.* FROM recording r JOIN albumsrecordingsmap arm ON r.id=arm.recording_id WHERE arm.album_id=" + album_id +" ORDER BY tracknumber";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public void printResults(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		int count = 1;
		while (resultSet.next()) {
			System.out.println("Record: " + count);
		    for (int i = 1; i <= columnsNumber; i++) {
		        String columnValue = resultSet.getString(i);
		        System.out.print(rsmd.getColumnName(i) + ": " + columnValue + ", ");
		    }
		    System.out.println("");
		    count++;
		}
	}
	
	public static void main(String[] args) throws SQLException, IOException {
		/* Update the database */
		System.out.print("Updating Database... ");
		Database db = new Database();
		int updateResult = db.update();
		String resultString;
		switch (updateResult) {
			case 1: 
				resultString = "SUCCESS!"; 
				break;
			case 0: 
				resultString = "FAILURE: No internet connection."; 
				break;
			case -1:
			default: 
				resultString = "FAILURE: An error occured while updating the database."; 
				break;
		}
		System.out.println(resultString);
		System.out.println("");
		
		/* get info on a person by id number */
//		System.out.println("Get Person");
		ResultSet resultSet = db.getPerson(2975);
		db.printResults(resultSet);
		
//		System.out.println("");
//		
//		/* Search for dance */
//		System.out.println("Search dance");
//		resultSet = db.searchDanceByName("dolphin");
//		db.printResults(resultSet);
//		
//		System.out.println("");
//		
//		/* Search for album */
//		System.out.println("Search album");
//		resultSet = db.searchAlbumByName("forever");
//		db.printResults(resultSet);
//		
//		/* Search for recording */
//		System.out.println("Search recording");
//		resultSet = db.searchRecordingByName("forever");
//		db.printResults(resultSet);
//		
//		int album_id = 1;
//
//		/* Search for name of some table its id */
//		System.out.println("ALBUM:");
//		resultSet = db.getNameByIdFromTable("album", album_id);
//		db.printResults(resultSet);
//		
//		/* Search for records by album id */
//		System.out.println("RECORDINGS:");
//		resultSet = db.getRecordingsByAlbum(album_id);
//		db.printResults(resultSet);
	}
}
