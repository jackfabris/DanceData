package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tables.RecordTable;
import views.SearchDataView;

/**
 * AlbumFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conduction an album search, such as specifying
 * an artist name or year of production
 * @author Jack
 *
 */
public class AlbumFilters extends AdvancedFilters {

	private TextField artistField, yearField;

	/**
	 * Create the VBox which will contain the filters for an album search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public AlbumFilters(Database db, SearchDataView sc) throws MalformedURLException, SQLException {
		super(db, sc, "album");
		artist();
		year();
		super.goAndClearButtons();
	}

	@Override
	/**
	 * Sets the text of the Album Title and Search Bar
	 */
	public void setTitleText(String text){
		SearchCollection.setAlbumTitle(text);
		SearchCollection.getSearch().setText(text);
	}

	/**
	 * Sets up the Artist options for Album
	 */
	public void artist(){
		map.put("artist_id", "");
		Label artist = new Label("Artist");
		artistField = new TextField();
		//Search on ENTER
		artistField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("artist_id", artistField.getText());
				callQuery();
			}
		});
		//Commit on Leave
		artistField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue.booleanValue())
					map.put("artist_id", artistField.getText());
			}
		});
		gridY++;
		grid.add(artist, 0, gridY);
		grid.add(artistField, 1, gridY);
	}

	/**
	 * Sets up the Year options for Album
	 */
	public void year(){
		map.put("productionyear", "");
		Label year = new Label("Year");
		yearField = new TextField();
		//Search on ENTER
		yearField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(!isNumeric(yearField.getText())) map.put("productionyear", "9999");
				else map.put("productionyear", yearField.getText());
				callQuery();
			}
		});
		//Commit on Leave
		yearField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue.booleanValue())
					if(!isNumeric(yearField.getText())) map.put("productionyear", "9999");
					else map.put("productionyear", yearField.getText());
			}
		});
		gridY++;
		grid.add(year, 0, gridY);
		grid.add(yearField, 1, gridY);
	}

	@Override
	/**
	 * Call the Advance Search query, populate the table, set visibility so that the 
	 * Album table is showing and everything else is hidden
	 */
	public void callQuery(){
		try {
			ResultSet data = db.advancedTableSearch("album", titleField.getText(), map, SearchCollection.isCollection());
			RecordTable albumTable = SearchCollection.getAlbumTable();
			albumTable.setTableData(albumTable.populate(data));
			albumTable.getCellInfo().setVisible(false);
			albumTable.getCellInfo().setVis(false);
			albumTable.getTable().setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * Clear the fields, clear the map, reset the titles, reset the table
	 */
	public void clear(){
		//clear fields
		titleField.clear();
		artistField.clear();
		yearField.clear();

		SearchCollection.setAlbumTitle("");
		SearchCollection.getSearch().clear();
		SearchCollection.getSearch().setPromptText("Search by Album Title");

		//clear map
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			map.put(x, "");
		}

		//reset table
		ResultSet data;
		try {
			data = db.searchTableByName("album", "", SearchCollection.isCollection());
			RecordTable albumTable = SearchCollection.getAlbumTable();
			albumTable.setTableData(albumTable.populate(data));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}