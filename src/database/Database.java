package database;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

/**
 * This is the database class. It is a sqlite database that connects to the file
 * in the database folder called scddata.db. It contains many functions for getting
 * the required data from the database to be put on screen. It also contains a
 * function to update the database upon the user request.
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
	
	/**
	 * Initialize the database connection
	 * @throws SQLException
	 * @throws MalformedURLException
	 */
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
	 * Close down the databases
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if(stmt != null)
			stmt.close();
		if(connection != null)
			connection.close();
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
	
	/**
	 * After updating the database, load the collection information back into the database
	 * @throws IOException
	 * @throws SQLException
	 */
	public void loadIHave() throws IOException, SQLException {
		Iterator<String> iter = FileUtils.readLines(saveFile).iterator();
		String line;
		String[] info;
		while(iter.hasNext()) {
			line = iter.next();
			info = line.split(" ");
			if(info.length == 2 || info[2].equals("null"))
				query = "UPDATE " + info[0] + " SET ihave=1 WHERE id=" + info[1];
			else
				query = "UPDATE " + info[0] + " SET ihave=1, tag='" + info[2] + "' WHERE id=" + info[1];
			stmt.execute(query);
		}
	}
	
	/**
	 * Mark as having in personal collection. 
	 * @param table - the type of thing i have (can be album, recording, publication, or dance)
	 * @param id - the id 
	 * @throws SQLException
	 */
	public void iHave(String table, int id) throws SQLException {
		query = "UPDATE " + table + " SET ihave=1 WHERE id=" + id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET ihave=1 WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET ihave=1 WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
			stmt.execute(query);
		}
	}

	/**
	 * Mark as not having in personal collection
	 * @param table - the type (album, recording, publication, or dance)
	 * @param id - the id
	 * @throws SQLException
	 */
	public void iDontHave(String table, int id) throws SQLException {
		query = "UPDATE " + table + " SET ihave=0 WHERE id=" +id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET ihave=0 WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET ihave=0 WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
			stmt.execute(query);
		}
	}

	/**
	 * Give the item a tag
	 * @param table - the type (album, recording, publication, or dance)
	 * @param id - the id
	 * @param tag - the tag string
	 * @throws SQLException
	 */
	public void addTag(String table, int id, String tag) throws SQLException {
		query = "UPDATE " + table + " SET tag='" + tag + "' WHERE id=" + id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET tag='" + tag + "' WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET tag='" + tag + "' WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
			stmt.execute(query);
		}
	}

	/**
	 * Remove the items tag
	 * @param table - the type (album, recording, publicaiton, or dance)
	 * @param id - the id
	 * @throws SQLException 
	 */
	public void removeTag(String table, int id) throws SQLException {
		query = "UPDATE " + table + " SET tag=null WHERE id=" +id;
		stmt.execute(query);
		if(table.equals("publication")) {
			query = "UPDATE dance SET tag=null WHERE id in "
					+ "(SELECT dance_id FROM dancespublicationsmap WHERE publication_id=" + id + ")";
			stmt.execute(query);
		} else if(table.equals("album")) {
			query = "UPDATE recording SET tag=null WHERE id in "
					+ "(SELECT recording_id FROM albumsrecordingsmap WHERE album_id=" + id + ")";
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
	 * Get person's name from their id
	 * @param id
	 * @return String
	 * @throws SQLException
	 */
	public String getPersonName(int id) throws SQLException {
		query = "SELECT name FROM person WHERE id=" + id;
		return stmt.executeQuery(query).getString("name");
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
		name = name.replace("'", "''");
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
			query += " ORDER by name";
		} else if(table.equals("album")) {
			query = "SELECT a.*, p.name as artist FROM album a "
					+ "LEFT OUTER JOIN person p ON a.artist_id=p.id "
					+ "WHERE a.name like '%" + name + "%'";
			if(ihave) {
				query += " AND a.ihave=1";
			}
			query += " ORDER by name";
		} else if(table.equals("publication")) {
			query = "SELECT pb.*, pr.name as devisor FROM publication pb "
					+ "LEFT OUTER JOIN person pr ON pb.devisor_id=pr.id WHERE pb.name like '%" + name + "%'";
			if(ihave) {
				query += " AND pb.ihave=1";
			}
			query += " ORDER by name";
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
			query += " ORDER by name";
		} else {
			query = "SELECT * FROM " + table + " WHERE name like '%" + name + "%'";
			if(ihave) {
				query += " AND ihave=1";
			}
		}
		return stmt.executeQuery(query);
	}
	
	/**
	 * Search the table with specified advanced search params
	 * @param table - the table to search in
	 * @param name - the name to search for
	 * @param map - the mapping of keys and values in the advanced search
	 * @param ihave - if true only show what is marked as ihave, otherwise show all results
	 * @return ResultSet
	 * @throws SQLException
	 */
	
	public ResultSet advancedTableSearch(String table, String name, Map<String,String> map, boolean ihave) throws SQLException{
		if(table.equals("dance")) {
			query = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
					+ "c.name as couples, p.name as progression, pb.name as publication, pn.name as devisor, "
					+ "f.name as formations, st.name as steps FROM dance d "
					+ "LEFT OUTER JOIN dancetype dt ON d.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON d.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN shape s ON d.shape_id=s.id "
					+ "LEFT OUTER JOIN couples c ON d.couples_id=c.id "
					+ "LEFT OUTER JOIN progression p ON d.progression_id=p.id "
					+ "LEFT OUTER JOIN dancespublicationsmap dpm ON d.id=dpm.dance_id "
					+ "LEFT OUTER JOIN publication pb ON dpm.publication_id=pb.id "
					+ "LEFT OUTER JOIN person pn ON d.devisor_id=pn.id "
					+ "LEFT OUTER JOIN dancesformationsmap dfm ON d.id=dfm.dance_id "
					+ "LEFT OUTER JOIN formation f ON dfm.formation_id=f.id "
					+ "LEFT OUTER JOIN dancesstepmap dsm ON d.id=dfm.dance_id "
					+ "LEFT OUTER JOIN step st ON dsm.step=st.id ";
			//"SELECT f.* FROM formation f LEFT OUTER JOIN dancesformationsmap dfm " + "ON f.id=dfm.formation_id WHERE dfm.dance_id=" + dance_id;
			if (name.length() != 0)
				query += "WHERE d.name like '%" + name + "%'";
			else
				query += "WHERE d.name like '%%'";
			Object[] keys = map.keySet().toArray();
			for (int i=0; keys.length>i; i++){
				String param = (String)keys[i];
				String val = map.get(keys[i]);
				if(!val.isEmpty()){
					if (param.equals("bars")){
						query += " AND d.barsperrepeat"+val;
					}
					else if (param.equals("author")){
						query += " AND pn.name like '%"+val+"%'";
					}
					else if (param.equals("type")){
						query += " AND dt.name='"+ val +"'";
					}
					else if (param.equals("couples")){
						query += " AND c.name='"+ val +"'";
					}
					else if (param.equals("shape")){
						query += " AND s.name='"+ val +"'";
					}
					//parser for formation search
					else if (param.equals("formation") || param.equals("steps")){
						boolean threeParams = false; //boolean for situations where there's more than one 
						//System.out.println(val);
						//double trailing and/or/not check
						String doubleCheck = val.substring(val.length()-9);
						if (doubleCheck.contains("or 'or")){
							val = val.substring(0, val.length()-8);
						}
						if (doubleCheck.contains("or 'and") || doubleCheck.contains("and 'or") || doubleCheck.contains("not 'or") || doubleCheck.contains("or 'not")){
							val = val.substring(0, val.length()-9);
						}
						else if (doubleCheck.contains("and 'and") || doubleCheck.contains("not 'and") || doubleCheck.contains("and 'not") || doubleCheck.contains("not 'not")){
							val = val.substring(0, val.length()-10);
						}
						//separator for and/not/or
						if (val.toLowerCase().contains("'or '") || val.toLowerCase().contains("'and '") || val.toLowerCase().contains("'not '")){
							for (int k =0; k+6<val.length();k++){
								String andnotCheck = val.substring(k, k+6);
								String orCheck = val.substring(k, k+5);
								if(andnotCheck.equals("'and '")){
									if (threeParams){
										if (param.equals("formation"))
											val = val.substring(0, k-1) + "' " + val.substring(k+1, k+5) + "f.name='" + val.substring(k+6,val.length()-1) +")";
										else { //steps
											val = val.substring(0, k-1) + "' " + val.substring(k+1, k+5) + "st.name='" + val.substring(k+6,val.length()-1) +")";
										}
									}
									else {
										if (param.equals("formation"))
											val = "(f.name="+val.substring(0, k-1) + "' " + val.substring(k+1, k+5) + "f.name='" + val.substring(k+6,val.length()-1) + "')";
										else{ //steps
											val = "(st.name="+val.substring(0, k-1) + "' " + val.substring(k+1, k+5) + "st.name='" + val.substring(k+6,val.length()-1) + "')";
										}
										threeParams = true;
									}
									//System.out.println("1");
								}
								if (andnotCheck.equals("'not '")){
									if (threeParams){
										if (param.equals("formation"))
											val = val.substring(0, k-1) + "' and (" + val.substring(k+1, k+5) + "f.name='" + val.substring(k+6,val.length()-1) +"))";
										else { //steps
											val = val.substring(0, k-1) + "' and (" + val.substring(k+1, k+5) + "st.name='" + val.substring(k+6,val.length()-1) +"))";
										}
									}
									else {
										if (param.equals("formation"))
											val = "(f.name="+val.substring(0, k-1) + "' and " + val.substring(k+1, k+5) + "f.name='" + val.substring(k+6,val.length()-1) + "')";
										else{ //steps
											val = "(st.name="+val.substring(0, k-1) + "' and " + val.substring(k+1, k+5) + "st.name='" + val.substring(k+6,val.length()-1) + "')";
										}
										threeParams = true;
									}
								}
								if (orCheck.equals("'or '")){
									if (threeParams)
										val = val.substring(0, k-1) + "' " + val.substring(k+1, k+4) + "f.name='"+val.substring(k+5,val.length()-1)+")";
									else {
										val = "(f.name=" + val.substring(0, k-1) + "' " + val.substring(k+1, k+4) + "f.name='"+val.substring(k+5,val.length()-1)+"')";
										threeParams = true;
									}
								}
							}
							System.out.println("Before extra and/or check: " + val);
						}
						//check for single trailing and/or/not
						int lastChar = val.length()-1; //index of last character in val string (for trailing or/and/nor)(-1 due to space)
						String orCheck1 = val.substring(lastChar-2, lastChar);
						String orCheck2 = val.substring(lastChar-3, lastChar-1);
						String andnotCheck1 = val.substring(lastChar-3, lastChar);
						String andnotCheck2 = val.substring(lastChar-4, lastChar-1);
						if (orCheck1.equals("or")){ //check for extra or (only one formation param)
							//System.out.println(val.substring(lastChar-2, lastChar));
							query += " AND f.name="+val.substring(0, lastChar-4) +"'";
						}
						else if (orCheck2.equals("or")){ //check for extra or (multiple formation params)
							query += " AND "+val.substring(0, lastChar-5) +"')";
						}
						else if (andnotCheck1.equals("and") || andnotCheck1.equals("not")){ //check for extra and/not (only one formation param)
							query += " AND f.name="+val.substring(0, lastChar-5) +"'";
						}
						else if (andnotCheck2.equals("and") || andnotCheck2.equals("not")){ //check for extra and/not (multiple formation params)
							query += " AND "+val.substring(0, lastChar-6) +"')";
						}
						else {
							if (threeParams)
								query += " AND " + val;
							else {
								System.out.println("1");
								query += " AND f.name="+val.substring(0, val.length()-1)+"'";
							}
						}
//					else {
//						System.out.println("1");
//						query += " AND f.name="+val.substring(0, val.length()-1)+"'";
//					}
					}
				}
			}
			if(ihave) {
				query += " AND d.ihave=1";
			}
			query += " GROUP by d.name, publication";
		}
		else if(table.equals("publication")) {
			query = "SELECT pb.*, pr.name as devisor FROM publication pb "
					+ "LEFT OUTER JOIN person pr ON pb.devisor_id=pr.id WHERE pb.name like '%" + name + "%'";
			String author = map.get("author");
			if (!author.isEmpty())
				query += " AND pr.name like '%"+author+"%'";
			if(ihave) {
				query += " AND pb.ihave=1";
			}
			query += " ORDER by name";
		}
		else if(table.equals("recording")){
			query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "WHERE r.name like '%" + name + "%'";
			String type = map.get("type");
			String medley = map.get("medley type");
			String repetitions = map.get("repetitions");
			String bars = map.get("bars");
			if (type != null)
				if (!type.isEmpty())
					query += " AND dt.name='"+type+"'";
			if (medley != null)
				if (!medley.isEmpty())
					query += " AND mt.description='"+medley+"'";
			if (repetitions != null)
				if (!repetitions.isEmpty())
					query += " AND r.repetitions"+repetitions;
			if (bars != null)
				if (!bars.isEmpty())
					query += " AND r.barsperrepeat"+bars;
			if(ihave) {
				query += " AND r.ihave=1";
			}
			query += " ORDER by name";
		} 
		else if(table.equals("album")) {
			query = "SELECT a.*, p.name as artist FROM album a "
					+ "LEFT OUTER JOIN person p ON a.artist_id=p.id "
					+ "WHERE a.name like '%" + name + "%'";
			String artist = map.get("artist_id");
			String year = map.get("productionyear");
			if (artist != null)
				if(!artist.isEmpty())
					query += " AND p.name like '%"+artist+"%'";
			if (year != null)
				if (!year.isEmpty())
					query += " AND a.productionyear="+year;
			if(ihave) {
				query += " AND a.ihave=1";
			}
			query += " ORDER by name";
		}
		else {
			query = "SELECT * FROM " + table + " WHERE name like '%" + name + "%'";
			if(ihave) {
				query += " AND ihave=1";
			}
		}
		System.out.println(query);
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
		query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist, tracknumber "
				+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
				+ "LEFT OUTER JOIN albumsrecordingsmap arm ON r.id=arm.recording_id "
				+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
				+ "WHERE arm.album_id=" + album_id + " ORDER BY tracknumber";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all recordings for a given dance
	 * @param dance_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getRecordingsByDance(int dance_id) throws SQLException {
		query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
				+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
				+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
				+ "LEFT OUTER JOIN dancesrecordingsmap drm ON r.id=drm.recording_id "
				+ "WHERE drm.dance_id=" + dance_id + " ORDER BY r.name";
		return stmt.executeQuery(query);
	}

	/** 
	 * Get recordings for the given tune
	 * @param tune_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getRecordingsByTune(int tune_id) throws SQLException {
		query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
				+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
				+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
				+ "LEFT OUTER JOIN tunesrecordingsmap trm ON r.id=trm.recording_id "
				+ "WHERE trm.tune_id=" + tune_id + " ORDER BY r.name";
		return stmt.executeQuery(query);
	}

	/**
	 * Get all recordings by the artist with person_id
	 * @param person_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getRecordingsByPerson(int person_id) throws SQLException {
		query = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
				+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
				+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
				+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
				+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
				+ "WHERE pn.id=" + person_id + " ORDER BY r.name";
		return stmt.executeQuery(query);
	}

	/**
	 * Get all the steps for a given dance
	 * @param dance_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getStepsByDance(int dance_id) throws SQLException {
		query = "SELECT s.* FROM step s LEFT OUTER JOIN dancesstepsmap dsm "
				+ "ON s.id=dsm.step_id WHERE dsm.dance_id=" + dance_id + " ORDER BY s.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all the tunes for a given dance
	 * @param dance_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getTunesByDance(int dance_id) throws SQLException {
		query = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN dancestunesmap dtm ON t.id=dtm.tune_id "
				+ "LEFT OUTER JOIN person p ON t.composer_id=p.id WHERE dtm.dance_id=" + dance_id + " ORDER BY t.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get a list of tues in the publication with publication_id
	 * @param publication_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getTunesByPublication(int publication_id) throws SQLException {
		query = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN tunespublicationsmap tpm "
				+ "ON t.id=tpm.tune_id LEFT OUTER JOIN person p ON t.composer_id=p.id "
				+ "WHERE tpm.publication_id=" + publication_id + " ORDER BY t.name";
		return stmt.executeQuery(query);
	}

	/**
	 * Get all tunes composed by the person with person_id
	 * @param person_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getTunesByPerson(int person_id) throws SQLException {
		query = "SELECT t.*, p.name as composer FROM tune t "
				+ "LEFT OUTER JOIN person p ON t.composer_id=p.id "
				+ "WHERE p.id=" + person_id + " ORDER BY t.name";
		return stmt.executeQuery(query);
	}

	/**
	 * Get all tunes for a given recording
	 * @param recording_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getTunesByRecording(int recording_id) throws SQLException {
		query = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN tunesrecordingsmap trm ON t.id=trm.tune_id "
				+ "LEFT OUTER JOIN person p ON t.composer_id=p.id WHERE trm.recording_id=" + recording_id + " ORDER BY t.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get album for a given recording
	 * @param recording_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getAlbumByRecording(int recording_id) throws SQLException {
		query = "SELECT a.*, p.name as artist FROM album a LEFT OUTER JOIN albumsrecordingsmap arm ON a.id=arm.album_id "
				+ "LEFT OUTER JOIN person p ON a.artist_id=p.id WHERE arm.recording_id=" + recording_id + " ORDER BY a.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all albums by the artist with person_id
	 * @param person_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getAlbumsByPerson(int person_id) throws SQLException {
		query = "SELECT a.*, p.name as artist FROM album a "
				+ "LEFT OUTER JOIN person p ON a.artist_id=p.id "
				+ "WHERE p.id=" + person_id + " ORDER BY a.name";
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
				+ "WHERE dpm.publication_id=" + publication_id + " ORDER BY d.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get the dances for a given tune
	 * @param tune_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getDancesByTune(int tune_id) throws SQLException {
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
				+ "LEFT OUTER JOIN dancestunesmap dtm ON d.id=dtm.dance_id "
				+ "WHERE dtm.tune_id=" + tune_id + " ORDER BY d.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all dances devised by the person with person_id
	 * @param person_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getDancesByPerson(int person_id) throws SQLException {
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
				+ "WHERE pn.id=" + person_id + " ORDER BY d.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all publications devised by the person with person_id
	 * @param person_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getPublicationsByPerson(int person_id) throws SQLException {
		query = "SELECT p.*, pn.name as devisor FROM publication p "
				+ "LEFT OUTER JOIN person pn ON p.devisor_id=pn.id "
				+ "WHERE pn.id=" + person_id + " ORDER BY p.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all publications that the dance is found in
	 * @param dance_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getPublicationsByDance(int dance_id) throws SQLException {
		query = "SELECT p.*, pn.name as devisor FROM publication p "
				+ "LEFT OUTER JOIN person pn ON p.devisor_id=pn.id "
				+ "LEFT OUTER JOIN dancespublicationsmap dpm ON p.id=dpm.publication_id "
				+ "WHERE dpm.dance_id=" + dance_id + " ORDER BY p.name";
		return stmt.executeQuery(query);
	}
	
	/**
	 * Get all the formations for a given dance
	 * @param dance_id
	 * @return ResultSet
	 * @throws SQLException
	 */
	public ResultSet getFormationsByDance(int dance_id) throws SQLException {
		query = "SELECT f.* FROM formation f LEFT OUTER JOIN dancesformationsmap dfm "
				+ "ON f.id=dfm.formation_id WHERE dfm.dance_id=" + dance_id + " ORDER BY f.name";
		return stmt.executeQuery(query);
	}

	public ResultSet doQuery(String s) throws SQLException {
		query = s;
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
		
		/* Get person name */
		System.out.println("Person Name:");
		System.out.println(db.getPersonName(2975));
		
		System.out.println("");
		
		db.iHave("dance", 1670);
		db.addTag("dance", 1670, "tag1");
				
		/* Search for dance */
		System.out.println("Search dance");
		resultSet = db.searchTableByName("dance", "dolphin", true);
		db.printResults(resultSet);
		
		db.iHave("dance", 100);
		db.addTag("dance", 100, "tag2");
		
		System.out.println("");
		
		db.iHave("album", 24);
		db.addTag("album", 24, "album24");
		db.iHave("album", 247);
		db.addTag("album", 247, "album247");
				
		/* Search for album */
		System.out.println("Search album");
		resultSet = db.searchTableByName("album", "Jimmy", true);
		db.printResults(resultSet);
		
		db.iHave("dance", 12188);
		
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
		
		/* Get album by recording */
		System.out.println("Album by recording:");
		resultSet = db.getAlbumByRecording(1599);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Search publication */
		System.out.println("Publication search:");
		resultSet = db.searchTableByName("publication", "flavour", false);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get dances by publication */
		System.out.println("Dances by publication:");
		resultSet = db.getDancesByPublication(100);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get formations by dance */
		System.out.println("Formations by dance:");
		resultSet = db.getFormationsByDance(18);
		db.printResults(resultSet);
		
		System.out.println("");

		/* Get steps by dance */
		System.out.println("Steps by dance:");
		resultSet = db.getStepsByDance(18);
		db.printResults(resultSet);
		
		System.out.println("");

		/* Get tunes by dance */
		System.out.println("Tunes by dance:");
		resultSet = db.getTunesByDance(18);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get recordings by dance */
		System.out.println("Recordings by dance:");
		resultSet = db.getRecordingsByDance(10);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get tunes by recording */
		System.out.println("Tunes by recording:");
		resultSet = db.getTunesByRecording(18);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get tunes by publication */
		System.out.println("Tunes by publication");
		resultSet = db.getTunesByPublication(18);
		db.printResults(resultSet);
		
		System.out.println("");
		
		/* Get dances by tune */
		System.out.println("Dances by tune:");
		resultSet = db.getDancesByTune(41);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Recordings by tune:");
		resultSet = db.getRecordingsByTune(41);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Dances by person:");
		resultSet = db.getDancesByPerson(2975);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Publications by person:");
		resultSet = db.getPublicationsByPerson(2);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Tunes by person:");
		resultSet = db.getTunesByPerson(10);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Recordings by person:");
		resultSet = db.getRecordingsByPerson(3056);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Albums by person:");
		resultSet = db.getAlbumsByPerson(3056);
		db.printResults(resultSet);
		
		System.out.println("");
		
		System.out.println("Publication by dance:");
		resultSet = db.getPublicationsByDance(497);
		db.printResults(resultSet);
		
		System.out.println("");
				
		System.out.println("Last executed query: " + db.query);
		
		System.out.println("");
		
		System.out.println("Close database");
		db.close();
		
	}
}