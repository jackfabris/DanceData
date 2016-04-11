package views;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DanceTable {
	
	private Database db; 
	private TableView<Dance> table;
	
	public DanceTable() throws SQLException {
		db = new Database();
		table = new TableView<Dance>();
		initializeTable();
	}

	public void initializeTable() throws SQLException{
		//Dance Table
		table.setEditable(false);
		ResultSet danceSet = db.searchDanceByTitle("");

		TableColumn<Dance, String> nameCol = new TableColumn<Dance, String>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Dance, String>("name"));
		
		TableColumn<Dance, String> shapeCol = new TableColumn<Dance, String>("Shape");
		shapeCol.setCellValueFactory(new PropertyValueFactory<Dance, String>("shapeid"));
		
		TableColumn<Dance, String> typeCol = new TableColumn<Dance, String>("Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<Dance, String>("typeid"));
		
		TableColumn<Dance, String> authorCol = new TableColumn<Dance, String>("Author");
		authorCol.setCellValueFactory(new PropertyValueFactory<Dance, String>("devisorid"));
		
		ObservableList<Dance> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Dance(danceSet.getString(3), danceSet.getString(5), danceSet.getString(6), danceSet.getString(8)));
		}
		table.setItems(data);
		table.getColumns().addAll(nameCol, shapeCol, typeCol, authorCol);
	}
	
	public void setTableData(ObservableList<Dance> data){
		table.setItems(data);
	}
	
	public TableView<Dance> getTable(){
		return table;
	}
	

	public static class Dance{
		//private final SimpleStringProperty id;
		//private final SimpleStringProperty barsperrepeat;
		private final String name;
		//private final SimpleStringProperty ucname;
		private final String shapeid; 
		private final String typeid;
		//private final SimpleStringProperty couples_id;
		private final String devisorid;
		//private final SimpleStringProperty verified;
		//private final SimpleStringProperty lastmod;
		//private final SimpleStringProperty devised;
		//private final SimpleStringProperty notes;
		//private final SimpleStringProperty medleytype_id;
		//private final SimpleStringProperty progression_id;
		//private final SimpleStringProperty url;
		//private final SimpleStringProperty creationdate;
		
		Dance(String nameString, String shape, String type, String dev){
			name = nameString;
			shapeid = shape;
			typeid = type;
			devisorid = dev;
		}
		public String getName() {
			return name;
		}
		public String getShapeid() {
			return shapeid;
		}
		public String getTypeid() {
			return typeid;
		}
		public String getDevisorid() {
			return devisorid;
		}
	}
}
