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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import tables.RecordTable;

public class SearchDataView {

	private VBox vBox, danceFiltersVBox, recordingFiltersVBox, albumFiltersVBox, publicationFiltersVBox;
	private DanceFilters df;
	private PublicationFilters pf;
	private RecordingFilters rf;
	private AlbumFilters af;
	private HBox searchBox;
	private Database db;
	public Main m;
	private RecordTable danceTable, publicationTable, recordingTable, albumTable;
	private final TextField search;
	private RadioButton advSF;
	private Button export, reset;
	private String state, danceTitle, publicationTitle, recordingTitle, albumTitle;
	private boolean isCollection;
	
	public SearchDataView(Database db, Main m, boolean isCollection) throws MalformedURLException, SQLException{
		this.m = m;
		this.isCollection = isCollection;
		vBox = new VBox(10);
		this.db = db;
		search = new TextField();
		navigationButtons();
		setUpSearchBar();
		advSF = new RadioButton();
		navSearchFilter();
		searchFilters();
		state = "d";
		danceTitle = "";
		publicationTitle = "";
		recordingTitle = "";
		albumTitle = "";
		setUpTables();
		exportButton();
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
		
		Region r = new Region();
		
		reset = new Button("Reset Dance Table");
		reset.setStyle("-fx-alignment: CENTER_RIGHT;");
		//Pos.

		searchBox = new HBox(10);
		
		searchBox.getChildren().add(search);
		searchBox.getChildren().add(searchGoBtn);
		searchBox.getChildren().add(r);
		searchBox.getChildren().add(reset);
		searchBox.setHgrow(r, Priority.ALWAYS);

		this.vBox.getChildren().add(searchBox);
		
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
		
		//Reset Button Event
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				search.setText("");
				ResultSet set;
				//reset table
				if(state.equals("d")) {
					danceTitle = "";
					try {
						set = db.searchTableByName("dance", "", isCollection);
						danceTable.setTableData(danceTable.populate(set));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if(state.equals("p")) {
					publicationTitle = "";
					try {
						set = db.searchTableByName("publication", "", isCollection);
						publicationTable.setTableData(publicationTable.populate(set));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if(state.equals("r")) {
					recordingTitle = "";
					try {
						set = db.searchTableByName("recording", "", isCollection);
						recordingTable.setTableData(recordingTable.populate(set));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if(state.equals("a")) {
					albumTitle = "";
					try {
						set = db.searchTableByName("album", "", isCollection);
						albumTable.setTableData(albumTable.populate(set));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//visibility? make it disappear on cell info
			}
		});
	}
	
	public void searchText(String title) throws SQLException {
		ResultSet set;
		if(state.equals("d")) {
			danceTitle = title;
			set = db.searchTableByName("dance",title, isCollection);
			danceTable.setTableData(danceTable.populate(set));
			
			danceTable.getTable().setVisible(true);
			danceTable.getCellInfo().setVisible(false);
			danceTable.getCellInfo().setVis(false);
		}
		else if(state.equals("p")) {
			publicationTitle = title;
			set = db.searchTableByName("publication",title, isCollection);
			publicationTable.setTableData(publicationTable.populate(set));
			
			publicationTable.getTable().setVisible(true);
			publicationTable.getCellInfo().setVisible(false);
			publicationTable.getCellInfo().setVis(false);
		}
		else if(state.equals("r")) {
			recordingTitle = title;
			set = db.searchTableByName("recording",title, isCollection);
			recordingTable.setTableData(recordingTable.populate(set));
			
			recordingTable.getTable().setVisible(true);
			recordingTable.getCellInfo().setVisible(false);
			recordingTable.getCellInfo().setVis(false);
		}
		else if(state.equals("a")) {
			albumTitle = title;
			set = db.searchTableByName("album",title, isCollection);
			albumTable.setTableData(albumTable.populate(set));
			
			albumTable.getTable().setVisible(true);
			albumTable.getCellInfo().setVisible(false);
			albumTable.getCellInfo().setVis(false);
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

		final ToggleButton danceTB = new ToggleButton();
		if(!isCollection) danceTB.setText("Dance");
		else danceTB.setText("My Dances");
		danceTB.setSelected(true);
		danceTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(danceTB);
		danceTB.setId("toggle-button");

		final ToggleButton publicationTB = new ToggleButton();
		if(!isCollection) publicationTB.setText("Publication");
		else publicationTB.setText("My Publications");
		publicationTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(publicationTB);
		publicationTB.setId("toggle-button");

		final ToggleButton recordingTB = new ToggleButton();
		if(!isCollection) recordingTB.setText("Recording");
		else recordingTB.setText("My Recordings");
		recordingTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(recordingTB);
		recordingTB.setId("toggle-button");

		final ToggleButton albumTB = new ToggleButton();
		if(!isCollection) albumTB.setText("Album");
		else albumTB.setText("My Albums");
		albumTB.setToggleGroup(tg);
		navBtnBox.getChildren().add(albumTB);
		albumTB.setId("toggle-button");

		this.vBox.getChildren().add(navBtnBox);
		
		// Dance
		danceTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				danceTB.setSelected(true);
				if(!state.equals("d")){
					advSF.setText("Show Advanced Search Options For Dance");
					searchBox.setVisible(true);
					advSF.setSelected(false);
					search.clear();
					if(!danceTable.getCellInfo().isVis()) reset.setVisible(true);
					else reset.setVisible(false);
				}
				state = "d";
				reset.setText("Reset Dance Table");
				export.setVisible(false);
				searchFiltersVisibility(danceFiltersVBox.isVisible(), false, false, false);
				tableVisibility(true, false, false, false);
				cellInfoVisibility(danceTable.getCellInfo().isVis(), false, false, false);
				if(danceTable.getCellInfo().isVis()) danceTable.getTable().setVisible(false);
				if (danceTitle.equals("")) search.setPromptText("Search by Dance Title");
				else search.setText(danceTitle);
			}
		});
		// Publication
		publicationTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				publicationTB.setSelected(true);
				if(!state.equals("p")){
					advSF.setText("Show Advanced Search Options For Publication");
					searchBox.setVisible(true);
					advSF.setSelected(false);
					search.clear();
					if(!publicationTable.getCellInfo().isVis()) reset.setVisible(true);
					else reset.setVisible(false);
				}
				state = "p";
				reset.setText("Reset Publication Table");
				if(isCollection){export.setVisible(true);}
				searchFiltersVisibility(false, publicationFiltersVBox.isVisible(), false, false);
				tableVisibility(false, true, false, false);
				cellInfoVisibility(false, publicationTable.getCellInfo().isVis(), false, false);
				if(publicationTable.getCellInfo().isVis()) publicationTable.getTable().setVisible(false);
				if (publicationTitle.equals("")) search.setPromptText("Search by Publication Title");
				else search.setText(publicationTitle);
			}
		});
		// Recording
		recordingTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				recordingTB.setSelected(true);
				if(!state.equals("r")){
					advSF.setText("Show Advanced Search Options For Recording");
					searchBox.setVisible(true);
					advSF.setSelected(false);
					search.clear();
					if(!recordingTable.getCellInfo().isVis()) reset.setVisible(true);
					else reset.setVisible(false);
				}
				state = "r";
				reset.setText("Reset Recording Table");
				export.setVisible(false);
				searchFiltersVisibility(false, false, recordingFiltersVBox.isVisible(), false);
				tableVisibility(false, false, true, false);
				cellInfoVisibility(false, false, recordingTable.getCellInfo().isVis(), false);
				if(recordingTable.getCellInfo().isVis()) recordingTable.getTable().setVisible(false);
				if (recordingTitle.equals("")) search.setPromptText("Search by Recording Title");
				else search.setText(recordingTitle);
			}
		});
		// Album
		albumTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumTB.setSelected(true);
				if(!state.equals("a")){
					advSF.setText("Show Advanced Search Options For Album");
					searchBox.setVisible(true);
					advSF.setSelected(false);
					search.clear();
					if(!albumTable.getCellInfo().isVis()) reset.setVisible(true);
					else reset.setVisible(false);
				}
				state = "a";
				reset.setText("Reset Album Table");
				if(isCollection){export.setVisible(true);}
				searchFiltersVisibility(false, false, false, albumFiltersVBox.isVisible());
				tableVisibility(false, false, false, true);
				cellInfoVisibility(false, false, false, albumTable.getCellInfo().isVis());
				if(albumTable.getCellInfo().isVis()) albumTable.getTable().setVisible(false);
				if (albumTitle.equals("")) search.setPromptText("Search by Album Title");
				else search.setText(albumTitle);
			}
		});
	}
	
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
		this.vBox.getChildren().add(advSF);
		// when the state is changed, 
		// the other three search filters box should be hidden
		advSF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {				
				searchBox.setVisible(!advSF.isSelected());
				
				if(state.equals("d")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Dance");
					else advSF.setText("Show Advanced Search Options For Dance");
					searchFiltersVisibility(!danceFiltersVBox.isVisible(), false, false, false);
					df.setTitleField(danceTitle);
				}
				else if(state.equals("p")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Publication");
					else advSF.setText("Show Advanced Search Options For Publication");
					searchFiltersVisibility(false, !publicationFiltersVBox.isVisible(), false, false);
					pf.setTitleField(publicationTitle);
				}
				else if(state.equals("r")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Recording");
					else advSF.setText("Show Advanced Search Options For Recording");
					searchFiltersVisibility(false, false, !recordingFiltersVBox.isVisible(), false);
					rf.setTitleField(recordingTitle);
				}
				else{
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Album");
					else advSF.setText("Show Advanced Search Options For Album");
					searchFiltersVisibility(false, false, false, !albumFiltersVBox.isVisible());
					af.setTitleField(albumTitle);
				}
			}
		});
	}
	
	public void searchFilters() throws SQLException, MalformedURLException {
		df = new DanceFilters(db,this);
		danceFiltersVBox = df.getFiltersVBox();
		this.vBox.getChildren().add(danceFiltersVBox);
		danceFiltersVBox.managedProperty().bind(danceFiltersVBox.visibleProperty());
		danceFiltersVBox.setVisible(false);

		rf = new RecordingFilters(db,this);
		recordingFiltersVBox = rf.getFiltersVBox();
		this.vBox.getChildren().add(recordingFiltersVBox);
		recordingFiltersVBox.managedProperty().bind(recordingFiltersVBox.visibleProperty());
		recordingFiltersVBox.setVisible(false);

		af = new AlbumFilters(db,this);
		albumFiltersVBox = af.getFiltersVBox();
		this.vBox.getChildren().add(albumFiltersVBox);
		albumFiltersVBox.managedProperty().bind(albumFiltersVBox.visibleProperty());
		albumFiltersVBox.setVisible(false);

		pf = new PublicationFilters(db,this);
		publicationFiltersVBox = pf.getFiltersVBox();
		this.vBox.getChildren().add(publicationFiltersVBox);
		publicationFiltersVBox.managedProperty().bind(publicationFiltersVBox.visibleProperty());
		publicationFiltersVBox.setVisible(false);
		
		vBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					if(state.equals("d") && df.getSearchBool()) df.callQuery();
					else if(state.equals("r") && rf.getSearchBool()) rf.callQuery();
					else if(state.equals("a") && af.getSearchBool()) af.callQuery();
					else if(state.equals("p") && pf.getSearchBool()) pf.callQuery();
				}
			}
		});
	}
	
	public void setUpTables() throws MalformedURLException, SQLException{
		danceTable = new RecordTable(db, this, "dance", "d");
		setUpTable(danceTable.getTable(), danceTable.getCellInfo());
		albumTable = new RecordTable(db, this, "album", "a");
		setUpTable(albumTable.getTable(), albumTable.getCellInfo());
		recordingTable = new RecordTable(db, this, "recording", "r");
		setUpTable(recordingTable.getTable(), recordingTable.getCellInfo());
		publicationTable = new RecordTable(db, this, "publication", "p");
		setUpTable(publicationTable.getTable(), publicationTable.getCellInfo());
		if(isCollection) showIHave();
		tableVisibility(true, false, false, false);
	}

	/**
	 * adds the given table and cellInfo to this class' VBox and binds when they are shown or 
	 * hidden to their visible property in order to collapse respective screens.
	 * @param table TableView of the passed in table
	 * @param cellInfo VBox representing the information from a single cell of the passed in table
	 */
	public void setUpTable(TableView<?> table, VBox cellInfo){
		this.vBox.getChildren().add(table);
		table.managedProperty().bind(table.visibleProperty());
		this.vBox.getChildren().add(cellInfo);
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
	
	public void exportButton(){
		export = new Button("Export as PDF");
		export.setVisible(false);
		this.vBox.getChildren().add(export);
		export.managedProperty().bind(export.visibleProperty());
		export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(isCollection && (state.equals("a") || state.equals("p"))){
					ExportPDF epdf = null;
					if(state.equals("a")) epdf = new ExportPDF(db, "My Albums.pdf", state, "album");
					else if (state.equals("p")) epdf = new ExportPDF(db, "My Publications.pdf", state, "publication");
					epdf.createPdf();
				}
				else{
					System.out.println("No.");
				}
			}
		});
	}
	
	public VBox getVBox(){
		return vBox;
	}


	public VBox getDanceFiltersVBox() {return danceFiltersVBox;}
	public String getDanceTitle() {
		return danceTitle;
	}

	public String getPublicationTitle() {
		return publicationTitle;
	}

	public String getRecordingTitle() {
		return recordingTitle;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setDanceTitle(String danceTitle) {
		this.danceTitle = danceTitle;
	}

	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}

	public void setRecordingTitle(String recordingTitle) {
		this.recordingTitle = recordingTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
	
	public boolean isCollection() {
		return isCollection;
	}
	
	public Button getReset(){
		return reset;
	}
	
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
