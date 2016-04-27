package database;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Iterator;
import java.util.Scanner;

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
	private File dbFile;
	private File saveFile;
	private URL dbURL;
	
	public Database() throws SQLException, MalformedURLException {
		query = "";
		dbURL = new URL("http://media.strathspey.org/scddata/scddata.db");
		dbFile = new File("database/scddata.db");
		saveFile = new File("database/ihave.txt");
		init();
	}
	
	private void init() throws SQLException, MalformedURLException {
		connection = connect();
		stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
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
	 * Remember what stuff the user has in their collection
	 * Downloads the most recent sqlite db file from the online source
	 * Add the ihave and tag columns back to the db
	 * Load the stuff we saved earlier back into the db
	 * @return 1 on success; 0 when no internet connection; -1 on another error
	 */
	public int update() {
		try {
			
			/* Save what I have */
			try {
				this.saveIHave();
			} catch (Exception e) {
				System.out.println("save failed...");
			}
			
			connection.close();
			FileUtils.copyURLToFile(dbURL, dbFile);
			init();
			this.addIHaveTagColumns();
			this.loadIHave();
			return 1;
		} catch(UnknownHostException e) {
			return 0;
		} catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Add the necessary columns to be able to tag dances, albums, and publications as 
	 * having in a personal collection.
	 * @throws SQLException
	 */
	public void addIHaveTagColumns() throws SQLException {
		stmt.execute("ALTER TABLE dance ADD ihave TINYINT(1) DEFAULT 0");
		stmt.execute("ALTER TABLE dance ADD COLUMN tag VARCHAR(256) DEFAULT NULL");
		stmt.execute("ALTER TABLE album ADD COLUMN ihave TINYINT(1) DEFAULT 0");
		stmt.execute("ALTER TABLE album ADD COLUMN tag VARCHAR(256) DEFAULT NULL");
		stmt.execute("ALTER TABLE publication ADD COLUMN ihave TINYINT(1) DEFAULT 0");
		stmt.execute("ALTER TABLE publication ADD COLUMN tag VARCHAR(256) DEFAULT NULL");
		stmt.execute("ALTER TABLE recording ADD COLUMN ihave TINYINT(1) DEFAULT 0");
		stmt.execute("ALTER TABLE recording ADD COLUMN tag VARCHAR(256) DEFAULT NULL");
	}
	
	/**
	 * Save the information for what the user has in their personal collection. 
	 * Writes the type (table), the id, and the tag to a file. We will use this information
	 * when we update to preserve this information.
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void saveIHave() throws SQLException, IOException {
		
		// dance
		query = "SELECT id, tag FROM dance WHERE ihave=1";
		ResultSet rs = stmt.executeQuery(query);
		String info;
		boolean append = false;
		while(rs.next()) {
			info = "dance " + rs.getInt("id") + " " + rs.getString("tag");
			FileUtils.writeStringToFile(saveFile, info, append);
			append = true;
			FileUtils.writeStringToFile(saveFile, "\n", append);
		}
		
		// album
		query = "SELECT id, tag FROM album WHERE ihave=1";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			info = "album " + rs.getInt("id") + " " + rs.getString("tag");
			FileUtils.writeStringToFile(saveFile, info, append);
			FileUtils.writeStringToFile(saveFile, "\n", append);
		}
		
		// publication
		query = "SELECT id, tag FROM publication WHERE ihave=1";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			info = "publication " + rs.getInt("id") + " " + rs.getString("tag");
			FileUtils.writeStringToFile(saveFile, info, append);
			FileUtils.writeStringToFile(saveFile, "\n", append);
		}
		
		// recording
		query = "SELECT id, tag FROM recording WHERE ihave=1";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			info = "recording " + rs.getInt("id") + " " + rs.getString("tag");
			FileUtils.writeStringToFile(saveFile, info, append);
			FileUtils.writeStringToFile(saveFile, "\n", append);
		}
	}
	
	public void loadIHave() throws IOException, SQLException {
		Iterator<String> iter = FileUtils.readLines(saveFile).iterator();
		String line;
		String[] info;
		while(iter.hasNext()) {
			line = iter.next();
			info = line.split(" ");
			if(info.length == 2)
				query = "UPDATE " + info[0] + " SET ihave=1 WHERE id=" + info[1];
			else
				query = "UPDATE " + info[0] + " SET ihave=1, tag='" + info[2] + "' WHERE id=" + info[1];
			stmt.execute(query);
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
	 * @param ihave - if true only show what is marked as ihave, otherwise show all results
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet searchTableByName(String table, String name, boolean ihave) throws SQLException {
		if(table.equals("dance")) {
			query = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
					+ "c.name as couples, p.name as progression, pb.name as publication, pn.name as devisor FROM dance d "
					+ "LEFT OUTER JOIN dancetype dt ON d.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON d.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN shape s ON d.shape_id=s.id "
					+ "LEFT OUTER JOIN couples c ON d.couples_id=c.id "
					+ "LEFT OUTER JOIN progression p ON d.progression_id=p.id "
					+ "LEFT OUTER JOIN dancespublicationsmap dpm ON d.id=dpm.dance_id "
					+ "LEFT OUTER JOIN publication pb ON dpm.publication_id=pb.id "
					+ "LEFT OUTER JOIN person pn ON d.devisor_id=pn.id "
					+ "WHERE d.name like '%" + name + "%'";
			if(ihave) {
				query += " AND d.ihave=1";
			}
		} else if(table.equals("album")) {
			query = "SELECT a.*, p.name as artist FROM album a "
					+ "LEFT OUTER JOIN person p ON a.artist_id=p.id "
					+ "WHERE a.name like '%" + name + "%'";
			if(ihave) {
				query += " AND a.ihave=1";
			}
		} else if(table.equals("publication")) {
			query = "SELECT pb.*, pr.name as devisor FROM publication pb "
					+ "LEFT OUTER JOIN person pr ON pb.devisor_id=pr.id WHERE pb.name like '%" + name + "%'";
			if(ihave) {
				query += " AND pb.ihave=1";
			}
		} else if(table.equals("recording")){
			query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "WHERE r.name like '%" + name + "%'";
			if(ihave) {
				query += " AND r.ihave=1";
			}
		} else {
			query = "SELECT * FROM " + table + " WHERE name like '%" + name + "%'";
			if(ihave) {
				query += " AND ihave=1";
			}
		}
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
	 * Search the given table and return all data for the given id
	 * @param table - the table to search in
	 * @param id - the id to find
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getAllByIdFromTable(String table, int id) throws SQLException {
		query = "SELECT * FROM '" + table +"' WHERE id=" + id;
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get a list of songs on the album with album_id
	 * @param album_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getRecordingsByAlbum(int album_id) throws SQLException {
		query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
				+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
				+ "LEFT OUTER JOIN albumsrecordingsmap arm ON r.id=arm.recording_id "
				+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
				+ "WHERE arm.album_id=" + album_id + " ORDER BY tracknumber";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get a list of dances in the publication with publication_id
	 * @param publication_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getDancesByPublication(int publication_id) throws SQLException {
		query = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
				+ "c.name as couples, p.name as progression, pb.name as publication, pn.name as devisor FROM dance d "
				+ "LEFT OUTER JOIN dancetype dt ON d.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON d.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN shape s ON d.shape_id=s.id "
				+ "LEFT OUTER JOIN couples c ON d.couples_id=c.id "
				+ "LEFT OUTER JOIN progression p ON d.progression_id=p.id "
				+ "LEFT OUTER JOIN dancespublicationsmap dpm ON d.id=dpm.dance_id "
				+ "LEFT OUTER JOIN publication pb ON dpm.publication_id=pb.id "
				+ "LEFT OUTER JOIN person pn ON d.devisor_id=pn.id "
				+ "WHERE dpm.publication_id=" + publication_id + "ORDER BY number";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Mark as having in personal collection. 
	 * @param table - the type of thing i have (can be album, recording, publication, or dance)
	 * @param id - the id 
	 * @param tag - what should we mark the tag
	 * @throws SQLException
	 */
	public void iHave(String table, int id, String tag) throws SQLException {
		query = "UPDATE " + table + " SET ihave=1, tag='" + tag + "' WHERE id=" + id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET ihave=1, tag='" + tag + "' WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET ihave=1, tag='" + tag + "' WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
			stmt.execute(query);
		}
	}
	
	public void iDontHave(String table, int id) throws SQLException {
		query = "UPDATE " + table + " SET ihave=0, tag=null WHERE id=" +id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET ihave=0, tag=null WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET ihave=0, tag=null WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
			stmt.execute(query);
		}
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
		Database db = new Database();
		
		Scanner input = new Scanner(System.in);
		System.out.print("Update Database? (y/n): ");
		String ans = input.nextLine();
		input.close();
		
		if(ans.equals("y")) {

			/* Update the database */
			System.out.print("Updating Database... ");
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
		}
		
		/* get info on a person by id number */
		System.out.println("Get Person");
		ResultSet resultSet = db.getPerson(2975);
		db.printResults(resultSet);
		
		System.out.println("");
		
		db.iHave("dance", 1670, "tag1");
				
		/* Search for dance */
		System.out.println("Search dance");
		resultSet = db.searchTableByName("dance", "dolphin", true);
		db.printResults(resultSet);
		
		db.iHave("dance", 100, "tag2");
		
		System.out.println("");
		
		db.iHave("album", 24, "album24");
		db.iHave("album", 247, "album247");
				
		/* Search for album */
		System.out.println("Search album");
		resultSet = db.searchTableByName("album", "Jimmy", true);
		db.printResults(resultSet);
		
		db.iHave("dance", 12188, "");
		
		System.out.println("");
		
		/* Search for recording */
		System.out.println("Search recording");
		resultSet = db.searchTableByName("recording", "frog", false);
		db.printResults(resultSet);
		
		System.out.println("");
		
		int album_id = 247;

		/* Search for name of some table its id */
		System.out.println("ALBUM:");
		resultSet = db.getNameByIdFromTable("album", album_id);
		db.printResults(resultSet);
		
		/* Search for records by album id */
		System.out.println("RECORDINGS:");
		resultSet = db.getRecordingsByAlbum(album_id);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search publication */
		System.out.println("Publication search:");
		resultSet = db.searchTableByName("publication", "flavour", false);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Last executed query: " + db.query);
		
		System.out.println("");
		
	}
}