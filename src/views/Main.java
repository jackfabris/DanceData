package views;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * Main runs the entire application and holds information regarding the three main screens
 * which are represented as VBoxes for the classes Home, Search, and Collection
 * 
 */
public class Main extends Application {
	
	private GridPane grid;
	private int gridY;
	private VBox home;
	private VBox search;
	private VBox collection;
	
	/**
	 * constructor for Main initializes Grid, Home, Search, and Collection 
	 * and sets this classes VBoxes respectively
	 * @throws SQLException
	 */
	public Main() throws SQLException, MalformedURLException {
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
	/**
	 * sets title, icon, grid, header, navigation buttons and 
	 * adds them to the scene of the application's stage
	 * @throws Exception
	 */
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
		
		stg.getIcons().add(new Image(Main.class.getResourceAsStream("ghillie.png")));
		
		Scene scene = new Scene(grid);
		stg.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		stg.setMaximized(true);
		stg.show();
	}
	
	/**
	 * sets up the grid
	 */
	public void setUpGrid(){
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(100);
		grid.getColumnConstraints().add(c);
	}
	
	/**
	 * sets up the header
	 */
	public void setUpHeader(){
		Text gtText = new Text("Ghillie Tracks 2.0");
		gtText.setId("header-text");
		HBox header = new HBox(10);
		header.getChildren().add(gtText);
		grid.add(header, 0, gridY++);
	}
	
	/**
	 * creates the toggle navigation buttons for home, search, 
	 * and collection and changes VBox visibility 
	 * of each respectively depending on the button action
	 */
	@SuppressWarnings("static-access")
	public void navigationButtons(){
		ToggleButton homeBtn = new ToggleButton("Home");
		homeBtn.setSelected(true);
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
	
	/**
	 * main method that launches the application
	 * @param args unused
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws SQLException, IOException{
		launch(args);
	}
}
