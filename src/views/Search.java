package views;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import filters.AlbumFilters;
import filters.DanceFilters;
import filters.RecordingFilters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tables.DanceTable;
import tables.DanceTable.Dance;

public class Search {
	
	private VBox searchVBox;
	private Database db;
	private DanceTable danceTable;
	private VBox danceFiltersVBox;
	private VBox recordingFiltersVBox;
	private VBox albumFiltersVBox;
	
	public Search() throws SQLException {
		searchVBox = new VBox(10);
		db = new Database();
		setUpSearchBar();
		radioButtons();
		searchFilters();
		danceTable = new DanceTable();
		setUpDanceTable();
	}
	
	public VBox getSearchVBox(){
		return this.searchVBox;
	}
	
	public void setUpSearchBar(){
		//Search Bar
		final TextField search = new TextField();
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
					danceSearch(search.getText());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		searchBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					try {
						danceSearch(search.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void danceSearch(String title) throws SQLException{
		ResultSet danceSet = db.searchTableByName("dance",title);
		ObservableList<Dance> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Dance(danceSet.getString(3), danceSet.getString(5), danceSet.getString(6), danceSet.getString(8)));
		}
		danceTable.setTableData(data);
	}
	
	public void radioButtons(){
		//Radio Buttons
		HBox radioBtnBox = new HBox(10);
		
		final RadioButton albumRB = new RadioButton();
		albumRB.setText("Album");
		radioBtnBox.getChildren().add(albumRB);
		
		final RadioButton danceRB = new RadioButton();
		danceRB.setText("Dance");
		danceRB.setSelected(true);
		radioBtnBox.getChildren().add(danceRB);
		
		final RadioButton recordingRB = new RadioButton();
		recordingRB.setText("Recording");
		radioBtnBox.getChildren().add(recordingRB);
		
		this.searchVBox.getChildren().add(radioBtnBox);
		
		// Album
		albumRB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumRB.setSelected(true);
				//unselect other radio buttons
				danceRB.setSelected(false);
				recordingRB.setSelected(false);
				//search specifics
				albumFiltersVBox.setVisible(!albumFiltersVBox.isVisible());
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(false);
			}
		});
		// Dance
		danceRB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				danceRB.setSelected(true);
				//unselect other radio buttons
				albumRB.setSelected(false);
				recordingRB.setSelected(false);
				//search specifics
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(!danceFiltersVBox.isVisible());
				recordingFiltersVBox.setVisible(false);
			}
		});
		// Recording
		recordingRB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				recordingRB.setSelected(true);
				//unselect other radio buttons
				albumRB.setSelected(false);
				danceRB.setSelected(false);
				//search specifics
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(!recordingFiltersVBox.isVisible());
			}
		});
	}
	
	public void searchFilters() {
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
	}
	
	public void setUpDanceTable(){
		this.searchVBox.getChildren().add(danceTable.getTable());
		danceTable.getTable().managedProperty().bind(danceTable.getTable().visibleProperty());
		this.searchVBox.getChildren().add(danceTable.getCellInfo());
		danceTable.getCellInfo().managedProperty().bind(danceTable.getCellInfo().visibleProperty());
	}
}
