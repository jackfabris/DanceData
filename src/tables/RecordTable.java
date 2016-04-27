package tables;

import java.net.MalformedURLException;
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

/**
 * 
 * Record Table holds information regarding the TableView and CellInfo of the passed in state and table.
 * The four types of RecordTables would be a Dance Table, Publication Table, Recording Table and Album Table.
 * The columns are created based on a mapping from column name to field name of that specific Record.
 * Should any more columns be added to a specific table, you must add to the LinkedHashMap accordingly.
 * The fields must also be added to Record and initialized for that state. The state is one of "d", "p", "r" 
 * or "a" representing a Dance, Publication, Recording, or Album respectively.
 *
 */
public class RecordTable {
	
	private Database db; 
	private TableView<Record> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	private String tableString;
	private String state;
	
	/**
	 * constructor for RecordTable creates a table of records according to either Dance, Publication, Recording, or Album.
	 * @param tableString String of the desired table name of the schema. Should correspond with state.
	 * @param state String that determines what type of RecordTable this should be. Should correspond with tableString.
	 * @throws SQLException
	 * @throws MalformedURLException 
	 */
	public RecordTable(String tableString, String state) throws SQLException, MalformedURLException {
		this.tableString = tableString;
		this.state = state;
		db = new Database();
		table = new TableView<Record>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId();
		initializeTable();
	}
	
	/**
	 * returns the mapping of the class' state of column names in the TableView to field 
	 * names of the Record according to the state
	 */
	public void mapColumnNameToId(){
		//Dance
		if(state.equals("d")){
			colNameField.put("Name", "name");
			colNameField.put("Type", "type");
			colNameField.put("Bars", "barsperrepeat");
			colNameField.put("Publication", "publication");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		//Album
		else if(state.equals("a")){
			colNameField.put("Name", "name");
			colNameField.put("Artist", "artist");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		//Publication
		else if(state.equals("p")){
			colNameField.put("Name", "name");
			colNameField.put("Author", "devisor_id");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
		//Recording
		else{
			colNameField.put("Name", "name");
			colNameField.put("Type", "type");
			colNameField.put("Bars", "barsperrepeat");
			colNameField.put("Artist", "artist");
			colNameField.put("I Have", "iHave");
			colNameField.put("Index", "index");
		}
	}

	/**
	 * sets up the initial table that holds all of the records. Columns are created according to the mapping
	 * and each record is added based on the a database query, getting all records according to the state
	 * @throws SQLException
	 */
	public void initializeTable() throws SQLException{
		table.setEditable(false);
		ResultSet set = db.searchTableByName(tableString, "", false);
		
		//set up columns
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Record, String> col = new TableColumn<Record, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Record, String>(field));
			table.getColumns().add(col);
		}
		
		//add rows to table
		table.setItems(populate(set));
		
		//link columns to get CellInfo for a given cell
		final TableColumn<Record, String> RecordCol = (TableColumn<Record, String>) table.getColumns().get(0);
		RecordCol.setCellFactory(new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
			@Override
		    public TableCell<Record, String> call(TableColumn<Record, String> col) {
		        //set linked cellInfo to be underlined and blue
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
		         //on double click, show cellInfo
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
	
	/**
	 * takes the given database query result set and adds each result to the rows of the table as a Record
	 * according to what table and what state.
	 * @param set ResultSet of the query to populate the database with
	 * @return ObservableList of Records containing all Records to be added to the rows of the table
	 * @throws SQLException
	 */
	public ObservableList<Record> populate(ResultSet set) throws SQLException{
		ObservableList<Record> data = FXCollections.observableArrayList();
		while(set.next()){
			data.add(new Record(set,state));
		}
		return data;
	}
	
	/**
	 * sets the given list to be the rows of the table
	 * @param data ObservableList of Records that should be the rows of the table
	 */
	public void setTableData(ObservableList<Record> data){
		table.setItems(data);
	}
	
	/**
	 * returns the TableView of Records of this class
	 * @return TableView of Records of this class
	 */
	public TableView<Record> getTable(){
		return table;
	}
	
	/**
	 * sets this class' cellInfo according to colName and id and 
	 * creates a button for toggling between table and cellInfo
	 * @param colName String column name to display
	 * @param id String id of the column to display
	 */
	public void setCellInfo(String colName, String id){
		//set up cell info
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
		
		//return to table
		tableReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				table.setVisible(true);
                cellInfo.setVisible(false);
			}
		});
	}
	
	/**
	 * returns the VBox CellInfo of this class
	 * @return VBox CellInfo of this class
	 */
	public VBox getCellInfo(){
		return cellInfo;
	}
}
