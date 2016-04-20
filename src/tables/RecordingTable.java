package tables;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class RecordingTable {
	
	private Database db; 
	private TableView<Recording> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	
	public RecordingTable() throws SQLException, MalformedURLException {
		db = new Database();
		table = new TableView<Recording>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId();
		initializeTable();
	}
	
	public void mapColumnNameToId(){
		//Recording results: name, type, bars, repetitions, name of Recording, index, "I have"
		colNameField.put("Name", "name");
		colNameField.put("Type", "type_id");
		colNameField.put("Repetitions", "repetitions");
		colNameField.put("Bars", "barsperrepeat");
	}
	
	public void initializeTable() throws SQLException{
		//Recording Table
		table.setEditable(false);
		ResultSet set = db.searchTableByName("recording", "");
		
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Recording, String> col = new TableColumn<Recording, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Recording, String>(field));
			table.getColumns().add(col);
		}
		
		table.setItems(populate(set));
		
		final TableColumn<Recording, String> RecordingCol = (TableColumn<Recording, String>) table.getColumns().get(0);
		RecordingCol.setCellFactory(new Callback<TableColumn<Recording, String>, TableCell<Recording, String>>() {
			@Override
		    public TableCell<Recording, String> call(TableColumn<Recording, String> col) {
		        final TableCell<Recording, String> cell = new TableCell<Recording, String>() {
		            @Override
		            public void updateItem(String firstName, boolean empty) {
		                super.updateItem(firstName, empty);
		                if (empty) {
		                    setText(null);
		                } else {
		                	setTextFill(Color.BLUE);
		                	setUnderline(true);
		                    setText(firstName);
		                }
		            }
		         };
		         cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		             @Override
		             public void handle(MouseEvent event) {
		                 if (event.getClickCount() > 1) {
		                     setCellInfo(table.getColumns().get(0).getText(), cell.getItem());
		                     table.setVisible(false);
		                     cellInfo.setVisible(true);
		                 }
		             }
		         });
		         return cell;
		    }
		});
		table.setId("table");
		RecordingCol.setStyle( "-fx-alignment: CENTER-LEFT;");
	}
	
	public ObservableList<Recording> populate(ResultSet set) throws SQLException{
		ObservableList<Recording> data = FXCollections.observableArrayList();
		List<String> l = new ArrayList<String>(colNameField.values());
		while(set.next()){
			data.add(new Recording(set.getString(l.get(0)), set.getString(l.get(1)), set.getString(l.get(2)), set.getString(l.get(3))));
		}
		return data;
	}
	
	public void setTableData(ObservableList<Recording> data){
		table.setItems(data);
	}
	
	public TableView<Recording> getTable(){
		return table;
	}
	
	public void setCellInfo(String colName, String id){
		cellInfo.getChildren().clear();
		cellInfo.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		Label l1 = new Label(colName);
		Label l2 = new Label(id);
		Button tableReturn = new Button("BACK");
		cellInfo.getChildren().add(l1);
		cellInfo.getChildren().add(l2);
		cellInfo.getChildren().add(tableReturn);
		
		tableReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				table.setVisible(true);
                cellInfo.setVisible(false);
			}
		});
	}
	
	public VBox getCellInfo(){
		return cellInfo;
	}
	
	public static class Recording{
//		private final String id;
		private final String name; //2
//		private final String artist_id;
		private final String type_id; //4
		private final String repetitions; //5
		private final String barsperrepeat; //6
//		private final String medleytype_id;
//		private final String phrasing_id;
//		private final String playingseconds;
//		private final String twochords;
//		private final String notes;
		
		public Recording(String nameString, String type, String reps, String bars){
			name = nameString;
			type_id= type;
			repetitions = reps;
			barsperrepeat = bars;
		}
		public String getName() {
			return name;
		}
		public String getType_id() {
			return type_id;
		}
		public String getRepetitions() {
			return repetitions;
		}
		public String getBarsperrepeat() {
			return barsperrepeat;
		}
	}
}
