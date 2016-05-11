package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import tables.RecordTable;
import views.SearchDataView;

public abstract class AdvancedFilters {

	protected VBox filtersVBox;
	protected GridPane grid;
	protected LinkedHashMap<String, String> map;
	protected Database db;
	protected boolean searchBool;
	protected SearchDataView SearchCollection;
	protected String table;

	public AdvancedFilters(SearchDataView sc, String table) throws MalformedURLException, SQLException{
		this.SearchCollection = sc;
		this.table = table;
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		db = new Database();
		map = new LinkedHashMap<String, String>();
		setUpFilters();
		searchBool = false;
	}
	
	public void printMap(){
		//TESTING
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			System.out.println(x+", "+map.get(x));
		}
		System.out.println();
	}

	public void setUpFilters(){
		filtersVBox = new VBox(10);
		filtersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		filtersVBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					callQuery();
				}
			}
		});
		//MOUSE HOVER
		filtersVBox.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				searchBool = true;
			}
		});
		filtersVBox.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				searchBool = false;
			}
		});
	}
	
	public void goButton(){
		Button goBtn = new Button("Advanced Search");
		goBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				callQuery();	
			}
		});
		this.filtersVBox.getChildren().add(goBtn);
	}
	
	public void buttonGrid(){
		this.filtersVBox.getChildren().add(grid);
		goButton();
	}
	
	public LinkedHashMap<String, String> getDanceMap() {
		return map;
	}
	
	public abstract void callQuery();
//	{
//		//have sub classes overide call query
//		printMap();
//		String name = "";
//		if(table.equals("dance")) name = SearchCollection.getDanceTitle();
//		else if(table.equals("album")) name = SearchCollection.getAlbumTitle();
//		else if(table.equals("publication")) name = SearchCollection.getPublicationTitle();
//		else if(table.equals("recording")) name = SearchCollection.getRecordingTitle();
//
//		try {
//			//Result set data = db.advancedTableSearch(table, name, map, SearchCollection.isCollection());
//			ResultSet data = db.searchTableByName(table, "dog", SearchCollection.isCollection());
//			RecordTable danceTable = SearchCollection.getDanceTable();
//			RecordTable albumTable = SearchCollection.getAlbumTable();
//			RecordTable publicationTable = SearchCollection.getPublicationTable();
//			RecordTable recordingTable = SearchCollection.getRecordingTable();
//			if(table.equals("dance")) danceTable.setTableData(danceTable.populate(data));
//			else if(table.equals("album")) albumTable.setTableData(albumTable.populate(data));
//			else if(table.equals("publication")) publicationTable.setTableData(publicationTable.populate(data));
//			else if(table.equals("recording")) recordingTable.setTableData(recordingTable.populate(data));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Get the dance filters VBox
	 * @return danceFiltersVBox
	 */
	public VBox getFiltersVBox() {
		return this.filtersVBox;
	}
	
	public boolean getSearchBool(){
		return searchBool;
	}

}
