package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

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

public class PublicationTable {
	
	//THIS NEEDS TO CHANGE
	
	private Database db; 
	private TableView<Publication> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	
	public PublicationTable() throws SQLException {
		db = new Database();
		table = new TableView<Publication>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId();
		initializeTable();
	}
	
	public void mapColumnNameToId(){
		//name, type, bars, publication????, index, “I have”
		colNameField.put("Name", "name");
		colNameField.put("Type", "typeid");
		colNameField.put("Bars", "barsperrepeat");
	}

	public void initializeTable() throws SQLException{
		//Dance Table
		table.setEditable(false);
		ResultSet danceSet = db.searchTableByName("dance", "");
		
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Publication, String> col = new TableColumn<Publication, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Publication, String>(field));
			table.getColumns().add(col);
		}
		
		ObservableList<Publication> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Publication(danceSet.getString(3), danceSet.getString(6), danceSet.getString(2)));
		}
		table.setItems(data);
		
		final TableColumn<Publication, String> danceCol = (TableColumn<Publication, String>) table.getColumns().get(0);
		danceCol.setCellFactory(new Callback<TableColumn<Publication, String>, TableCell<Publication, String>>() {
			@Override
		    public TableCell<Publication, String> call(TableColumn<Publication, String> col) {
		        final TableCell<Publication, String> cell = new TableCell<Publication, String>() {
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
	}
	
	public void setTableData(ObservableList<Publication> data){
		table.setItems(data);
	}
	
	public TableView<Publication> getTable(){
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
	
	public static class Publication{
		//private final SimpleStringProperty id;
		private final String barsperrepeat; 			//2
		private final String name; 						//3
		//private final SimpleStringProperty ucname;
		//private final String shapeid; 
		private final String typeid;					//6
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
		
		public Publication(String nameString, String type, String bars){
			barsperrepeat = bars;
			name = nameString;
			typeid = type;
		}
		public String getBarsperrepeat() {
			return barsperrepeat;
		}
		public String getName() {
			return name;
		}
		public String getTypeid() {
			return typeid;
		}
	}
}
