package tables;

import java.sql.ResultSet;
import java.sql.SQLException;

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

public class DanceTable {
	
	private Database db; 
	private TableView<Dance> table;
	private VBox cellInfo;
	
	public DanceTable() throws SQLException {
		db = new Database();
		table = new TableView<Dance>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
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
		
		final TableColumn<Dance, String> authorCol = new TableColumn<Dance, String>("Author");
		authorCol.setCellValueFactory(new PropertyValueFactory<Dance, String>("devisorid"));
		
		ObservableList<Dance> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Dance(danceSet.getString(3), danceSet.getString(5), danceSet.getString(6), danceSet.getString(8)));
		}
		table.setItems(data);
		table.getColumns().addAll(nameCol, shapeCol, typeCol, authorCol);
		
		authorCol.setCellFactory(new Callback<TableColumn<Dance, String>, TableCell<Dance, String>>() {
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
		                     setCellInfo(authorCol.getText(), cell.getItem());
		                     table.setVisible(false);
		                     cellInfo.setVisible(true);
		                 }
		             }
		         });
		         return cell;
		    }
		});
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
		
		public Dance(String nameString, String shape, String type, String dev){
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
