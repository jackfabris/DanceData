package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
	
	private ComboBox<String> typeOptions, medleyTypeOptions;
	private TextField repetitionsField, barsField;

	/**
	 * Create the VBox which will contain the filters for a recording search
	 * @throws SQLException
	 */
	public RecordingFilters(Database db, SearchDataView sc) throws SQLException, MalformedURLException {
		super(db, sc, "recording");
		type();
		medley();
		repetitions();
		bars();
		super.goAndClearButtons();
	}
	
	@Override
	public void setTitleText(String text){
		SearchCollection.setRecordingTitle(text);
		SearchCollection.getSearch().setText(text);
	}

	public void type() throws SQLException{
		map.put("type", "");
		ResultSet typeSet;
		typeSet = db.doQuery("SELECT name FROM dancetype");
		ObservableList<String> typeList = FXCollections.observableArrayList("");
		while(typeSet.next()) {
			typeList.add(typeSet.getString(1));
		}
		Collections.sort(typeList);
		Label type = new Label("Type");
		typeOptions = new ComboBox<String>(typeList);
		typeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("type", typeOptions.getValue());
			}
		});
		gridY++;
		grid.add(type, 0, gridY);
		grid.add(typeOptions, 1, gridY);
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
		medleyTypeOptions = new ComboBox<String>(medleyTypeList);
		medleyTypeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("medleytype", medleyTypeOptions.getValue());
			}
		});
		gridY++;
		grid.add(medleyType, 0, gridY);
		grid.add(medleyTypeOptions, 1, gridY);
	}

	public void repetitions(){
		map.put("repetitions", "");
		// Repetitions
		Label repetitions = new Label("Repetitions");
		repetitionsField = new TextField();
		repetitionsField.setTooltip(new Tooltip("Use <, <=, =, >, >= before the number of repetitions \nto indicate less, equal, or more repetitions"));
		Tooltip.install(repetitionsField, repetitionsField.getTooltip());
		repetitionsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(repetitionsField.getText().contains("=") || repetitionsField.getText().contains("<") || repetitionsField.getText().contains(">")){
					String num = repetitionsField.getText().substring(1, repetitionsField.getText().length());
					if(!isNumeric(num)) map.put("repetitions", "=9999");
					else map.put("repetitions", repetitionsField.getText());
				}
				else {
					if(!isNumeric(repetitionsField.getText())) map.put("repetitions", "=9999");
					else map.put("repetitions", "="+repetitionsField.getText());
				}
				callQuery();
			}
		});
		repetitionsField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue.booleanValue())
					if(repetitionsField.getText().contains("=") || repetitionsField.getText().contains("<") || repetitionsField.getText().contains(">")){
						String num = repetitionsField.getText().substring(1, repetitionsField.getText().length());
						if(!isNumeric(num)) map.put("repetitions", "=9999");
						else map.put("repetitions", repetitionsField.getText());
					}
					else {
						if(!isNumeric(repetitionsField.getText())) map.put("repetitions", "=9999");
						else map.put("repetitions", "="+repetitionsField.getText());
					}
			}
		});
		gridY++;
		grid.add(repetitions, 0, gridY);
		grid.add(repetitionsField, 1, gridY);
	}

	public void bars(){
		map.put("bars", "");
		// Bars
		Label bars = new Label("Bars");
		barsField = new TextField();
		barsField.setTooltip(new Tooltip("Use <, <=, =, >, >= before number of bars \nto indicate less, equal, or more bars"));
		Tooltip.install(barsField, barsField.getTooltip());
		barsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(barsField.getText().contains("=") || barsField.getText().contains("<") || barsField.getText().contains(">")){
					String num = barsField.getText().substring(1, barsField.getText().length());
					if(!isNumeric(num)) map.put("bars", "=9999");
					else map.put("bars", barsField.getText());
				}
				else {
					if(!isNumeric(barsField.getText())) map.put("bars", "=9999");
					else map.put("bars", "="+barsField.getText());
				}
				callQuery();
			}
		});
		barsField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue.booleanValue())
					if(barsField.getText().contains("=") || barsField.getText().contains("<") || barsField.getText().contains(">")){
						String num = barsField.getText().substring(1, barsField.getText().length());
						if(!isNumeric(num)) map.put("bars", "=9999");
						else map.put("bars", barsField.getText());
					}
					else {
						if(!isNumeric(barsField.getText())) map.put("bars", "=9999");
						else map.put("bars", "="+barsField.getText());
					}
			}
		});
		gridY++;
		grid.add(bars, 0, gridY);
		grid.add(barsField, 1, gridY);
	}

	@Override
	public void callQuery(){
		try {
			ResultSet data = db.advancedTableSearch("recording", titleField.getText(), map, SearchCollection.isCollection());
			//ResultSet data = db.searchTableByName("recording", "dog", SearchCollection.isCollection());
			RecordTable recordingTable = SearchCollection.getRecordingTable();
			recordingTable.setTableData(recordingTable.populate(data));
			recordingTable.getCellInfo().setVisible(false);
			recordingTable.getCellInfo().setVis(false);
			recordingTable.getTable().setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void clearButton(){
		Button clearBtn = new Button("Clear Fields and Reset");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				titleField.clear();
				typeOptions.setValue("");
				medleyTypeOptions.setValue("");
				repetitionsField.clear();
				barsField.clear();
				
				SearchCollection.setRecordingTitle("");
				SearchCollection.getSearch().clear();
				SearchCollection.getSearch().setPromptText("Search by Recording Title");
				
				//clear map
				Iterator<String> i = map.keySet().iterator();
				while(i.hasNext()){
					String x = i.next();
					map.put(x, "");
				}
				
				//reset table
				ResultSet data;
				try {
					data = db.searchTableByName("recording", "", SearchCollection.isCollection());
					RecordTable recordingTable = SearchCollection.getRecordingTable();
					recordingTable.setTableData(recordingTable.populate(data));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		grid.add(clearBtn, 1, gridY);
	}
}
