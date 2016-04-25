package database;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DatabaseTest {

	@Test
	public void DatabaseTests() throws SQLException {
		//arbitrary test values
		int getPersonTestValue = 3;
		String getPersonTestEquals = "Adams, Florence H";
		
		String searchByTableNameTestValue = "shining";
		
		int getNameByIdFromTableTestValue = 11;
		String getNameByIdFromTableTestEquals = "Aberbrothock";
		
		int getAllByIdFromTableTestValue = 11;
		String getAllByIdFromTableTestEquals1 = "Aberbrothock";
		String getAllByIdFromTableTestEquals2 = "4";
		
		int getRecordingsByAlbumTestValue = 4;
		List<Integer> getRecordingsByAlbumTestEquals = Arrays.asList(8,14,34,45,91,116,128,147,286,366);
		
		int iHaveTestTagValue = 12;
		int iHaveTestNoTagValue = 134;
		
		
		//initialization test
		try {
			String currentMethod = "";
			System.out.print("initializing database...");
			Database db = new Database();
			System.out.println("INITIALIZATION SUCCESSFUL");
			
			//testing update
			try {
				System.out.print("updating database...");
				assertEquals(db.update(),1);
				System.out.println("UPDATE SUCCESSFUL\n");
			}
			catch(Exception e){
				System.out.println("Error updating database");
				System.out.println(e);
			}
			
			//test iHave database alterations
			try {
				System.out.print("altering tables for iHave...");
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
				System.out.print("saving iHave info to file...");
				db.saveIHave();
				System.out.println("IHAVE SAVE SUCESSFUL");
				System.out.print("loading to tables ...");
				db.loadIHave();
				System.out.println("IHAVE LOADING SUCCESSFUL\n");
			}
			catch(Exception e){
				System.out.println("Error saving/loading tables for iHave");
				System.out.println(e);
			}
			
			//test SQL queries
			try {
				System.out.println("Testing SQL functions...\n");
				System.out.print("Testing getPerson function... ");
				currentMethod = "getPerson";
				assertTrue(db.getPerson(getPersonTestValue).getString("name").equals(getPersonTestEquals));
				System.out.println("DONE");
				
				System.out.print("Testing searchByTableName function... ");
				currentMethod = "searchByTableName";
				ResultSet searchTableByNameTest = db.searchTableByName("dance", searchByTableNameTestValue);
				while (searchTableByNameTest.next()){
					//System.out.println(searchTableByNameTest.getString("name"));
					assertTrue(searchTableByNameTest.getString("name").toLowerCase().contains(searchByTableNameTestValue));
				}
				System.out.println("DONE");
				
				System.out.print("Testing getNameByIdFromTable function... ");
				currentMethod = "getNameByIdFromTable";
				assertTrue(db.getNameByIdFromTable("dance", getNameByIdFromTableTestValue).getString("name").equals(getNameByIdFromTableTestEquals));
				System.out.println("DONE");
				
				System.out.print("Testing getAllByIdFromTable function... ");
				currentMethod = "getAllByIdFromTable";
				assertTrue(db.getAllByIdFromTable("dance", getAllByIdFromTableTestValue).getString("name").equals(getAllByIdFromTableTestEquals1));
				assertTrue(db.getAllByIdFromTable("dance", getAllByIdFromTableTestValue).getString("shape_id").equals(getAllByIdFromTableTestEquals2));
				System.out.println("DONE");
				
				System.out.print("Testing getRecordingsByAlbum function... ");
				currentMethod = "getRecordingsByAlbum";
				ResultSet getRecordingsByAlbumTest = db.getRecordingsByAlbum(getRecordingsByAlbumTestValue);
				while (getRecordingsByAlbumTest.next()){
					//System.out.println(Integer.parseInt(getRecordingsByAlbumTest.getString("id")));
					//System.out.println(Arrays.asList(getRecordingsByAlbumTestEquals).get(0).contains(8));
					assertTrue(Arrays.asList(getRecordingsByAlbumTestEquals).get(0).contains(Integer.parseInt(getRecordingsByAlbumTest.getString("id"))));
				}
				System.out.println("DONE");
				
				System.out.print("Testing iHave/iDontHave functions... ");
				currentMethod = "iHave";
				db.iHave("recording", iHaveTestTagValue, "");
				ResultSet haveNoTag = db.getAllByIdFromTable("recording", iHaveTestTagValue);
				//System.out.println(haveNoTag.getString("iHave"));
				assertTrue(haveNoTag.getString("iHave").equals("1"));
				db.iDontHave("recording", iHaveTestTagValue);
				ResultSet donthaveNoTag = db.getAllByIdFromTable("recording", iHaveTestTagValue);
				assertTrue(donthaveNoTag.getString("iHave").equals("0"));
				db.iHave("recording", iHaveTestNoTagValue, "fav");
				ResultSet haveWithTag = db.getAllByIdFromTable("recording", iHaveTestNoTagValue);
				assertTrue(haveWithTag.getString("iHave").equals("1"));
				db.iDontHave("recording", iHaveTestNoTagValue);
				ResultSet donthaveWithTag = db.getAllByIdFromTable("recording", iHaveTestNoTagValue);
				assertTrue(donthaveWithTag.getString("iHave").equals("0"));
				System.out.println("DONE");
				
				
			}
			catch(Exception e){
				System.out.println("\nError in function " + currentMethod);
				System.out.println(e);
			}
			
			
		}
		catch (Exception e){
			System.out.println("Error creating database,");
			System.out.println(e);
		}
		
	}

}
