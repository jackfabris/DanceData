package views;
import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	private VBox home, search, collection;
	private ScrollPane homeSP, searchSP, collectionSP;
	private Scene scene;
	
	public static ReadOnlyDoubleProperty sceneWidthProp;
	public static double width;
	
	/**
	 * constructor for Main initializes Grid, Home, Search, and Collection 
	 * and sets this classes VBoxes respectively
	 * @throws SQLException
	 * @throws IOException 
	 */
	public Main() throws SQLException, IOException {
		grid = new GridPane();
		gridY = 0;
		scene = new Scene(grid);
		sceneWidthProp = scene.widthProperty();
		Home h = new Home();
		home = h.getHomeVBox();
		SearchDataView s = new SearchDataView(false);
		search = s.getVBox();
		SearchDataView c = new SearchDataView(true);
		collection = c.getVBox();
		homeSP = new ScrollPane();
		searchSP = new ScrollPane();
		collectionSP = new ScrollPane();
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
		scrollPaneVBox(homeSP, home, true);
		scrollPaneVBox(searchSP, search, false);
		scrollPaneVBox(collectionSP, collection, false);
		
		stg.getIcons().add(new Image(Main.class.getResourceAsStream("ghillie.png")));

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
				homeSP.setVisible(true);
				searchSP.setVisible(false);
				collectionSP.setVisible(false);
			}
		});
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {				
				homeSP.setVisible(false);
				searchSP.setVisible(true);
				collectionSP.setVisible(false);
			}
		});
		collectionBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeSP.setVisible(false);
				searchSP.setVisible(false);
				collectionSP.setVisible(true);
			}
		});
	}
	
	/**
	 * takes this classes VBoxes and put them in a Scroll Pane
	 */
	public void scrollPaneVBox(ScrollPane sp, VBox box, boolean vis){
		//Home
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setContent(box);
		grid.add(sp, 0, gridY);
		sp.managedProperty().bind(sp.visibleProperty());
		sp.setVisible(vis);
		sp.setId("scroll-pane");
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
