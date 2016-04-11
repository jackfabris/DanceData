package views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import database.Database;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Home extends VBox {
	
	private VBox homeVBox;
	private Database db;

	public Home() throws SQLException{
		homeVBox = new VBox(10);
		db = new Database();
		lastUpdate();
		
	}
	
	public void lastUpdate(){
		HBox updateHBox = new HBox(10);
		//Date String
		Label date = new Label();
		String workingDir = System.getProperty("user.dir");
		Path path = Paths.get(workingDir + "/database/scddata.db");
		BasicFileAttributes attr;
		
		try {
			attr = Files.readAttributes(path, BasicFileAttributes.class);
			long difference = System.currentTimeMillis() - attr.creationTime().toMillis();
			int lastDays = (int) (difference / (1000*60*60*24));
			date.setText("It has been " + lastDays + " days since the database was last updated.");
			
		} catch (IOException e) {
			System.out.println("Unable to Retrieve Date of Last Update");
		}
		updateHBox.getChildren().add(date);
		
		//Update Button
		Button updateBtn = new Button("UPDATE");
		updateHBox.getChildren().add(updateBtn);
		
		this.getHomeVBox().getChildren().add(updateHBox);
	}

	public VBox getHomeVBox() {
		return homeVBox;
	}
	
}
