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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import tables.RecordingTable.Recording;

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
		colNameField.put("Name", "name");
		colNameField.put("Author", "devisor_id");
	}

	public void initializeTable() throws SQLException{
		//Dance Table
		table.setEditable(false);
		ResultSet set = db.searchTableByName("publication", "");
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Publication, String> col = new TableColumn<Publication, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Publication, String>(field));
			table.getColumns().add(col);
		}
		
		table.setItems(populate(set));
		
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
		table.setId("table");
		danceCol.setStyle( "-fx-alignment: CENTER-LEFT;");
	}
	
	public ObservableList<Publication> populate(ResultSet set) throws SQLException{
		ObservableList<Publication> data = FXCollections.observableArrayList();
		List<String> l = new ArrayList<String>(colNameField.values());
		while(set.next()){
			data.add(new Publication(set.getString(l.get(0)), set.getString(l.get(1))));
		}
		return data;
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
//		private final String id;
		private final String name;
//		private final String shortname; 
		private final String devisor_id;
//		private final String lastmod;
//		private final String hasdances;
//		private final String hastunes;
//		private final String pagenumbering;
//		private final String url;
//		private final String onpaper;
//		private final String creationdate;
		
		public Publication(String name, String dev){
			this.name = name;
			devisor_id = dev;
		}
		public String getName(){
			return name;
		}
		public String getDevisor_id(){
			return devisor_id;
		}
	}
}
