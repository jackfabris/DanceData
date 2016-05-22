package database;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
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
				assertEquals(1, db.update());
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
				ResultSet searchTableByNameTest = db.searchTableByName("dance", searchByTableNameTestValue, false);

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
				db.iHave("recording", iHaveTestTagValue);
				ResultSet haveNoTag = db.getAllByIdFromTable("recording", iHaveTestTagValue);
				//System.out.println(haveNoTag.getString("iHave"));
				assertTrue(haveNoTag.getString("iHave").equals("1"));
				db.iDontHave("recording", iHaveTestTagValue);
				ResultSet donthaveNoTag = db.getAllByIdFromTable("recording", iHaveTestTagValue);
				assertTrue(donthaveNoTag.getString("iHave").equals("0"));
				db.iHave("recording", iHaveTestNoTagValue);
				db.addTag("recording", iHaveTestNoTagValue, "fav");
				ResultSet haveWithTag = db.getAllByIdFromTable("recording", iHaveTestNoTagValue);
				assertTrue(haveWithTag.getString("iHave").equals("1"));
				db.iDontHave("recording", iHaveTestNoTagValue);
				ResultSet donthaveWithTag = db.getAllByIdFromTable("recording", iHaveTestNoTagValue);
				assertTrue(donthaveWithTag.getString("iHave").equals("0"));
				db.close();
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
	
	@Test
	public void testGetFormationsByDance() {
		try {
			System.out.print("Testing getFormationsByDance... ");
			Database db = new Database();
			int dance_id = 18;
			List<Integer> formation_ids = Arrays.asList(10,27,43);
			ResultSet rs = db.getFormationsByDance(dance_id);
			while(rs.next()) {
				assertTrue(formation_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT f.* FROM formation f LEFT OUTER JOIN dancesformationsmap dfm "
					+ "ON f.id=dfm.formation_id WHERE dfm.dance_id=" + dance_id + " ORDER BY f.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPublicationsByDance() {
		try {
			System.out.print("Testing getPublicationsByDance... ");
			Database db = new Database();
			int dance_id = 497;
			List<Integer> publication_ids = Arrays.asList(818, 191, 74, 954, 803, 405, 179, 814, 1282);
			ResultSet rs = db.getPublicationsByDance(dance_id);
			while(rs.next()) {
				assertTrue(publication_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT p.*, pn.name as devisor FROM publication p "
					+ "LEFT OUTER JOIN person pn ON p.devisor_id=pn.id "
					+ "LEFT OUTER JOIN dancespublicationsmap dpm ON p.id=dpm.publication_id "
					+ "WHERE dpm.dance_id=" + dance_id + " ORDER BY p.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetPublicationsByPerson() {
		try {
			System.out.print("Testing getPublicationsByPerson... ");
			Database db = new Database();
			int person_id = 2;
			List<Integer> publication_ids = Arrays.asList(763);
			ResultSet rs = db.getPublicationsByPerson(person_id);
			while(rs.next()) {
				assertTrue(publication_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT p.*, pn.name as devisor FROM publication p "
					+ "LEFT OUTER JOIN person pn ON p.devisor_id=pn.id "
					+ "WHERE pn.id=" + person_id + " ORDER BY p.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDancesByPerson() {
		try {
			System.out.print("Testing getDancesByPerson... ");
			Database db = new Database();
			int person_id = 2975;
			List<Integer> dance_ids = Arrays.asList(12894, 10285);
			ResultSet rs = db.getDancesByPerson(person_id);
			while(rs.next()) {
				assertTrue(dance_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
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
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDancesByTune() {
		try {
			System.out.print("Testing getDancesByTune... ");
			Database db = new Database();
			int tune_id = 41;
			List<Integer> dance_ids = Arrays.asList(153, 1658, 10017, 3750, 7874, 6341, 13112, 6476, 6709);
			ResultSet rs = db.getDancesByTune(tune_id);
			while(rs.next()) {
				assertTrue(dance_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
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
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDancesByPublication() {
		try {
			System.out.print("Testing getDancesByPublication... ");
			Database db = new Database();
			int publication_id = 100;
			List<Integer> dance_ids = Arrays.asList(1229, 1314, 1810, 2043, 2139, 5722, 6081, 7260);
			ResultSet rs = db.getDancesByPublication(publication_id);
			while(rs.next()) {
				assertTrue(dance_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT d.*, dt.name as type, mt.description as medleytype, s.name as shape, "
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
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAlbumsByPerson() {
		try {
			System.out.print("Testing getAlbumsByPerson... ");
			Database db = new Database();
			int person_id = 3056;
			List<Integer> album_ids = Arrays.asList(301, 748, 257, 259, 189, 421, 419);
			ResultSet rs = db.getAlbumsByPerson(person_id);
			while(rs.next()) {
				assertTrue(album_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT a.*, p.name as artist FROM album a "
					+ "LEFT OUTER JOIN person p ON a.artist_id=p.id "
					+ "WHERE p.id=" + person_id + " ORDER BY a.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetAlbumByRecording() {
		try {
			System.out.print("Testing getAlbumByRecording... ");
			Database db = new Database();
			int recording_id = 1599;
			List<Integer> album_ids = Arrays.asList(247);
			ResultSet rs = db.getAlbumByRecording(recording_id);
			while(rs.next()) {
				assertTrue(album_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT a.*, p.name as artist FROM album a LEFT OUTER JOIN albumsrecordingsmap arm ON a.id=arm.album_id "
					+ "LEFT OUTER JOIN person p ON a.artist_id=p.id WHERE arm.recording_id=" + recording_id + " ORDER BY a.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetTunesByRecording() {
		try {
			System.out.print("Testing getTunesByRecording... ");
			Database db = new Database();
			int recording_id = 18;
			List<Integer> tune_ids = Arrays.asList(121, 1955, 1982, 1585);
			ResultSet rs = db.getTunesByRecording(recording_id);
			while(rs.next()) {
				assertTrue(tune_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN tunesrecordingsmap trm ON t.id=trm.tune_id "
					+ "LEFT OUTER JOIN person p ON t.composer_id=p.id WHERE trm.recording_id=" + recording_id + " ORDER BY t.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetTunesByPerson() {
		try {
			System.out.print("Testing getTunesByPerson... ");
			Database db = new Database();
			int person_id = 10;
			List<Integer> tune_ids = Arrays.asList(4573);
			ResultSet rs = db.getTunesByPerson(person_id);
			while(rs.next()) {
				assertTrue(tune_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT t.*, p.name as composer FROM tune t "
					+ "LEFT OUTER JOIN person p ON t.composer_id=p.id "
					+ "WHERE p.id=" + person_id + " ORDER BY t.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetTunesByPublication() {
		try {
			System.out.print("Testing getTunesByPublication... ");
			Database db = new Database();
			int publication_id = 18;
			List<Integer> tune_ids = Arrays.asList(338, 823, 886, 887, 912, 962, 1010, 1095, 1137, 1235, 1296, 1351, 1478, 1508, 1565);
			ResultSet rs = db.getTunesByPublication(publication_id);
			while(rs.next()) {
				assertTrue(tune_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN tunespublicationsmap tpm "
					+ "ON t.id=tpm.tune_id LEFT OUTER JOIN person p ON t.composer_id=p.id "
					+ "WHERE tpm.publication_id=" + publication_id + " ORDER BY t.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetTunesByDance() {
		try {
			System.out.print("Testing getTunesByDance... ");
			Database db = new Database();
			int dance_id = 18;
			List<Integer> tune_ids = Arrays.asList(1476);
			ResultSet rs = db.getTunesByDance(dance_id);
			while(rs.next()) {
				assertTrue(tune_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT t.*, p.name as composer FROM tune t LEFT OUTER JOIN dancestunesmap dtm ON t.id=dtm.tune_id "
					+ "LEFT OUTER JOIN person p ON t.composer_id=p.id WHERE dtm.dance_id=" + dance_id + " ORDER BY t.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetStepsByDance() {
		try {
			System.out.print("Testing getStepsByDance... ");
			Database db = new Database();
			int dance_id = 18;
			List<Integer> step_ids = Arrays.asList(1);
			ResultSet rs = db.getStepsByDance(dance_id);
			while(rs.next()) {
				assertTrue(step_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT s.* FROM step s LEFT OUTER JOIN dancesstepsmap dsm "
					+ "ON s.id=dsm.step_id WHERE dsm.dance_id=" + dance_id + " ORDER BY s.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRecordingsByPerson() {
		try {
			System.out.print("Testing getRecordingsByPerson... ");
			Database db = new Database();
			int person_id = 1456;
			List<Integer> recording_ids = Arrays.asList(8210, 3136, 8820, 3140, 8830, 1587, 1590, 1589, 1591, 8687, 3137, 8818, 3138, 1592, 1588, 3141, 8826, 1593, 3139, 8824, 3142, 8822);
			ResultSet rs = db.getRecordingsByPerson(person_id);
			while(rs.next()) {
				assertTrue(recording_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "WHERE pn.id=" + person_id + " ORDER BY r.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRecordingsByTune() {
		try {
			System.out.print("Testing getRecordingsByTune... ");
			Database db = new Database();
			int tune_id = 41;
			List<Integer> recording_ids = Arrays.asList(1783, 1974, 2153, 2381, 4579, 3003, 4977, 8295, 8533, 8737, 9636, 4697, 1077, 5622, 94, 3155, 2595, 1178, 5059, 3877);
			ResultSet rs = db.getRecordingsByTune(tune_id);
			while(rs.next()) {
				assertTrue(recording_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "LEFT OUTER JOIN tunesrecordingsmap trm ON r.id=trm.recording_id "
					+ "WHERE trm.tune_id=" + tune_id + " ORDER BY r.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRecordingsByDance() {
		try {
			System.out.print("Testing getRecordingsByDance... ");
			Database db = new Database();
			int dance_id = 10;
			List<Integer> recording_ids = Arrays.asList(3284);
			ResultSet rs = db.getRecordingsByDance(dance_id);
			while(rs.next()) {
				assertTrue(recording_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "LEFT OUTER JOIN dancesrecordingsmap drm ON r.id=drm.recording_id "
					+ "WHERE drm.dance_id=" + dance_id + " ORDER BY r.name";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetRecordingsByAlbum() {
		try {
			System.out.print("Testing getRecordingsByAlbum... ");
			Database db = new Database();
			int album_id = 247;
			List<Integer> recording_ids = Arrays.asList(1598, 1602, 1595, 1600, 1594, 23, 1599, 1601, 1596);
			ResultSet rs = db.getRecordingsByAlbum(album_id);
			while(rs.next()) {
				assertTrue(recording_ids.contains(Integer.parseInt(rs.getString("id"))));
			}
			String expectQ = "SELECT r.*, dt.name as type, mt.description as medleytype, p.name as phrasing, pn.name as artist, tracknumber "
					+ "FROM recording r LEFT OUTER JOIN dancetype dt ON r.type_id=dt.id "
					+ "LEFT OUTER JOIN medleytype mt ON r.medleytype_id=mt.id "
					+ "LEFT OUTER JOIN phrasing p ON r.phrasing_id=p.id "
					+ "LEFT OUTER JOIN albumsrecordingsmap arm ON r.id=arm.recording_id "
					+ "LEFT OUTER JOIN person pn ON r.artist_id=pn.id "
					+ "WHERE arm.album_id=" + album_id + " ORDER BY tracknumber";
			assertEquals(expectQ, db.getQuery());
			db.close();
			System.out.print("DONE\n");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
