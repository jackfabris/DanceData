package views;

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
import tables.AlbumTable;
import tables.DanceTable;
import tables.PublicationTable;
import tables.RecordingTable;

public class Search {
	
	private VBox searchVBox;
	private Database db;
	private DanceTable danceTable;
	private PublicationTable publicationTable;
	private RecordingTable recordingTable;
	private AlbumTable albumTable;
	private final TextField search;
	private RadioButton advSF;
	private VBox danceFiltersVBox;
	private VBox recordingFiltersVBox;
	private VBox albumFiltersVBox;
	private VBox publicationFiltersVBox;
	private String state; // "d", "p", "r", a"
	
	public Search() throws SQLException {
		searchVBox = new VBox(10);
		db = new Database();
		search = new TextField();
		setUpSearchBar();
		navigationButtons();
		advSF = new RadioButton();
		navSearchFilter();
		searchFilters();
		state = "d";
		danceTable = new DanceTable();
		setUpTable(danceTable.getTable(), danceTable.getCellInfo());
		albumTable = new AlbumTable();
		setUpTable(albumTable.getTable(), albumTable.getCellInfo());
		recordingTable = new RecordingTable();
		setUpTable(recordingTable.getTable(), recordingTable.getCellInfo());
		publicationTable = new PublicationTable();
		setUpTable(publicationTable.getTable(), publicationTable.getCellInfo());
		tableVisibility(true, false, false, false);
	}
	
	public VBox getSearchVBox(){
		return this.searchVBox;
	}
	
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
	
	public void searchText(String title) throws SQLException{
		ResultSet set;
		if(state.equals("d")) {
			set = db.searchTableByName("dance",title);
			danceTable.setTableData(danceTable.populate(set));
		}
		else if(state.equals("p")) {
			set = db.searchTableByName("publication",title);
			publicationTable.setTableData(publicationTable.populate(set));
		}
		else if(state.equals("r")) {
			set = db.searchTableByName("recording",title);
			recordingTable.setTableData(recordingTable.populate(set));
		}
		else if(state.equals("a")) {
			set = db.searchTableByName("album",title);
			albumTable.setTableData(albumTable.populate(set));
		}
	}
	
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
				search.setPromptText("Search by Dance Title");
				advSF.setText("Show Advanced Search Options For Dance");
				if(!state.equals("d")){
					advSF.setSelected(false);
					search.clear();
				}
				//hide/show tables/cellInfo
				state = "d";
				tableVisibility(true, false, false, false);
				//search specifics
				albumFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(false);
				publicationFiltersVBox.setVisible(false);
			}
		});
		// Publication
		publicationTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				search.setPromptText("Search by Publication Title");
				advSF.setText("Show Advanced Search Options For Publication");
				if(!state.equals("p")){
					advSF.setSelected(false);
					search.clear();
				}
				//hide/show tables/cellInfo
				state = "p";
				tableVisibility(false, true, false, false);
				//search specifics
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(false);
			}
		});
		// Recording
		recordingTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				search.setPromptText("Search by Recording Title");
				advSF.setText("Show Advanced Search Options For Recording");
				if(!state.equals("r")){
					advSF.setSelected(false);
					search.clear();
				}
				//hide/show tables/cellInfo
				state = "r";
				tableVisibility(false, false, true, false);
				//search specifics
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(false);
				publicationFiltersVBox.setVisible(false);
			}
		});
		//Album
		albumTB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				search.setPromptText("Search by Album Title");
				advSF.setText("Show Advanced Search Options For Album");
				if(!state.equals("a")){
					advSF.setSelected(false);
					search.clear();
				}
				//hide/show tables/cellInfo
				state = "a";
				tableVisibility(false, false, false, true);
				//search specifics
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(false);
				publicationFiltersVBox.setVisible(false);
			}
		});
	}
	
	public void tableVisibility(boolean d, boolean p, boolean r, boolean a){
		danceTable.getTable().setVisible(d);
		publicationTable.getTable().setVisible(p);
		recordingTable.getTable().setVisible(r);
		albumTable.getTable().setVisible(a);
		
		//cellInfo
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
	
	public void navSearchFilter(){
		advSF.setText("Show Advanced Search Options For Dance");
		this.searchVBox.getChildren().add(advSF);
		
		advSF.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(state.equals("d")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Dance");
					else advSF.setText("Show Advanced Search Options For Dance");
					albumFiltersVBox.setVisible(false);
					danceFiltersVBox.setVisible(!danceFiltersVBox.isVisible());
					recordingFiltersVBox.setVisible(false);
					publicationFiltersVBox.setVisible(false);
				}
				else if(state.equals("p")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Publication");
					else advSF.setText("Show Advanced Search Options For Publication");
					albumFiltersVBox.setVisible(false);
					danceFiltersVBox.setVisible(false);
					recordingFiltersVBox.setVisible(false);
					publicationFiltersVBox.setVisible(!publicationFiltersVBox.isVisible());
				}
				else if(state.equals("r")){
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Recording");
					else advSF.setText("Show Advanced Search Options For Recording");
					albumFiltersVBox.setVisible(false);
					danceFiltersVBox.setVisible(false);
					recordingFiltersVBox.setVisible(!recordingFiltersVBox.isVisible());
					publicationFiltersVBox.setVisible(false);
				}
				else{
					if(advSF.isSelected()) advSF.setText("Hide Advanced Search Options For Album");
					else advSF.setText("Show Advanced Search Options For Album");
					albumFiltersVBox.setVisible(!albumFiltersVBox.isVisible());
					danceFiltersVBox.setVisible(false);
					recordingFiltersVBox.setVisible(false);
					publicationFiltersVBox.setVisible(false);
				}
			}
		});
	}
	
	public void searchFilters() throws SQLException {
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
	
	public void setUpTable(TableView<?> table, VBox cellInfo){
		this.searchVBox.getChildren().add(table);
		table.managedProperty().bind(table.visibleProperty());
		this.searchVBox.getChildren().add(cellInfo);
		cellInfo.managedProperty().bind(cellInfo.visibleProperty());
	}
}
