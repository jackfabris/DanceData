package database;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;

import org.apache.commons.io.FileUtils;

/**
 * This is the database class. It is a sqlite database that connects to the file
 * in the database folder called scddata.db. It contains many functions for getting
 * the required data from the database to be put on screen. It also contains a
 * function to update the database upon the user request.
 *
 */
public class Database {
	
	private Connection connection;
	private Statement stmt;
	private String query;
	
	public Database() throws SQLException {
		connection = connect();
		stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		query = "";
	}
	
	/** 
	 * Connect to the local sqlite database and return the connection
	 * @return
	 * @throws SQLException
	 */
	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:database/scddata.db");
	}
	
	/**
	 * Downloads the most recent sqlite db file from the online source
	 * @return 1 on success; 0 when no internet connection; -1 on another error
	 */
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
	
	/**
	 * Gets all information about a person in the database with the given id
	 * 
	 * @param id - the person's id in the database
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getPerson(int id) throws SQLException {
		query = "SELECT * FROM person WHERE id="+ id;
		return stmt.executeQuery(query);
	}
	
	/**
	 * Search the table and return all records where name contains the param name
	 * @param table - the table to search in
	 * @param name - the name to search for
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet searchTableByName(String table, String name) throws SQLException {
		query = "SELECT * FROM " + table + " WHERE name like '%" + name + "%'";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Search the given table and return the name of the record with the given id
	 * @param table - the table to search in
	 * @param id - the id to find
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getNameByIdFromTable(String table, int id) throws SQLException {
		query = "SELECT name FROM '" + table +"' WHERE id=" + id;
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get a list of songs on the album with album_id
	 * @param album_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getRecordingsByAlbum(int album_id) throws SQLException {
		query = "SELECT r.* FROM recording r JOIN albumsrecordingsmap arm ON r.id=arm.recording_id WHERE arm.album_id=" + album_id +" ORDER BY tracknumber";
		return stmt.executeQuery(query);
	}
	
	/* Use for debugging to print the results of a query */
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
	
	public ResultSet doQuery(String s) throws SQLException {
		query = s;
		return stmt.executeQuery(query);
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
		System.out.println("Get Person");
		ResultSet resultSet = db.getPerson(2975);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search for dance */
		System.out.println("Search dance");
		resultSet = db.searchTableByName("dance", "dolphin");
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search for album */
		System.out.println("Search album");
		resultSet = db.searchTableByName("album", "Jimmy");
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search for recording */
		System.out.println("Search recording");
		resultSet = db.searchTableByName("recording", "frog");
		db.printResults(resultSet);
		
		System.out.println("");
		
		int album_id = 1;

		/* Search for name of some table its id */
		System.out.println("ALBUM:");
		resultSet = db.getNameByIdFromTable("album", album_id);
		db.printResults(resultSet);
		
		/* Search for records by album id */
		System.out.println("RECORDINGS:");
		resultSet = db.getRecordingsByAlbum(album_id);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Last executed query: " + db.query);
	}
}