package tables;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 
 * Record Table holds information regarding the TableView and CellInfo of the passed in state and table.
 * The four types of RecordTables would be a Dance Table, Publication Table, Recording Table and Album Table.
 * The columns are created based on a mapping from column name to field name of that specific Record.
 * 
 * The state is one of "d", "p", "r" or "a" representing a Dance, Publication, Recording, or Album respectively.
 * 
 * To change the columns of the Table, one must change colNameField mapping of column to field in the mapColumnNametoId() method 
 * as well as the fields of the Record class.
 *
 */
public class RecordTable {
	
	private Database db; 
	private TableView<Record> table;
	private CellInfo cellInfo;
	private LinkedHashMap<String, String> colNameField;
	private String tableString, state;
	
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
		cellInfo = new CellInfo(10, table);
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
		}
		//Album
		else if(state.equals("a")){
			colNameField.put("Name", "name");
			colNameField.put("Artist", "artist");
		}
		//Publication
		else if(state.equals("p")){
			colNameField.put("Name", "name");
			colNameField.put("Author", "devisor_id");
		}
		//Recording
		else{
			colNameField.put("Name", "name");
			colNameField.put("Type", "type");
			colNameField.put("Bars", "barsperrepeat");
			colNameField.put("Artist", "artist");
		}
		colNameField.put("I Have", "ihave");
		colNameField.put("Tag", "tag");
	}

	/**
	 * sets up the initial table that holds all of the records. Columns are created according to the mapping
	 * and each record is added based on the a database query, getting all records according to the state
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public void initializeTable() throws SQLException, MalformedURLException{
		table.setEditable(true);
		ResultSet set = db.searchTableByName(tableString, "");
		
		//set up columns
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			if(colName.equals("I Have")) {
				TableColumn<Record, CheckBox> col = new TableColumn<Record, CheckBox>(colName);
				col.setCellValueFactory(new PropertyValueFactory<Record, CheckBox>(field));
				table.getColumns().add(col);
			}
			else {
				TableColumn<Record, String> col = new TableColumn<Record, String>(colName);
				col.setCellValueFactory(new PropertyValueFactory<Record, String>(field));
				table.getColumns().add(col);
			}
		}
		
		//add rows to table
		table.setItems(populate(set));
		
		//CELL INFO
		final TableColumn<Record, String> RecordCol = (TableColumn<Record, String>) table.getColumns().get(0);
		RecordCol.setCellFactory(new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
			@Override
		    public TableCell<Record, String> call(TableColumn<Record, String> col) {
				final TableCell<Record, String> cell = new TableCell<Record, String>() {
		            @Override
		            public void updateItem(String firstName, boolean empty) {
		                super.updateItem(firstName, empty);
		                if (!empty) {
		                	//set linked cellInfo to be underlined and blue
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
		        			 final Record r = (Record) ((cell.getTableRow()!=null) ? cell.getTableRow().getItem() : null);
		        			 ResultSet set;
		        			 try {
		        				 set = db.doQuery("SELECT * FROM "+ tableString + " WHERE id="+r.getId());
		        				 cellInfo.set(set);
		        			 } catch (SQLException e) {
		        				 e.printStackTrace();
		        			 }
		        			 table.setVisible(false);
		        			 cellInfo.setVisible(true);
		        		 }
		        	 }
		         });
		         return cell;
		    }
		});
		
		//I HAVE COLUMN
		final TableColumn<Record, CheckBox> iHaveCol = (TableColumn<Record, CheckBox>) table.getColumns().get(table.getColumns().size()-2);

		iHaveCol.setCellFactory(new Callback<TableColumn<Record, CheckBox>, TableCell<Record, CheckBox>>() {
			@Override
			public TableCell<Record, CheckBox> call(TableColumn<Record, CheckBox> arg0) {
				final TableCell<Record, CheckBox> cell = new TableCell<Record, CheckBox>() {
					@Override
					public void updateItem(CheckBox cBox, boolean empty) {
						super.updateItem(cBox, empty);
						if(!empty){
							final Record r = (Record) ((this.getTableRow()!=null) ? this.getTableRow().getItem() : null);
							cBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
								//changed is called twice - problem?
								public void changed(ObservableValue<? extends Boolean> ov,Boolean old_val, Boolean new_val) {
									try {
										if(r!=null && !old_val && new_val) db.iHave(tableString, r.getId(), r.getTag());
										else if(r!=null && old_val && !new_val) db.iDontHave(tableString, r.getId());
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							});
							setGraphic(cBox);
						}
					}
				};
				return cell;
			}
		});		
		iHaveCol.setStyle("-fx-alignment: CENTER;");

		//INDEX COLUMN
		final TableColumn<Record, String> indexCol = (TableColumn<Record, String>) table.getColumns().get(table.getColumns().size()-1);
		indexCol.setEditable(true);
		indexCol.setCellFactory(new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
			@Override
			public TableCell<Record, String> call(TableColumn<Record, String> col) {
				final TextFieldTableCell<Record, String> cell = new TextFieldTableCell<Record, String>(
						new StringConverter<String>(){
							@Override
							public String fromString(String arg0) {return arg0;}
							@Override
							public String toString(String arg0) {return arg0;}
						});
				return cell;
			};
		});
		indexCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Record, String>>() {
            @Override public void handle(TableColumn.CellEditEvent<Record, String> t) {
            	Record r = ((Record)t.getTableView().getItems().get(t.getTablePosition().getRow()));
            	try {
            		if(!t.getNewValue().equals("")) db.iHave(tableString, r.getId(), t.getNewValue());
            		//else db.iDontHave(tableString, r.getId());
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
		indexCol.setStyle("-fx-alignment: CENTER;");
		table.setId("table");
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
			data.add(new Record(set, colNameField.values()));
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
	 * returns the VBox CellInfo of this class
	 * @return VBox CellInfo of this class
	 */
	public VBox getCellInfo(){
		return cellInfo;
	}
}