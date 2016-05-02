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
public class RecordingFilters extends VBox {

	private VBox recordingFiltersVBox;
	private Database db;
	private GridPane grid;
	private LinkedHashMap<String, String> recMap;

	/**
	 * Create the VBox which will contain the filters for a recording search
	 * @throws SQLException
	 */
	public RecordingFilters() throws SQLException, MalformedURLException {
		recordingFiltersVBox = new VBox(10);
		recordingFiltersVBox.setStyle(
				"-fx-padding: 10;" +
						"-fx-border-style: solid inside;" +
						"-fx-border-width: 1;" +
						"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		db = new Database();
		recMap = new LinkedHashMap<String, String>();
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		type();
		medley();
		repetitions();
		bars();

		this.recordingFiltersVBox.getChildren().add(grid);
	}

	public void type() throws SQLException{
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
				recMap.put("Type", typeOptions.getValue());
			}
		});
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
	}
	
	public void medley() throws SQLException{
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
				recMap.put("Medley Type", medleyTypeOptions.getValue());
			}
		});
		grid.add(medleyType, 0, 1);
		grid.add(medleyTypeOptions, 1, 1);
	}
	
	public void repetitions(){
		// Repetitions
		Label repetitions = new Label("Repetitions");
		final TextField repetitionsField = new TextField();
		repetitionsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				recMap.put("Repetitions", repetitionsField.getText());
			}
		});
		grid.add(repetitions, 0, 2);
		grid.add(repetitionsField, 1, 2);
	}
	
	public void bars(){
		// Bars
		Label bars = new Label("Bars");
		final TextField barsField = new TextField();
		barsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				recMap.put("Bars", barsField.getText());
			}
		});
		grid.add(bars, 0, 3);
		grid.add(barsField, 1, 3);
	}

	public LinkedHashMap<String, String> getRecMap() {
		return recMap;
	}

	/**
	 * Get the recording filters VBox
	 * @return recordingFiltersVBox
	 */
	public VBox getRecordingFiltersVBox() {
		return this.recordingFiltersVBox;
	}

}
