package views;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Home {
	
	private VBox homeVBox;
	private Database db;
	private TextFormatter tf;

	public Home(Database db) throws SQLException, IOException{
		homeVBox = new VBox(10);
		this.db = db;
		tf = new TextFormatter();
		lastUpdate();
		setUp();
	}

	/**
	 * Display how long it has been since the user's local copy of the
	 * DanceData database was updated and provide the option (via button)
	 * for the user to update their copy
	 */
	public void lastUpdate(){
		final VBox updateVBox = new VBox(10);
		updateVBox.setAlignment(Pos.CENTER);
		updateVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5 10 5 5;" +
				"-fx-border-color: #cfcfcf;");
		//Date String
		final Label date = new Label();
		String workingDir = System.getProperty("user.dir");
		Path path = Paths.get(workingDir + "/database/scddata.db");
		BasicFileAttributes attr;
		try {
			attr = Files.readAttributes(path, BasicFileAttributes.class);
			long difference = System.currentTimeMillis() - attr.lastModifiedTime().toMillis();
			int lastDays = (int) (difference / (1000*60*60*24));
			date.setText("It has been " + lastDays + " days since the database was last updated.");
		} catch (IOException e) {
			System.out.println("Unable to Retrieve Date of Last Update");
		}
		updateVBox.getChildren().add(date);
		
		//Update Button
		Button updateBtn = new Button("UPDATE");
		updateBtn.setId("update");
		updateVBox.getChildren().add(updateBtn);
		final Label status = new Label();
		status.setVisible(false);
		updateVBox.getChildren().add(status);
		this.getHomeVBox().getChildren().add(updateVBox);
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int result = db.update();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Status of Database Update");
				alert.setHeaderText(null);
				if(result == 1) status.setText("Update Successful!");
				else if(result == 0) status.setText("Cannot Update. Please connect to Internet and try again.");
				else if(result == -1) status.setText("An error has occured on update.");
				else if(result == -2) status.setText("A fatal error has occured. Please restart Ghillie Tracks 2.0");
				status.setVisible(true);
				date.setText("It has been 0 days since the database was last updated.");
			}
		});
	}
	
	/**
	 * Generate the main body of the home page (introduction text, general 
	 * features, copyright information, etc.)
	 * @throws IOException 
	 */
	public void setUp() throws IOException {
		VBox homeTextVBox = new VBox(10);
		homeTextVBox.setId("home");
		
		// Welcome
		Text welcome = new Text("Welcome to GhillieTracks 2.0!");
		welcome.setFont(Font.font(null, FontWeight.BOLD, 15));
		homeTextVBox.getChildren().add(welcome);
		
		// Read the content from "home.txt" to display on home screen
		
		String workingDir = System.getProperty("user.dir");
		String path = workingDir + "/src/views/home.txt";
		TextFlow homeTextFlow = tf.createTextFlow(path);
		homeTextFlow.maxWidthProperty().bind(Main.sceneWidthProp.subtract(54));
		homeTextVBox.getChildren().add(homeTextFlow);
		
		this.getHomeVBox().getChildren().add(homeTextVBox);
	}

	/**
	 * Get the home VBox
	 * @return homeVBox
	 */
	public VBox getHomeVBox() {
		return homeVBox;
	}
	
	/**
	 * Read the contents of a file and return them as a String. This will be
	 * used to read the contents of the "home.txt" file which will contain
	 * all of the text that is to be displayed on the GhillieTracks home
	 * screen (for the sake of easy editing)
	 * @param path - the path of the file to be read
	 * @param encoding
	 * @return String
	 * @throws IOException
	 */
	public String readFile(Path path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(path);
		return new String(encoded, encoding);
	}
}