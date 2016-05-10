package filters;

import java.net.MalformedURLException;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * AlbumFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conduction an album search, such as specifying
 * an artist name or year of production
 * @author Jack
 *
 */
public class AlbumFilters extends AdvancedFilters {

	/**
	 * Create the VBox which will contain the filters for an album search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public AlbumFilters() throws MalformedURLException, SQLException {
		super();
		artist();
		year();
		buttonGrid();
	}
	
	public void artist(){
		map.put("Artist", "");
		// Artist Name
		Label artist = new Label("Artist");
		final TextField artistField = new TextField();
		artistField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Artist", artistField.getText());
				callQuery();
			}
		});
        artistField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
                	map.put("Bars", artistField.getText());
            }
        });
		grid.add(artist, 0, 0);
		grid.add(artistField, 1, 0);
	}

	public void year(){
		map.put("Year", "");
		// Production Year
		Label year = new Label("Year");
		final TextField yearField = new TextField();
		yearField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Year", yearField.getText());
				callQuery();
			}
		});
        yearField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
                	map.put("Bars", yearField.getText());
            }
        });
		grid.add(year, 0, 1);
		grid.add(yearField, 1, 1);
	}
}
