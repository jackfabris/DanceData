package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
	
	public void artist(){
		map.put("artist_id", "");
		// Artist Name
		Label artist = new Label("Artist");
		artistField = new TextField();
		artistField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("artist_id", artistField.getText());
				callQuery();
			}
		});
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

	public void year(){
		map.put("productionyear", "");
		// Production Year
		Label year = new Label("Year");
		yearField = new TextField();
		yearField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("productionyear", yearField.getText());
				callQuery();
			}
		});
        yearField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
                	map.put("productionyear", yearField.getText());
            }
        });
        gridY++;
		grid.add(year, 0, gridY);
		grid.add(yearField, 1, gridY);
	}
	
	@Override
	public void callQuery(){
		try {
			//ResultSet data = db.advancedTableSearch("album", titleField.getText(), map, SearchCollection.isCollection());
			ResultSet data = db.searchTableByName("album", "dog", SearchCollection.isCollection());
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
	public void clearButton(){
		Button clearBtn = new Button("Clear Fields");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				titleField.clear();
				artistField.clear();
				yearField.clear();
			}
		});
		grid.add(clearBtn, 1, gridY);
	}
}
