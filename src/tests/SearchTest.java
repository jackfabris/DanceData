package tests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.sql.SQLException;

import org.junit.Test;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import views.Main;
import views.SearchDataView;

/*
 * Tests the Search Class and its methods
 */
public class SearchTest {
	
	private SearchDataView s;
	
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
	public void initialSearchTest() throws SQLException, MalformedURLException {
		s = new SearchDataView(false);
		//non-primitive reference fields should be initialized and not null
		//VBox
		assertNotNull(s.getVBox());
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
		
		assertFalse(s.isCollection());
	}
	
	/*
	 * Tests the setUpSearchBar() method
	 */
	@Test
	public void setUpSearchBarTest() throws SQLException, MalformedURLException{
		s = new SearchDataView(false);
		s.setUpSearchBar();
		assertEquals(s.getSearch().getPromptText(), "Search by Dance Title");
		assertEquals(s.getSearch().getPrefWidth(), 300, .01);
	}
}
