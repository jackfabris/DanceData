package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * DanceFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conducting a dance search, such as specifying
 * type, number of bars, couples, set shapes, author, formations, and steps
 * @author Jack
 *
 */
public class DanceFilters extends VBox {
	
	private VBox danceFiltersVBox;
	private Database db;
	
	/**
	 * Create the VBox which will contain the filters for a dance search
	 * @throws SQLException
	 */
	public DanceFilters() throws SQLException, MalformedURLException {
		danceFiltersVBox = new VBox(10);
		danceFiltersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		
		db = new Database();
		
		// https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm#BABDBJBD
		// Make observable list using database query to get options
		
		// Type
		ResultSet typesSet;
		typesSet = db.doQuery("SELECT name FROM dancetype");
		ObservableList<String> typesList = FXCollections.observableArrayList();
		while(typesSet.next()) {
			typesList.add(typesSet.getString(1));
		}
		Label type = new Label("Type");
		ComboBox<String> typeOptions = new ComboBox<String>(typesList);
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
		
		// Bars
		Label bars = new Label("Bars");
		TextField barsField = new TextField();
		grid.add(bars, 0, 1);
		grid.add(barsField, 1, 1);
		
		// Couples
		ResultSet couplesSet;
		couplesSet = db.doQuery("SELECT name FROM couples");
		ObservableList<String> couplesList = FXCollections.observableArrayList();
		while(couplesSet.next()) {
			couplesList.add(couplesSet.getString(1));
		}
		Label couples = new Label("Couples");
		ComboBox<String> couplesOptions = new ComboBox<String>(couplesList);
		grid.add(couples, 0, 2);
		grid.add(couplesOptions, 1, 2);
		
		// Set Shape
		ResultSet setShapeSet;
		setShapeSet = db.doQuery("SELECT name FROM shape");
		ObservableList<String> setShapeList = FXCollections.observableArrayList();
		while(setShapeSet.next()) {
			setShapeList.add(setShapeSet.getString(1));
		}
		Label setShape = new Label("Set Shape");
		ComboBox<String> setShapeOptions = new ComboBox<String>(setShapeList);
		grid.add(setShape, 0, 3);
		grid.add(setShapeOptions, 1, 3);
		
		// Author
		Label author = new Label("Author");
		TextField authorField = new TextField();
		grid.add(author, 0, 4);
		grid.add(authorField, 1, 4);
		
		// Formations
		ResultSet formationSet;
		formationSet = db.doQuery("SELECT name FROM formation");
		ObservableList<String> formationList = FXCollections.observableArrayList();
		while(formationSet.next()) {
			formationList.add(formationSet.getString(1));
		}
		Label formations = new Label("Formations");
		ComboBox<String> formationOptions1 = new ComboBox<String>(formationList);
		ComboBox<String> formationBool1 = new ComboBox<String>();
		formationBool1.getItems().addAll("and", "or", "but not");
		ComboBox<String> formationOptions2 = new ComboBox<String>(formationList);
		ComboBox<String> formationBool2 = new ComboBox<String>();
		formationBool2.getItems().addAll("and", "or", "but not");
		ComboBox<String> formationOptions3 = new ComboBox<String>(formationList);
		grid.add(formations, 0, 5);
		grid.add(formationOptions1, 1, 5);
		grid.add(formationBool1, 2, 5);
		grid.add(formationOptions2, 1, 6);
		grid.add(formationBool2, 2, 6);
		grid.add(formationOptions3, 1, 7);
		
		// Steps
		ResultSet stepSet;
		stepSet = db.doQuery("SELECT name FROM step");
		ObservableList<String> stepList = FXCollections.observableArrayList();
		while(stepSet.next()) {
			stepList.add(stepSet.getString(1));
		}
		Label steps = new Label("Steps");
		ComboBox<String> stepOptions1 = new ComboBox<String>(stepList);
		ComboBox<String> stepBool1 = new ComboBox<String>();
		stepBool1.getItems().addAll("and", "or", "but not");
		ComboBox<String> stepOptions2 = new ComboBox<String>(stepList);
		ComboBox<String> stepBool2 = new ComboBox<String>();
		stepBool2.getItems().addAll("and", "or", "but not");
		ComboBox<String> stepOptions3 = new ComboBox<String>(stepList);
		grid.add(steps, 0, 8);
		grid.add(stepOptions1, 1, 8);
		grid.add(stepBool1, 2, 8);
		grid.add(stepOptions2, 1, 9);
		grid.add(stepBool2, 2, 9);
		grid.add(stepOptions3, 1, 10);
		
		this.danceFiltersVBox.getChildren().add(grid);
	}
	
	/**
	 * Get the dance filters VBox
	 * @return danceFiltersVBox
	 */
	public VBox getDanceFiltersVBox() {
		return this.danceFiltersVBox;
	}

}
