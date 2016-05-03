package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * RecordingFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conducting a recording search, such as type,
 * medley type, number of bars, and number of repetitions
 * @author Jack
 *
 */
public class RecordingFilters extends AdvancedFilters {

	/**
	 * Create the VBox which will contain the filters for a recording search
	 * @throws SQLException
	 */
	public RecordingFilters() throws SQLException, MalformedURLException {
		super();
		type();
		medley();
		repetitions();
		bars();
		buttonGrid();
	}

	public void type() throws SQLException{
		map.put("Type", "");
		// Same as dance type?
		ResultSet typeSet;
		typeSet = db.doQuery("SELECT name FROM dancetype");
		ObservableList<String> typeList = FXCollections.observableArrayList("");
		while(typeSet.next()) {
			typeList.add(typeSet.getString(1));
		}
		Collections.sort(typeList);
		Label type = new Label("Type");
		final ComboBox<String> typeOptions = new ComboBox<String>(typeList);
		typeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Type", typeOptions.getValue());
			}
		});
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
	}
	
	public void medley() throws SQLException{
		map.put("Medley Type", "");
		// Medley Type
		ResultSet medleyTypeSet;
		medleyTypeSet = db.doQuery("SELECT description FROM medleytype");
		ObservableList<String> medleyTypeList = FXCollections.observableArrayList("");
		while(medleyTypeSet.next()) {
			medleyTypeList.add(medleyTypeSet.getString(1));
		}
		Collections.sort(medleyTypeList);
		Label medleyType = new Label("Medley Type");
		final ComboBox<String> medleyTypeOptions = new ComboBox<String>(medleyTypeList);
		medleyTypeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Medley Type", medleyTypeOptions.getValue());
			}
		});
		grid.add(medleyType, 0, 1);
		grid.add(medleyTypeOptions, 1, 1);
	}
	
	public void repetitions(){
		map.put("Repetitions", "");
		// Repetitions
		Label repetitions = new Label("Repetitions");
		final TextField repetitionsField = new TextField();
		repetitionsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Repetitions", repetitionsField.getText());
			}
		});
		grid.add(repetitions, 0, 2);
		grid.add(repetitionsField, 1, 2);
	}
	
	public void bars(){
		map.put("Bars", "");
		// Bars
		Label bars = new Label("Bars");
		final TextField barsField = new TextField();
		barsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Bars", barsField.getText());
			}
		});
		grid.add(bars, 0, 3);
		grid.add(barsField, 1, 3);
	}
}
