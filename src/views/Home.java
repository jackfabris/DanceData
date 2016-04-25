package views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Home {
	
	private VBox homeVBox;
	private Database db;
	private Scene scene;


	public Home(Scene s) throws SQLException, MalformedURLException{
		homeVBox = new VBox(10);
		db = new Database();	
		scene = s;
		lastUpdate();
		setUp();
	}

	/**
	 * Display how long it has been since the user's local copy of the
	 * DanceData database was updated and provide the option (via button)
	 * for the user to update their copy
	 */
	public void lastUpdate(){
		HBox updateHBox = new HBox(10);

		updateHBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5 10 5 5;" +
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
	
	/**
	 * Generate the main body of the home page (introduction text, general 
	 * features, copyright information, etc.)
	 */
	public void setUp() {
		VBox homeTextVBox = new VBox(10);
		homeTextVBox.setId("home");
		
		// Welcome
		Text welcome = new Text("Welcome to GhillieTracks 2.0!");
		welcome.setFont(Font.font(null, FontWeight.BOLD, 15));
		homeTextVBox.getChildren().add(welcome);
		
		Text text = new Text(
				"GhillieTracks is an application designed to help teachers, " +
				"class managers, and dancers in the Scottish Country Dance " +
				"community research and manage dance and music resources.\n\n" +
				
				"This application was inspired by the GhillieTracks program " +
				"created by Patty Lindsay. It retains much of the same " +
				"functionality, but any collections created in that program " +
				"will need to be recreated by the user in this version.\n\n\n" +
				
				"GhillieTracks has 2 main functions:\n\n" +
				"DanceData Search - Search DanceData without being connected " +
				"to the Internet, allowing access to the data from a laptop " +
				"in a classroom environment. Also provides the capability of " + 
				"marking dances, publications, albums, and tunes as \"I Have\" " + 
				"similar to the original DanceData application.\n\n" +
				"Collections - View a list of dances, publications, albums, " +
				"and tunes that you have marked as \"I Have.\"\n\n\n" +
				
				"Copyright 2016 University of Delaware\n" +
				"GhillieTracks is free software developed by students at " +
				"the University of Delaware in Newark, DE.");
		text.wrappingWidthProperty().bind(scene.widthProperty().subtract(15));
		homeTextVBox.getChildren().add(text);
		
//		// Intro
//		Text intro1 = new Text(
//				"Ghillie Tracks is an application designed to help teachers, " +
//				"class managers, and dancers in the Scottish Country Dance " +
//				"community research and manage dance and music resources.");
//		Text intro2 = new Text(
//				"This application was inspired by the GhillieTracks program " +
//				"created by Patty Lindsay. It retains much of the same " +
//				"functionality, but any collections created in that program " +
//				"will need to be recreated by the user in this version.");
//		Text padding1 = new Text();
//		homeTextVBox.getChildren().addAll(intro1, intro2, padding1);
//		
//		// Features
//		Text features = new Text("GhillieTracks has 2 main functions:");
//		HBox feature1HBox = new HBox(10);
//		Text feature1a = new Text("DanceData Search");
//		feature1a.setFont(Font.font(null, FontWeight.BOLD, 13));
//		Text feature1b = new Text(
//				"Search DanceData without being connected to the Internet, " +
//				"allowing access to the data from a laptop in a classroom " + 
//				"environment. Also provides the capability of marking " + 
//				"dances, publications, albums, and tunes as \"I Have\" " + 
//				"similar to the original DanceData application.");
//		feature1b.wrappingWidthProperty().bind(scene.widthProperty());
//		//feature1b.setWrapText(true);
////		feature1HBox.setHgrow(feature1b, Priority.ALWAYS);
//		feature1HBox.getChildren().addAll(feature1a, feature1b);
//		HBox feature2HBox = new HBox(10);
//		Text feature2a = new Text("Collections");
//		feature2a.setFont(Font.font(null, FontWeight.BOLD, 13));
//		Text feature2b = new Text(
//				"View a list of the dances, publications, albums, and " +
//				"tunes that have been marked as \"I Have.\"");
//		feature2HBox.getChildren().addAll(feature2a, feature2b);
//		Text padding2 = new Text();
//		homeTextVBox.getChildren().addAll(features, feature1HBox, feature2HBox, padding2);
//		
//		// Copyright
//		Text copyright1 = new Text("Copyright 2016 University of Delaware");
//		Text copyright2 = new Text(
//				"GhillieTracks is free software developed by students at " +
//				"the University of Delaware in Newark, DE.");
//		homeTextVBox.getChildren().addAll(copyright1, copyright2);
		
		this.getHomeVBox().getChildren().add(homeTextVBox);
	}

	/**
	 * Get the home VBox
	 * @return homeVBox
	 */
	public VBox getHomeVBox() {
		return homeVBox;
	}
}