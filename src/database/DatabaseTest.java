package database;

import static org.junit.Assert.*;
import java.sql.SQLException;
import org.junit.Test;

public class DatabaseTest {

	@Test
	public void DatabaseTests() throws SQLException {
		try {
			Database db = new Database();
		}
		catch (Exception e){
			System.out.println("Error creating database");
		}
		
	}

}
