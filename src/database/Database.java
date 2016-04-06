package database;

import java.sql.*;

public class Database {
	
	private Connection connection;
	
	public Database() throws SQLException {
		connection = connect();
	}
	
	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:database/scddata.db");
	}
	
	public ResultSet getPerson(int id) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM person WHERE id="+ id + ";";
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public ResultSet searchDanceByTitle(String searchString) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		String query = "SELECT * FROM dance WHERE name like '%" + searchString + "%'";
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

//	// search function for searching by album, type, name, shape, and author
//	public ResultSet searchBy(String selection, String searchString) throws SQLException{
//		Statement stmt = connection.createStatement();
//		stmt.setQueryTimeout(30);
//		String query = "";
//		if (selection.equals("album")){
//			query = "SELECT * FROM dance WHERE album like '%" + searchString + "%'";
//		}
//		if (selection.equals("dance")){
//			query = "SELECT * FROM dance WHERE type like '%" + searchString + "%'";
//		}
//		if (selection.equals("title")){
//			query = "SELECT * FROM dance WHERE name like '%" + searchString + "%'";
//		}
//		if (selection.equals("shape")){
//			query = "SELECT * FROM dance WHERE shape like '%" + searchString + "%'";
//		}
//		if (selection.equals("author")){
//			query = "SELECT * FROM dance WHERE author like '%" + searchString + "%'";
//		}
//		ResultSet rs = stmt.executeQuery(query);
//		return rs;
//	}
	
	//takes in an array of search choices (i.e. [artist, song, year]) and an array of search string values respective to the choices
	//"selections" may eventually be set in stone once all the possible parameters for advanced search are decided
	public ResultSet advancedSearch(String[] selections, String[] searchStrings) throws SQLException{
		Statement stmt = connection.createStatement();
		stmt.setQueryTimeout(30);
		int fieldCounter = 0; //used to check if there is more than one field being searched so and is/isn't appended
		String query = "SELECT  * FROM dance where ";
		for (int i = 0; i <selections.length; i++){
			if (!selections[i].isEmpty()){
				if (fieldCounter > 0)
					query += " AND ";
				query += selections[i] + " like %" + searchStrings[i] + "%";
				fieldCounter++;
			}
		}
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
}
