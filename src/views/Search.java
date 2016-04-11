package views;

import java.sql.SQLException;

import filters.AlbumFilters;
import filters.DanceFilters;
import filters.RecordingFilters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Search extends VBox {
	
	private VBox searchVBox;
	private DanceTable danceTable;
	private VBox danceFiltersVBox;
	private VBox recordingFiltersVBox;
	private VBox albumFiltersVBox;
	
	public Search() throws SQLException {
		searchVBox = new VBox(10);
		setUpSearchBar();
		radioButtons();
		searchFilters();
		
		danceTable = new DanceTable();
		this.searchVBox.getChildren().add(danceTable.getTable());
	}
	
	public VBox getSearchVBox(){
		return this.searchVBox;
	}
	
	public void setUpSearchBar(){
		TextField search = new TextField();
		search.setPrefWidth(300);
		Button searchGoBtn = new Button("Go");
		searchGoBtn.setPrefWidth(50);
		
		HBox searchBox = new HBox(10);
		searchBox.getChildren().add(search);
		searchBox.getChildren().add(searchGoBtn);
		
		this.searchVBox.getChildren().add(searchBox);
	}
	
	public void radioButtons(){
		//Radio Buttons
		HBox radioBtnBox = new HBox(10);
		final ToggleGroup group = new ToggleGroup();
		final RadioButton rb1 = new RadioButton();
		rb1.setText("Album");
		rb1.setToggleGroup(group);
		radioBtnBox.getChildren().add(rb1);
		RadioButton rb2 = new RadioButton();
		rb2.setText("Dance");
		rb2.setToggleGroup(group);
		radioBtnBox.getChildren().add(rb2);
		RadioButton rb3 = new RadioButton();
		rb3.setText("Recording");
		rb3.setToggleGroup(group);
		radioBtnBox.getChildren().add(rb3);
		this.searchVBox.getChildren().add(radioBtnBox);
		
		// Album
		rb1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumFiltersVBox.setVisible(true);
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(false);
			}
		});
		// Dance
		rb2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(true);
				recordingFiltersVBox.setVisible(false);
			}
		});
		// Recording
		rb3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumFiltersVBox.setVisible(false);
				danceFiltersVBox.setVisible(false);
				recordingFiltersVBox.setVisible(true);
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
}
