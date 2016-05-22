package views;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import database.Database;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/*
 * Tests the Search Class and its methods
 */
public class ViewTest {
	
	private SearchDataView s;
	private Database db;
	private Main m;
	
	@Test
    public void testA() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
							new Main().start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						} 
                    }
                });
            }
        });
        thread.start();
        Thread.sleep(5000); //how long for thread to stop, currently 5s
    }

	@Test
	/*
	 * Tests the constructor for Search
	 */
	public void initialSearchTest() throws SQLException, IOException {
		db = new Database();
		m = new Main();
		s = new SearchDataView(db, m, false);
		//non-primitive reference fields should be initialized and not null
		//VBox
		assertNotNull(s.getSearch());
		assertNotNull(s.getDanceFiltersVBox());
		assertNotNull(s.getRecordingFiltersVBox());
		assertNotNull(s.getAlbumFiltersVBox());
		assertNotNull(s.getPublicationFiltersVBox());
		//Database
		assertNotNull(s.getDb());
		//RecordTable
		assertNotNull(s.getDanceTable());
		assertNotNull(s.getPublicationTable());
		assertNotNull(s.getRecordingTable());
		assertNotNull(s.getAlbumTable());
		//TextField
		assertNotNull(s.getSearch());
		//RadioButton
		assertNotNull(s.getAdvSF());
		
		//initial state should be Dance state
		assertEquals(s.getState(), "d");
		assertTrue(s.getDanceTable().getTable().isVisible());
		assertFalse(s.getPublicationTable().getTable().isVisible());
		assertFalse(s.getAlbumTable().getTable().isVisible());
		assertFalse(s.getRecordingTable().getTable().isVisible());
		
		//check for tables
		assertNotNull(s.getDanceTable());
		assertNotNull(s.getPublicationTable());
		assertNotNull(s.getRecordingTable());
		assertNotNull(s.getAlbumTable());
		
		db.close();
	}
}