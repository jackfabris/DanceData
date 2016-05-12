package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import tables.RecordTable;
import views.SearchDataView;

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
	public RecordingFilters(SearchDataView sc) throws SQLException, MalformedURLException {
		super(sc, "recording");
		type();
		medley();
		repetitions();
		bars();
		buttonGrid();
	}

	public void type() throws SQLException{
		map.put("type", "");
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
				map.put("type", typeOptions.getValue());
			}
		});
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
	}
	
	public void medley() throws SQLException{
		map.put("medleytype", "");
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
				map.put("medleytype", medleyTypeOptions.getValue());
			}
		});
		grid.add(medleyType, 0, 1);
		grid.add(medleyTypeOptions, 1, 1);
	}
	
	public void repetitions(){
		map.put("repetitions", "");
		// Repetitions
		Label repetitions = new Label("Repetitions");
		final TextField repetitionsField = new TextField();
		repetitionsField.setTooltip(new Tooltip("Use <, <=, >, >= before the number of repetitions \nto indicate less, equal, or more repetitions"));
		Tooltip.install(repetitionsField, repetitionsField.getTooltip());
		repetitionsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Repetitions", repetitionsField.getText());
				callQuery();
			}
		});
        repetitionsField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
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
		barsField.setTooltip(new Tooltip("Use <, <=, >, >= before number of bars \nto indicate less, equal, or more bars"));
		Tooltip.install(barsField, barsField.getTooltip());
		barsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Bars", barsField.getText());
				callQuery();
			}
		});
       barsField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
                	map.put("Bars", barsField.getText());
            }
        });
		grid.add(bars, 0, 3);
		grid.add(barsField, 1, 3);
	}
	
	@Override
	public void callQuery(){
		try {
			//Result set data = db.advancedTableSearch("recording", SearchCollection.getRecordingTitle(), map, SearchCollection.isCollection());
			ResultSet data = db.searchTableByName("recording", "dog", SearchCollection.isCollection());
			RecordTable recordingTable = SearchCollection.getRecordingTable();
			recordingTable.setTableData(recordingTable.populate(data));
			recordingTable.getCellInfo().setVisible(false);
			recordingTable.getTable().setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
