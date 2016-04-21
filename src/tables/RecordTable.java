package tables;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class RecordTable {
	
	private Database db; 
	private TableView<Record> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	private String tableString;
	private String state;
	
	public RecordTable(String tableString, String state) throws SQLException {
		this.tableString = tableString;
		this.state = state;
		db = new Database();
		table = new TableView<Record>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId(state);
		initializeTable();
	}
	
	public void mapColumnNameToId(String state){
		if(state.equals("d")){
			colNameField.put("Name", "name");
			colNameField.put("Type", "type_id");
			colNameField.put("Bars", "barsperrepeat");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		else if(state.equals("a")){
			colNameField.put("Name", "name");
			colNameField.put("Artist", "artist_id");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		else if(state.equals("p")){
			colNameField.put("Name", "name");
			colNameField.put("Author", "devisor_id");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		else{
			colNameField.put("Name", "name");
			colNameField.put("Type", "type_id");
			colNameField.put("Bars", "barsperrepeat");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
	}

	public void initializeTable() throws SQLException{
		//Record Table
		table.setEditable(false);
		ResultSet set = db.searchTableByName(tableString, "");
		
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Record, String> col = new TableColumn<Record, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Record, String>(field));
			table.getColumns().add(col);
		}

		table.setItems(populate(set));
		
		final TableColumn<Record, String> RecordCol = (TableColumn<Record, String>) table.getColumns().get(0);
		RecordCol.setCellFactory(new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
			@Override
		    public TableCell<Record, String> call(TableColumn<Record, String> col) {
		        final TableCell<Record, String> cell = new TableCell<Record, String>() {
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
		RecordCol.setStyle( "-fx-alignment: CENTER-LEFT;");
		}
	
	public ObservableList<Record> populate(ResultSet set) throws SQLException{
		ObservableList<Record> data = FXCollections.observableArrayList();
		List<String> l = new ArrayList<String>(colNameField.values());
		while(set.next()){
			data.add(new Record(set,state,l));
		}
		return data;
	}
	
	public void setTableData(ObservableList<Record> data){
		table.setItems(data);
	}
	
	public TableView<Record> getTable(){
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
}
