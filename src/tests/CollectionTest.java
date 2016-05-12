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
public class CollectionTest {
	
	private SearchDataView c;
	
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
		c = new SearchDataView(true);
		//non-primitive reference fields should be initialized and not null
		//VBox
		assertNotNull(c.getVBox());
		assertNotNull(c.getDanceFiltersVBox());
		assertNotNull(c.getRecordingFiltersVBox());
		assertNotNull(c.getAlbumFiltersVBox());
		assertNotNull(c.getPublicationFiltersVBox());
		//Database
		assertNotNull(c.getDb());
		//RecordTable
		assertNotNull(c.getDanceTable());
		assertNotNull(c.getPublicationTable());
		assertNotNull(c.getRecordingTable());
		assertNotNull(c.getAlbumTable());
		//TextField
		assertNotNull(c.getSearch());
		//RadioButton
		assertNotNull(c.getAdvSF());
		
		//initial state should be Dance state
		assertEquals(c.getState(), "d");
		assertTrue(c.getDanceTable().getTable().isVisible());
		assertFalse(c.getPublicationTable().getTable().isVisible());
		assertFalse(c.getAlbumTable().getTable().isVisible());
		assertFalse(c.getRecordingTable().getTable().isVisible());
		
		assertTrue(c.isCollection());
	}
	
	/*
	 * Tests the setUpSearchBar() method
	 */
	@Test
	public void setUpSearchBarTest() throws SQLException, MalformedURLException{
		c = new SearchDataView(true);
		c.setUpSearchBar();
		assertEquals(c.getSearch().getPromptText(), "Search by Dance Title");
		assertEquals(c.getSearch().getPrefWidth(), 300, .01);
	}
}
