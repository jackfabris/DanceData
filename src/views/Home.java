package views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Home {
	
	private VBox homeVBox;
	private Database db;

	public Home() throws SQLException{
		homeVBox = new VBox(10);
		db = new Database();
		lastUpdate();
		setUp();
	}

	public void lastUpdate(){
		HBox updateHBox = new HBox(10);
		updateHBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
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
				db.update();
			}
		});
	}
	
	public void setUp() {
		VBox homeTextVBox = new VBox(10);
		
//		WebView browser = new WebView();
//		WebEngine webEngine = browser.getEngine();
//		
//		ScrollPane scrollPane = new ScrollPane();
//		scrollPane.setContent(browser);
//		
//		webEngine.loadContent(
//				"<h1>Welcome to GhillieTracks 2.0!</h1>");
//		
//		homeTextVBox.getChildren().add(scrollPane);
		
		// Welcome
		Text welcome = new Text("Welcome to GhillieTracks 2.0!");
		homeTextVBox.getChildren().add(welcome);
		
		// Intro
		Text intro1 = new Text(
				"Ghillie Tracks is an application designed to help teachers, " +
				"class managers, and dancers in the Scottish Country Dance " +
				"community research and manage dance and music resources.");
		Text intro2 = new Text(
				"This application was inspired by the GhillieTracks program " +
				"created by Patty Lindsay. It retains much of the same " +
				"functionality, but any collections created in that program " +
				"will need to be recreated by the user in this version.");
		homeTextVBox.getChildren().addAll(intro1, intro2);
		
		// Features
		Text features = new Text("GhillieTracks has x main functions:");
		HBox feature1HBox = new HBox(10);
		Text feature1a = new Text("DanceData Search");
		Text feature1b = new Text(
				"Search DanceData without being connected to the Internet, " +
				"allowing access to the data from a laptop in a classroom " + 
				"environment. Also provides the capability of marking " + 
				"dances, publications, albums and tunes as \"I Have\" " + 
				"similar to the original DanceData application.");
		HBox feature2HBox = new HBox(10);
		feature1HBox.getChildren().addAll(feature1a, feature1b);
		Text feature2 = new Text("Collections");
		homeTextVBox.getChildren().addAll(features, feature1HBox, feature2HBox);
		
		// Copyright
		Text copyright1 = new Text("Copyright 2016 University of Delaware");
		Text copyright2 = new Text(
				"GhillieTracks is free software developed by students at " +
				"the University of Delaware in Newark, DE.");
		homeTextVBox.getChildren().addAll(copyright1, copyright2);
		
		this.getHomeVBox().getChildren().add(homeTextVBox);
	}

	public VBox getHomeVBox() {
		return homeVBox;
	}
}