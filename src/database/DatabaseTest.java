package database;

import static org.junit.Assert.*;
import java.sql.SQLException;
import org.junit.Test;

public class DatabaseTest {

	@Test
	public void advancedSearchQueryBuildTest() throws SQLException {
		Database db = new Database();
		String[] testSelections = {"Album", "Artist", "Year"};
		String[] testValues = {"", "Mark", "1998"};
		assertTrue(db.advancedSearchQueryBuild(testSelections, testValues).equals("SELECT  * FROM dance where Artist like 'Mike' AND Year like '1998'"));
	}

}
