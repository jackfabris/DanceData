package views;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import filters.AlbumFilters;
import filters.DanceFilters;
import filters.PublicationFilters;
import filters.RecordingFilters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.RecordTable;

/**
 * 
 * Search holds information regarding the four main tables for the database and handles 
 * the visibility and other changes depending on the state, which is one of "d", "p", "r" or "a"
 * representing a Dance, Publication, Recording, or Album respectively. 
 * 
 * The initial state of Search is "d".
 *
 */
public class Search {

	private VBox searchVBox, danceFiltersVBox, recordingFiltersVBox, albumFiltersVBox, publicationFiltersVBox;
	private Database db;
	private RecordTable danceTable, publicationTable, recordingTable, albumTable;
	private final TextField search;
	private RadioButton advSF;
	private String state, danceTitle, publicationTitle, recordingTitle, albumTitle;

	/**
	 * Constructor for Search sets up initial display and 
	 * initializes VBox, Database, Advanced Search Filters, and Tables
	 * @throws SQLException
	 */
	public Search() throws SQLException, MalformedURLException {
		searchVBox = new VBox(10);
		db = new Database();
		search = new TextField();
		setUpSearchBar();
		navigationButtons();
		advSF = new RadioButton();
		navSearchFilter();
		searchFilters();
		state = "d";
		danceTitle = "";
		publicationTitle = "";
		recordingTitle = "";
		albumTitle = "";
		danceTable = new RecordTable("dance", "d");
		setUpTable(danceTable.getTable(), danceTable.getCellInfo());
		albumTable = new RecordTable("album", "a");
		setUpTable(albumTable.getTable(), albumTable.getCellInfo());
		recordingTable = new RecordTable("recording", "r");
		setUpTable(recordingTable.getTable(), recordingTable.getCellInfo());
		publicationTable = new RecordTable("publication", "p");
		setUpTable(publicationTable.getTable(), publicationTable.getCellInfo());
		tableVisibility(true, false, false, false);
	}

	/**
	 * returns this class' VBox, which holds all information regarding the Search class
	 * @return VBox of the Search Class
	 */
	public VBox getSearchVBox(){
		return this.searchVBox;
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

		this.searchVBox.getChildren().add(searchBox);

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
			danceTitle = title;
			set = db.searchTableByName("dance",title);
			danceTable.setTableData(danceTable.populate(set));
		}
		else if(state.equals("p")) {
			publicationTitle = title;
			set = db.searchTableByName("publication",title);
			publicationTable.setTableData(publicationTable.populate(set));
		}
		else if(state.equals("r")) {
			recordingTitle = title;
			set = db.searchTableByName("recording",title);
			recordingTable.setTableData(recordingTable.populate(set));
		}
		else if(state.equals("a")) {
			albumTitle = title;
			set = db.searchTableByName("album",title);
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

		final ToggleButton danceTB = new ToggleButton("Dance");
		danceTB.setSelected(true);
		danceTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(danceTB);
		danceTB.setId("toggle-button");

		final ToggleButton publicationTB = new ToggleButton("Publication");
		publicationTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(publicationTB);
		publicationTB.setId("toggle-button");

		final ToggleButton recordingTB = new ToggleButton("Recording");
		recordingTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(recordingTB);
		recordingTB.setId("toggle-button");

		final ToggleButton albumTB = new ToggleButton("Album");
		albumTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(albumTB);
		albumTB.setId("toggle-button");

		this.searchVBox.getChildren().add(navBtnBox);

		// Dance
		danceTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				danceTB.setSelected(true);
				if (danceTitle.equals("")) search.setPromptText("Search by Dance Title");
				else search.setPromptText(danceTitle);
				advSF.setText("Show Advanced Search Options For Dance");
				if(!state.equals("d")){
					advSF.setSelected(false);
					search.clear();
				}
				state = "d";
				searchFiltersVisibility(danceFiltersVBox.isVisible(), false, false, false);
				tableVisibility(true, false, false, false);
				cellInfoVisibility(danceTable.getCellInfo().isVisible(), false, false, false);
			}
		});
		// Publication
		publicationTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				publicationTB.setSelected(true);
				if (publicationTitle.equals("")) search.setPromptText("Search by Publication Title");
				else search.setPromptText(publicationTitle);
				advSF.setText("Show Advanced Search Options For Publication");
				if(!state.equals("p")){
					advSF.setSelected(false);
					search.clear();
				}
				state = "p";
				searchFiltersVisibility(false, publicationFiltersVBox.isVisible(), false, false);
				tableVisibility(false, true, false, false);
				cellInfoVisibility(false, publicationTable.getCellInfo().isVisible(), false, false);
			}
		});
		// Recording
		recordingTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				recordingTB.setSelected(true);
				if (recordingTitle.equals("")) search.setPromptText("Search by Recording Title");
				else search.setPromptText(recordingTitle);
				advSF.setText("Show Advanced Search Options For Recording");
				if(!state.equals("r")){
					advSF.setSelected(false);
					search.clear();
				}
				state = "r";
				searchFiltersVisibility(false, false, recordingFiltersVBox.isVisible(), false);
				tableVisibility(false, false, true, false);
				cellInfoVisibility(false, false, recordingTable.getCellInfo().isVisible(), false);
			}
		});
		// Album
		albumTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumTB.setSelected(true);
				if (albumTitle.equals("")) search.setPromptText("Search by Album Title");
				else {
					search.setText(albumTitle);
				}
				advSF.setText("Show Advanced Search Options For Album");
				if(!state.equals("a")){
					advSF.setSelected(false);
					search.clear();
				}
				state = "a";
				searchFiltersVisibility(false, false, false, albumFiltersVBox.isVisible());
				tableVisibility(false, false, false, true);
				cellInfoVisibility(false, false, false, albumTable.getCellInfo().isVisible());
			}
		});
	}
	
	//javadoc
	public void searchFiltersVisibility(boolean d, boolean p, boolean r, boolean a){
		danceFiltersVBox.setVisible(d);
		publicationFiltersVBox.setVisible(p);
		recordingFiltersVBox.setVisible(r);
		albumFiltersVBox.setVisible(a);
	}

	/**
	 * based on the given table visibilities, sets the visibility of each table
	 * and hides or shows cellInfo of each table depending on the state. - *no cell info
	 * @param d boolean for danceTable visibility
	 * @param p boolean for publicationTable visibility
	 * @param r boolean for recordingTable visibility
	 * @param a boolean for albumTable visibility
	 */
	public void tableVisibility(boolean d, boolean p, boolean r, boolean a){
		danceTable.getTable().setVisible(d);
		publicationTable.getTable().setVisible(p);
		recordingTable.getTable().setVisible(r);
		albumTable.getTable().setVisible(a);
	}
	
	//javadoc
	public void cellInfoVisibility(boolean d, boolean p, boolean r, boolean a){
		danceTable.getCellInfo().setVisible(d);
		publicationTable.getCellInfo().setVisible(p);
		recordingTable.getCellInfo().setVisible(r);
		albumTable.getCellInfo().setVisible(a);
	}

	/**
	 * sets up the advanced search filter button that depends on the navigation buttons and search filters.
	 * based on the state and selected property of the radio button, sets the radio button test
	 * and sets the visibility of each filters box respectively. 
	 */
	public void navSearchFilter(){
		advSF.setText("Show Advanced Search Options For Dance");
		this.searchVBox.getChildren().add(advSF);
		// when the state is changed, 
		// the other three search filters box should be hidden
		advSF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(state.equals("d")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Dance");
					else advSF.setText("Show Advanced Search Options For Dance");
					searchFiltersVisibility(!danceFiltersVBox.isVisible(), false, false, false);
				}
				else if(state.equals("p")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Publication");
					else advSF.setText("Show Advanced Search Options For Publication");
					searchFiltersVisibility(false, !publicationFiltersVBox.isVisible(), false, false);
				}
				else if(state.equals("r")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Recording");
					else advSF.setText("Show Advanced Search Options For Recording");
					searchFiltersVisibility(false, false, !recordingFiltersVBox.isVisible(), false);
				}
				else{
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Album");
					else advSF.setText("Show Advanced Search Options For Album");
					searchFiltersVisibility(false, false, false, !albumFiltersVBox.isVisible());
				}
			}
		});
	}

	public void searchFilters() throws SQLException, MalformedURLException {
		DanceFilters df = new DanceFilters();
		danceFiltersVBox = df.getDanceFiltersVBox();
		this.searchVBox.getChildren().add(danceFiltersVBox);
		danceFiltersVBox.managedProperty().bind(danceFiltersVBox.visibleProperty());
		danceFiltersVBox.setVisible(false);

		RecordingFilters rf = new RecordingFilters();
		recordingFiltersVBox = rf.getRecordingFiltersVBox();
		this.searchVBox.getChildren().add(recordingFiltersVBox);
		recordingFiltersVBox.managedProperty().bind(recordingFiltersVBox.visibleProperty());
		recordingFiltersVBox.setVisible(false);

		AlbumFilters af = new AlbumFilters();
		albumFiltersVBox = af.getAlbumFiltersVBox();
		this.searchVBox.getChildren().add(albumFiltersVBox);
		albumFiltersVBox.managedProperty().bind(albumFiltersVBox.visibleProperty());
		albumFiltersVBox.setVisible(false);

		PublicationFilters pf = new PublicationFilters();
		publicationFiltersVBox = pf.getPublicationFiltersVBox();
		this.searchVBox.getChildren().add(publicationFiltersVBox);
		publicationFiltersVBox.managedProperty().bind(publicationFiltersVBox.visibleProperty());
		publicationFiltersVBox.setVisible(false);
	}

	/**
	 * adds the given table and cellInfo to this class' VBox and binds when they are shown or 
	 * hidden to their visible property in order to collapse respective screens.
	 * @param table TableView of the passed in table
	 * @param cellInfo VBox representing the information from a single cell of the passed in table
	 */
	public void setUpTable(TableView<?> table, VBox cellInfo){
		this.searchVBox.getChildren().add(table);
		table.managedProperty().bind(table.visibleProperty());
		this.searchVBox.getChildren().add(cellInfo);
		cellInfo.managedProperty().bind(cellInfo.visibleProperty());
	}

	//GETTERS FOR TESTING
	public VBox getDanceFiltersVBox() {return danceFiltersVBox;}
	public VBox getRecordingFiltersVBox() {return recordingFiltersVBox;}
	public VBox getAlbumFiltersVBox() {return albumFiltersVBox;}
	public VBox getPublicationFiltersVBox() {return publicationFiltersVBox;}
	public Database getDb() {return db;}
	public RecordTable getDanceTable() {return danceTable;}
	public RecordTable getPublicationTable() {return publicationTable;}
	public RecordTable getRecordingTable() {return recordingTable;}
	public RecordTable getAlbumTable() {return albumTable;}
	public TextField getSearch() {return search;}
	public RadioButton getAdvSF() {return advSF;}
	public String getState() {return state;}
}