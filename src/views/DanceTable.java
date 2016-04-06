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
		searchTables();
	}

	
	public void searchTables() throws SQLException{
		// Search results
		//TableView<Dance> table = new TableView<Dance>();
		table.setEditable(false);
		ResultSet danceSet = db.searchDanceByTitle("");
		/*ResultSetMetaData rsmd = danceSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for(int i=1; i <= columnsNumber; i++){
			table.getColumns().add(new TableColumn<Dance, String>(rsmd.getColumnName(i)));
		}
		*/
		TableColumn<Dance, String> c1 = new TableColumn<Dance, String>("Name");
		c1.setCellValueFactory(
				new PropertyValueFactory<Dance, String>("name"));
		TableColumn<Dance, String> c2 = new TableColumn<Dance, String>("Shape");
		c2.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("shapeid"));
		TableColumn<Dance, String> c3 = new TableColumn<Dance, String>("Type");
		c3.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("typeid"));
		TableColumn<Dance, String> c4 = new TableColumn<Dance, String>("Author");
		c4.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("devisorid"));
		
		ObservableList<Dance> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Dance(danceSet.getString(3), danceSet.getString(5), danceSet.getString(6), danceSet.getString(8)));
		}
		table.setItems(data);
		table.getColumns().addAll(c1, c2, c3, c4);
		
//		final HBox resultsBox = new HBox(10);
//		resultsBox.getChildren().add(table);
//		resultsBox.setHgrow(table, Priority.ALWAYS);
		//grid.add(resultsBox, 0, gridY++);
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
		
		private Dance(String nameString, String shape, String type, String dev){
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
