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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Home extends Application {
	
	private Database db;
	
	public Home() throws SQLException {
		db = new Database();
	}
	
	public Database getDatabase(){
		return db;
	}

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
		final Button homeBtn = new Button("Home");
		homeBtn.setId("home-button");
		final Button searchBtn = new Button("Search");
		searchBtn.setId("search-button");
		final Button collectionBtn = new Button("Collections");
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
		
		// button logic
		homeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeBtn.setStyle("-fx-background-color: #445878;;");
				collectionBtn.setStyle("-fx-background-color: #92cdcf");
				searchBtn.setStyle("-fx-background-color: #92cdcf;");
			}
		});
		
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeBtn.setStyle("-fx-background-color: #92cdcf;");
				searchBtn.setStyle("-fx-background-color: #445878;");
				collectionBtn.setStyle("-fx-background-color: #92cdcf;");
			}
		});
		
		collectionBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				homeBtn.setStyle("-fx-background-color: #92cdcf;");
				collectionBtn.setStyle("-fx-background-color: #445878;");
				searchBtn.setStyle("-fx-background-color: #92cdcf;");
			}
		});
		
		// Search bar
		TextField search = new TextField();
		search.setPrefWidth(300);
		Button searchGoBtn = new Button("Go");
		searchGoBtn.setPrefWidth(50);
		
		HBox searchBox = new HBox(10);
		searchBox.getChildren().add(search);
		searchBox.getChildren().add(searchGoBtn);
		grid.add(searchBox, 0, 3);
		
		//Radio Buttons
		HBox radioBtnBox = new HBox(10);
		RadioButton rb1 = new RadioButton();
		rb1.setText("Album");
		radioBtnBox.getChildren().add(rb1);
		RadioButton rb2 = new RadioButton();
		rb2.setText("Dance");
		radioBtnBox.getChildren().add(rb2);
		RadioButton rb3 = new RadioButton();
		rb3.setText("Recording");
		radioBtnBox.getChildren().add(rb3);
		grid.add(radioBtnBox, 0, 4);
		
		//Search specifics
		final HBox albumSearchBox = new HBox(10);
		Label albumSearch = new Label();
		albumSearch.setText("Album Search Specifics");
		albumSearchBox.getChildren().add(albumSearch);
		albumSearchBox.managedProperty().bind(albumSearchBox.visibleProperty());
		albumSearchBox.setVisible(false);
		grid.add(albumSearchBox, 0, 5);
		
		rb1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumSearchBox.setVisible(!albumSearchBox.isVisible());
			}
		});
		
		// Search results
		TableView<Dance> table = new TableView<Dance>();
		table.setEditable(false);
		/*TableColumn c1 = new TableColumn("Column 1");
		TableColumn c2 = new TableColumn("Column 2");
		TableColumn c3 = new TableColumn("Column 3");
		table.getColumns().addAll(c1, c2, c3);*/
		ResultSet danceSet = db.searchDanceByTitle("");
		/*ResultSetMetaData rsmd = danceSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for(int i=1; i <= columnsNumber; i++){
			table.getColumns().add(new TableColumn<Dance, String>(rsmd.getColumnName(i)));
		}
		*/
		TableColumn<Dance, String> c1 = new TableColumn<Dance, String>("Name");
		c1.setCellValueFactory(
				new PropertyValueFactory<Dance, String>("name"));
		TableColumn<Dance, String> c2 = new TableColumn<Dance, String>("Shape");
		c2.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("shapeid"));
		TableColumn<Dance, String> c3 = new TableColumn<Dance, String>("Type");
		c3.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("typeid"));
		TableColumn<Dance, String> c4 = new TableColumn<Dance, String>("Author");
		c4.setCellValueFactory(
                new PropertyValueFactory<Dance, String>("devisorid"));
		
		ObservableList<Dance> data = FXCollections.observableArrayList();
		while(danceSet.next()){
			data.add(new Dance(danceSet.getString(3), danceSet.getString(5), danceSet.getString(6), danceSet.getString(8)));
		}
		table.setItems(data);
		table.getColumns().addAll(c1, c2, c3, c4);
		
		final HBox resultsBox = new HBox(10);
		resultsBox.getChildren().add(table);
		resultsBox.setHgrow(table, Priority.ALWAYS);
		grid.add(resultsBox, 0, 6);
		
		Scene scene = new Scene(grid);
		stg.setScene(scene);
		scene.getStylesheets().add(Home.class.getResource("style.css").toExternalForm());
		stg.setMaximized(true);
		stg.show();
	}
	
	public static void main(String[] args) throws SQLException, IOException{
		launch(args);
	}
	
	public static class Dance{
		//private final SimpleStringProperty id;
		//private final SimpleStringProperty barsperrepeat;
		private final String name;
		//private final SimpleStringProperty ucname;
		private final String shapeid; 
		private final String typeid;
		//private final SimpleStringProperty couples_id;
		private final String devisorid;
		//private final SimpleStringProperty verified;
		//private final SimpleStringProperty lastmod;
		//private final SimpleStringProperty devised;
		//private final SimpleStringProperty notes;
		//private final SimpleStringProperty medleytype_id;
		//private final SimpleStringProperty progression_id;
		//private final SimpleStringProperty url;
		//private final SimpleStringProperty creationdate;
		
		private Dance(String nameString, String shape, String type, String dev){
			name = nameString;
			shapeid = shape;
			typeid = type;
			devisorid = dev;
		}

		public String getName() {
			return name;
		}

		public String getShapeid() {
			return shapeid;
		}

		public String getTypeid() {
			return typeid;
		}

		public String getDevisorid() {
			return devisorid;
		}
		
	}
}
