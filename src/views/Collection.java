package views;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.RecordTable;

public class Collection {
	
	private VBox collectionVBox;
	private Database db;
	private RecordTable danceTable, publicationTable, recordingTable, albumTable;
	private String state;
	private final TextField search;

	public Collection() throws SQLException, MalformedURLException{
		collectionVBox = new VBox(10);
		db = new Database();
		search = new TextField();
		setUpSearchBar();
		navigationButtons();
		state="d";
		danceTable = new RecordTable("dance", "d");
		setUpTable(danceTable.getTable(), danceTable.getCellInfo());
		albumTable = new RecordTable("album", "a");
		setUpTable(albumTable.getTable(), albumTable.getCellInfo());
		recordingTable = new RecordTable("recording", "r");
		setUpTable(recordingTable.getTable(), recordingTable.getCellInfo());
		publicationTable = new RecordTable("publication", "p");
		setUpTable(publicationTable.getTable(), publicationTable.getCellInfo());
		showIHave();
		tableVisibility(true, false, false, false);
	}
	
	/**
	 * sets up the search bar and search button, and will search on both
	 * a button press and 'Enter' or 'Return' key event
	 */
	public void setUpSearchBar(){
		//Search Bar
		search.setPromptText("Search by Dance Title");
		search.setPrefWidth(300);
		Button searchGoBtn = new Button("Go");
		searchGoBtn.setPrefWidth(50);

		HBox searchBox = new HBox(10);
		searchBox.getChildren().add(search);
		searchBox.getChildren().add(searchGoBtn);

		this.collectionVBox.getChildren().add(searchBox);

		//Go Button Event
		searchGoBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					searchText(search.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		//Enter/Return Key Event
		searchBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					try {
						searchText(search.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * queries the database table of the current state by rows like
	 * the given title and displays the result set in that table - *sets title
	 * @param title a String representing part of the title for the desired records
	 * @throws SQLException
	 */
	public void searchText(String title) throws SQLException{
		ResultSet set;
		if(state.equals("d")) {
			set = db.searchTableByName("dance",title, true);
			danceTable.setTableData(danceTable.populate(set));
		}
		else if(state.equals("p")) {
			set = db.searchTableByName("publication",title, true);
			publicationTable.setTableData(publicationTable.populate(set));
		}
		else if(state.equals("r")) {
			set = db.searchTableByName("recording",title, true);
			recordingTable.setTableData(recordingTable.populate(set));
		}
		else if(state.equals("a")) {
			set = db.searchTableByName("album",title, true);
			albumTable.setTableData(albumTable.populate(set));
		}
	}
	
	/**
	 * responsible for state change on button action.
	 * sets up the toggle buttons for Dance, Publication, Recording, 
	 * and Album where, on button action, the state, search bar prompt text, 
	 * advanced search filter radio button text, table/cellInfo visibility,
	 * and advanced search filter visibility is changed respectively.
	 */
	public void navigationButtons(){
		HBox navBtnBox = new HBox(10);
		
		ToggleGroup tg = new ToggleGroup();
		
		final ToggleButton danceTB = new ToggleButton("My Dances");
		danceTB.setSelected(true);
		danceTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(danceTB);
		danceTB.setId("toggle-button");
		
		final ToggleButton publicationTB = new ToggleButton("My Publications");
		publicationTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(publicationTB);
		publicationTB.setId("toggle-button");
		
		final ToggleButton recordingTB = new ToggleButton("My Recordings");
		recordingTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(recordingTB);
		recordingTB.setId("toggle-button");
		
		final ToggleButton albumTB = new ToggleButton("My Albums");
		albumTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(albumTB);
		albumTB.setId("toggle-button");
	
		this.collectionVBox.getChildren().add(navBtnBox);
		
		// Dance
		danceTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//hide/show tables/cellInfo
				state = "d";
				tableVisibility(true, false, false, false);
			}
		});
		// Publication
		publicationTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//hide/show tables/cellInfo
				state = "p";
				tableVisibility(false, true, false, false);
			}
		});
		// Recording
		recordingTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//hide/show tables/cellInfo
				state = "r";
				tableVisibility(false, false, true, false);
			}
		});
		// Album
		albumTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//hide/show tables/cellInfo
				state = "a";
				tableVisibility(false, false, false, true);
			}
		});
	}
	
	/**
	 * based on the given table visibilities, sets the visibility of each table
	 * and hides or shows cellInfo of each table depending on the state.
	 * @param d boolean for danceTable visibility
	 * @param p boolean for publicationTable visibility
	 * @param r boolean for recordingTable visibility
	 * @param a boolean for albumTable visibility
	 */
	public void tableVisibility(boolean d, boolean p, boolean r, boolean a){
		// table
		danceTable.getTable().setVisible(d);
		publicationTable.getTable().setVisible(p);
		recordingTable.getTable().setVisible(r);
		albumTable.getTable().setVisible(a);
		
		// cellInfo
		// should be hidden on state change - change later?
		if(state.equals("d")) { 
			publicationTable.getCellInfo().setVisible(false);
			recordingTable.getCellInfo().setVisible(false);
			albumTable.getCellInfo().setVisible(false);
		}
		else if(state.equals("p")){
			danceTable.getCellInfo().setVisible(false);
			recordingTable.getCellInfo().setVisible(false);
			albumTable.getCellInfo().setVisible(false);
		}
		else if(state.equals("r")){
			danceTable.getCellInfo().setVisible(false);
			publicationTable.getCellInfo().setVisible(false);
			albumTable.getCellInfo().setVisible(false);
		}
		else if(state.equals("a")){
			danceTable.getCellInfo().setVisible(false);
			publicationTable.getCellInfo().setVisible(false);
			recordingTable.getCellInfo().setVisible(false);
		}
	}
	
	/**
	 * adds the given table and cellInfo to this class' VBox and binds when they are shown or 
	 * hidden to their visible property in order to collapse respective screens.
	 * @param table TableView of the passed in table
	 * @param cellInfo VBox representing the information from a single cell of the passed in table
	 */
	public void setUpTable(TableView<?> table, VBox cellInfo){
		this.collectionVBox.getChildren().add(table);
		table.managedProperty().bind(table.visibleProperty());
		this.collectionVBox.getChildren().add(cellInfo);
		cellInfo.managedProperty().bind(cellInfo.visibleProperty());
	}
	
	public void showIHave() throws SQLException{
		ResultSet set = db.searchTableByName("dance", "", true);
		danceTable.setTableData(danceTable.populate(set));
		set = db.searchTableByName("publication", "", true);
		publicationTable.setTableData(publicationTable.populate(set));
		set = db.searchTableByName("album", "", true);
		albumTable.setTableData(albumTable.populate(set));
		set = db.searchTableByName("recording", "", true);
		recordingTable.setTableData(recordingTable.populate(set));
	}

	public VBox getCollectionVBox() {
		return collectionVBox;
	}
}
