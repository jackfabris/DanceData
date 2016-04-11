package views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import database.Update;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Home {
	
	private VBox homeVBox;

	public Home() throws SQLException{
		homeVBox = new VBox(10);
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
			long difference = System.currentTimeMillis() - attr.lastModifiedTime().toMillis();
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
		
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					Update.update();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VBox getHomeVBox() {
		return homeVBox;
	}
}
