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

public class AlbumTable {

	private Database db; 
	private TableView<Album> table;
	private VBox cellInfo;
	private LinkedHashMap<String, String> colNameField;
	
	public AlbumTable() throws SQLException {
		db = new Database();
		table = new TableView<Album>();
		cellInfo = new VBox(10);
		cellInfo.setVisible(false);
		colNameField = new LinkedHashMap<String, String>();
		mapColumnNameToId();
		initializeTable();
	}
	
	public void mapColumnNameToId(){
		//name of album, name of band, code (index), “I have”
		colNameField.put("Name", "name");
		colNameField.put("Artist", "artist_id");
	}
	
	public void initializeTable() throws SQLException{
		//Album Table
		table.setEditable(false);
		ResultSet set = db.searchTableByName("album", "");
		
		Iterator<String> i = colNameField.keySet().iterator();
		while(i.hasNext()){
			String colName = i.next();
			String field = colNameField.get(colName);
			
			TableColumn<Album, String> col = new TableColumn<Album, String>(colName);
			col.setCellValueFactory(new PropertyValueFactory<Album, String>(field));
			table.getColumns().add(col);
		}
		
		table.setItems(populate(set));
		
		final TableColumn<Album, String> AlbumCol = (TableColumn<Album, String>) table.getColumns().get(0);
		AlbumCol.setCellFactory(new Callback<TableColumn<Album, String>, TableCell<Album, String>>() {
			@Override
		    public TableCell<Album, String> call(TableColumn<Album, String> col) {
		        final TableCell<Album, String> cell = new TableCell<Album, String>() {
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
		AlbumCol.setStyle( "-fx-alignment: CENTER-LEFT;");
	}
	
	public ObservableList<Album> populate(ResultSet set) throws SQLException{
		ObservableList<Album> data = FXCollections.observableArrayList();
		List<String> l = new ArrayList<String>(colNameField.values());
		while(set.next()){
			data.add(new Album(set.getString(l.get(0)), set.getString(l.get(1))));
		}
		return data;
	}
	
	public void setTableData(ObservableList<Album> data){
		table.setItems(data);
	}
	
	public TableView<Album> getTable(){
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
	
	public static class Album{
//		private final String id;
		private final String name; //2
//		private final String shortname;
		private final String artist_id; //4
//		private final String lastmod;
//		private final String oncd;
//		private final String onmc;
//		private final String onlp; 
//		private final String cdcatalogno;
//		private final String mccatalogno;
//		private final String lpcatalogno;
//		private final String verifier;
//		private final String verified;
//		private final String alphaorder;
//		private final String isavailable;
//		private final String productionyear;
//		private final String ramsayindexverno;
//		private final String notes;
//		private final String creationdate;
		
		public Album(String nameString, String band){
			name = nameString;
			artist_id = band;
		}
		public String getName(){
			return name;
		}
		public String getArtist_id(){
			return artist_id;
		}
	}
}
