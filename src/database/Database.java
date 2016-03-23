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
}
