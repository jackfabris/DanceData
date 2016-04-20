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
import javafx.geometry.Pos;
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

public class DanceTable {
	
	private Database db; 
	private TableView<Dance> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	
	public DanceTable() throws SQLException, MalformedURLException {
		db = new Database();
		table = new TableView<Dance>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId();
		initializeTable();
	}
	
	public void mapColumnNameToId(){
		//name, type, bars, publication????, index, "I have"
		colNameField.put("Name", "name");
		colNameField.put("Type", "type_id");
		colNameField.put("Bars", "barsperrepeat");
		colNameField.put("I Have", "iHave");
		colNameField.put("Index", "index");
	}

	public void initializeTable() throws SQLException{
		//Dance Table
		table.setEditable(false);
		ResultSet set = db.searchTableByName("dance", "");
		
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Dance, String> col = new TableColumn<Dance, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Dance, String>(field));
			table.getColumns().add(col);
		}

		table.setItems(populate(set));
		
		final TableColumn<Dance, String> danceCol = (TableColumn<Dance, String>) table.getColumns().get(0);
		danceCol.setCellFactory(new Callback<TableColumn<Dance, String>, TableCell<Dance, String>>() {
			@Override
		    public TableCell<Dance, String> call(TableColumn<Dance, String> col) {
		        final TableCell<Dance, String> cell = new TableCell<Dance, String>() {
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
		danceCol.setStyle( "-fx-alignment: CENTER-LEFT;");
		}
	
	public ObservableList<Dance> populate(ResultSet set) throws SQLException{
		ObservableList<Dance> data = FXCollections.observableArrayList();
		List<String> l = new ArrayList<String>(colNameField.values());
		while(set.next()){
			data.add(new Dance(set.getString(l.get(0)), set.getString(l.get(1)), set.getString(l.get(2))));
		}
		return data;
	}
	
	public void setTableData(ObservableList<Dance> data){
		table.setItems(data);
	}
	
	public TableView<Dance> getTable(){
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
	
	public static class Dance{
		//private final SimpleStringProperty id;
		private final String barsperrepeat;
		private final String name; 
		//private final SimpleStringProperty ucname;
		//private final String shapeid; 
		private final String type_id;					
		//private final SimpleStringProperty couples_id;
		//private final String devisorid;
		//private final SimpleStringProperty verified;
		//private final SimpleStringProperty lastmod;
		//private final SimpleStringProperty devised;
		//private final SimpleStringProperty notes;
		//private final SimpleStringProperty medleytype_id;
		//private final SimpleStringProperty progression_id;
		//private final SimpleStringProperty url;
		//private final SimpleStringProperty creationdate;
		private CheckBox iHave;
		private String index;
		
		public Dance(String nameString, String type, String bars){
			barsperrepeat = bars;
			name = nameString;
			type_id = type;
			iHave = new CheckBox();
			index = "";
		}
		public String getBarsperrepeat() {
			return barsperrepeat;
		}
		public String getName() {
			return name;
		}
		public String getType_id() {
			return type_id;
		}
		public CheckBox getIHave(){
			return iHave;
		}
		public String getIndex(){
			return index;
		}
	}
}
