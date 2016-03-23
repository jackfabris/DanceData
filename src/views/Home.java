package views;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Application {

	@Override
	public void start(Stage stg) throws Exception {
		stg.setTitle("Ghillie Tracks 2.0");
		
		// Set up the grid
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));
		
		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(100);
		grid.getColumnConstraints().add(c);
		
		// Header/logo
		Text gtText = new Text("Ghillie Tracks 2.0");
		gtText.setId("header-text");
		HBox header = new HBox(10);
		header.getChildren().add(gtText);
		grid.add(header, 0, 0);
		
		// Navigation buttons
		Button homeBtn = new Button("Home");
		homeBtn.setId("home-button");
		Button searchBtn = new Button("Search");
		searchBtn.setId("search-button");
		Button collectionBtn = new Button("Collections");
		collectionBtn.setId("collection-button");
		
		HBox navBox = new HBox(10);
		navBox.getChildren().add(homeBtn);
		navBox.getChildren().add(searchBtn);
		navBox.getChildren().add(collectionBtn);
		grid.add(navBox, 0, 1);
		
		Separator sep = new Separator();
		HBox sepBox = new HBox(10);
		sepBox.getChildren().add(sep);
		sepBox.setHgrow(sep, Priority.ALWAYS);
		grid.add(sepBox, 0, 2);
		
		// Search bar
		TextField search = new TextField();
		search.setPrefWidth(300);
		Button searchGoBtn = new Button("Go");
		searchGoBtn.setPrefWidth(50);
		
		HBox searchBox = new HBox(10);
		searchBox.getChildren().add(search);
		searchBox.getChildren().add(searchGoBtn);
		grid.add(searchBox, 0, 3);
		
		// Search results
		TableView table = new TableView();
		table.setEditable(false);
		TableColumn c1 = new TableColumn("Column 1");
		TableColumn c2 = new TableColumn("Column 2");
		TableColumn c3 = new TableColumn("Column 3");
		table.getColumns().addAll(c1, c2, c3);
		
		final HBox resultsBox = new HBox(10);
		resultsBox.getChildren().add(table);
		resultsBox.setHgrow(table, Priority.ALWAYS);
		grid.add(resultsBox, 0, 4);
		
		HBox howdy = new HBox(10);
		Text test = new Text("howdy");
		howdy.getChildren().add(test);
		grid.add(howdy, 0, 5);
		
		// button logic
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				resultsBox.managedProperty().bind(resultsBox.visibleProperty());
				resultsBox.setVisible(!resultsBox.isVisible());
			}
		});
		
		Scene scene = new Scene(grid, 800, 600);
		stg.setScene(scene);
		scene.getStylesheets().add(Home.class.getResource("style.css").toExternalForm());
		stg.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
