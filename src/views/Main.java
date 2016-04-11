package views;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	
	private GridPane grid;
	private int gridY;
	private VBox home;
	private VBox search;
	private VBox collection;
	
	public Main() throws SQLException {
		grid = new GridPane();
		gridY = 0;
		Home h = new Home();
		home = h.getHomeVBox();
		Search s = new Search();
		search = s.getSearchVBox();
		Collection c = new Collection();
		collection = c.getCollectionVBox();
	}

	@Override
	public void start(Stage stg) throws Exception {
		stg.setTitle("Ghillie Tracks 2.0");
		
		setUpGrid();
		setUpHeader();
		navigationButtons();
		
		grid.add(home, 0, gridY);
		home.managedProperty().bind(home.visibleProperty());
		home.setVisible(true);
		
		grid.add(search, 0, gridY);
		search.managedProperty().bind(search.visibleProperty());
		search.setVisible(false);
		
		grid.add(collection, 0, gridY);
		collection.setVisible(false);
		collection.managedProperty().bind(collection.visibleProperty());
		
		// set icon
		stg.getIcons().add(new Image(Main.class.getResourceAsStream("ghillie.png")));
		
		Scene scene = new Scene(grid);
		stg.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		stg.setMaximized(true);
		stg.show();
	}
	
	public void setUpGrid(){
		// Set up the grid
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(100);
		grid.getColumnConstraints().add(c);
	}
	
	public void setUpHeader(){
		Text gtText = new Text("Ghillie Tracks 2.0");
		gtText.setId("header-text");
		HBox header = new HBox(10);
		header.getChildren().add(gtText);
		grid.add(header, 0, gridY++);
	}
	
	public void navigationButtons(){
		// Navigation buttons
		ToggleButton homeBtn = new ToggleButton("Home");
		homeBtn.setId("home-button");
		ToggleButton searchBtn = new ToggleButton("Search");
		searchBtn.setId("search-button");
		ToggleButton collectionBtn = new ToggleButton("Collections");
		collectionBtn.setId("collection-button");
		ToggleGroup navButtons = new ToggleGroup();
		homeBtn.setToggleGroup(navButtons);
		searchBtn.setToggleGroup(navButtons);
		collectionBtn.setToggleGroup(navButtons);
		
		HBox navBox = new HBox(10);
		navBox.getChildren().add(homeBtn);
		navBox.getChildren().add(searchBtn);
		navBox.getChildren().add(collectionBtn);
		grid.add(navBox, 0, gridY++);
		
		Separator sep = new Separator();
		HBox sepBox = new HBox(10);
		sepBox.getChildren().add(sep);
		sepBox.setHgrow(sep, Priority.ALWAYS);
		grid.add(sepBox, 0, gridY++);
		
		// button logic
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				home.setVisible(true);
				search.setVisible(false);
				collection.setVisible(false);
			}
		});
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {				
				home.setVisible(false);
				search.setVisible(true);
				collection.setVisible(false);
			}
		});
		collectionBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				home.setVisible(false);
				search.setVisible(false);
				collection.setVisible(true);
			}
		});
	}
	
	public static void main(String[] args) throws SQLException, IOException{
		launch(args);
	}
}
