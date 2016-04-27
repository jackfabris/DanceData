package database;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

public class DatabaseTest {

	@Test
	public void DatabaseTests() throws SQLException {
		//arbitrary test values
		int getPersonTestValue = 3;
		String searchByTableNameTestValue = "shining";
		int getNameByIdFromTableTestValue = 11;
		
		
		//initialization test
		try {
			System.out.println("initializing database...");
			Database db = new Database();
			System.out.println("INITIALIZATION SUCCESSFUL");
			
			//testing update
			try {
				System.out.println("updating database...");
				assertEquals(db.update(),1);
				System.out.println("UPDATE SUCCESSFUL");
			}
			catch(Exception e){
				System.out.println("Error updating database");
				System.out.println(e);
			}
			
			//test iHave database alterations
			try {
				System.out.println("altering tables for iHave...");
				db.addIHaveTagColumns();
				System.out.println("IHAVE ALTERING SUCCESSFUL");
			}
			catch (SQLException e){
				System.out.println("iHave columns already created! Continuing...");
				//System.out.println(e);
			}
			catch(Exception e){
				System.out.println("Error altering tables for iHave");
				System.out.println(e);
			}
			
			//test saving to db file/ loading to tables
			try {
				System.out.println("saving iHave info to file...");
				db.saveIHave();
				System.out.println("IHAVE SAVE SUCESSFUL");
				System.out.println("loading to tables ...");
				db.loadIHave();
				System.out.println("IHAVE LOADING SUCCESSFUL");
			}
			catch(Exception e){
				System.out.println("Error saving/loading tables for iHave");
				System.out.println(e);
			}
			
			//test SQL queries
			try {
				System.out.println("Testing SQL functions...");
				System.out.print("Testing getPerson function... ");
				assertTrue(db.getPerson(getPersonTestValue).getString("name").equals("Adams, Florence H"));
				System.out.println("DONE");
				
				System.out.print("Testing searchByTableName function... ");
				ResultSet searchTableByNameTest = db.searchTableByName("dance", searchByTableNameTestValue, false);
				while (searchTableByNameTest.next()){
					//System.out.println(searchTableByNameTest.getString("name"));
					assertTrue(searchTableByNameTest.getString("name").toLowerCase().contains("shining"));
				}
				System.out.println("DONE");
				
				System.out.print("Testing getNameByIdFromTable function... ");
				assertTrue(db.getNameByIdFromTable("dance", getNameByIdFromTableTestValue).getString("name").equals("Aberbrothock"));
				System.out.println("DONE");
			}
			catch(Exception e){
				System.out.println("Error getting person");
				System.out.println(e);
			}
			
		}
		catch (Exception e){
			System.out.println("Error creating database,");
			System.out.println(e);
		}
		
	}

}
