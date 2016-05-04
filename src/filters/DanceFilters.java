package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * DanceFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conducting a dance search, such as specifying
 * type, number of bars, couples, set shapes, author, formations, and steps
 * @author Jack
 *
 */
public class DanceFilters extends AdvancedFilters {

	private String[] formationStringArray = {"", "or", "", "or", ""};
	private String[] stepsStringArray = {"", "or", "", "or", ""};
	
	/**
	 * Create the VBox which will contain the filters for a dance search
	 * @throws SQLException
	 */
	public DanceFilters() throws SQLException, MalformedURLException {
		super();
		type();
		bars();
		couples();
		setShape();
		author();
		formations();
		steps();
		buttonGrid();
	}
	
	public void printMap(){
		//TESTING
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			System.out.println(x+", "+map.get(x));
		}
		System.out.println();
	}
		
		// https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm#BABDBJBD
		// Make observable list using database query to get options
		
	public void type() throws SQLException{
		map.put("Type", "");
		// Type
		ResultSet typesSet;
		typesSet = db.doQuery("SELECT name FROM dancetype");
		ObservableList<String> typesList = FXCollections.observableArrayList("");
		while(typesSet.next()) {
			typesList.add(typesSet.getString(1));
		}
		Collections.sort(typesList);
		Label type = new Label("Type");
		final ComboBox<String> typeOptions = new ComboBox<String>(typesList);
		typeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Type", typeOptions.getValue());

				//printMap();
			}
		});
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
	}
	
	public void bars(){
		map.put("Bars", "");
		// Bars
		Label bars = new Label("Bars");
		final TextField barsField = new TextField();
		barsField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Bars", barsField.getText());

				//printMap();
			}
		});
		grid.add(bars, 0, 1);
		grid.add(barsField, 1, 1);
	}
	
	public void couples() throws SQLException{
		map.put("Couples", "");
		// Couples
		ResultSet couplesSet;
		couplesSet = db.doQuery("SELECT name FROM couples");
		ObservableList<String> couplesList = FXCollections.observableArrayList("");
		while(couplesSet.next()) {
			couplesList.add(couplesSet.getString(1));
		}
		Collections.sort(couplesList);
		Label couples = new Label("Couples");
		final ComboBox<String> couplesOptions = new ComboBox<String>(couplesList);
		couplesOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Couples", couplesOptions.getValue());

				//printMap();
			}
		});
		grid.add(couples, 0, 2);
		grid.add(couplesOptions, 1, 2);
	}
	
	public void setShape() throws SQLException{
		map.put("Set Shape", "");
		// Set Shape
		ResultSet setShapeSet;
		setShapeSet = db.doQuery("SELECT name FROM shape");
		ObservableList<String> setShapeList = FXCollections.observableArrayList("");
		while(setShapeSet.next()) {
			setShapeList.add(setShapeSet.getString(1));
		}
		Collections.sort(setShapeList);
		Label setShape = new Label("Set Shape");
		final ComboBox<String> setShapeOptions = new ComboBox<String>(setShapeList);
		setShapeOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Set Shape", setShapeOptions.getValue());

				//printMap();
			}
		});
		grid.add(setShape, 0, 3);
		grid.add(setShapeOptions, 1, 3);
	}
	
	public void author(){
		map.put("Author", "");
		// Author
		Label author = new Label("Author");
		final TextField authorField = new TextField();
		authorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Author", authorField.getText());

				//printMap();
			}
		});
		grid.add(author, 0, 4);
		grid.add(authorField, 1, 4);
	}
	
	public void formations() throws SQLException{
		map.put("Formations", "");
		// Formations
		ResultSet formationSet;
		formationSet = db.doQuery("SELECT name FROM formation");
		ObservableList<String> formationList = FXCollections.observableArrayList("");
		while(formationSet.next()) {
			formationList.add(formationSet.getString(1));
		}
		Collections.sort(formationList);
		Label formations = new Label("Formations");
		
		final ComboBox<String> formationOptions1 = new ComboBox<String>(formationList);
		formationOptions1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(formationStringArray, formationOptions1.getValue(), 0);
				map.put("Formations", arrayToString(formationStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> formationBool1 = new ComboBox<String>();
		formationBool1.getItems().addAll("and", "or", "not");
		formationBool1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(formationStringArray, formationBool1.getValue(), 1);
				map.put("Formations", arrayToString(formationStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> formationOptions2 = new ComboBox<String>(formationList);
		formationOptions2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(formationStringArray, formationOptions2.getValue(), 2);
				map.put("Formations", arrayToString(formationStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> formationBool2 = new ComboBox<String>();
		formationBool2.getItems().addAll("and", "or", "not");
		formationBool2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(formationStringArray, formationBool2.getValue(), 3);
				map.put("Formations", arrayToString(formationStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> formationOptions3 = new ComboBox<String>(formationList);
		formationOptions3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(formationStringArray, formationOptions3.getValue(), 4);
				map.put("Formations", arrayToString(formationStringArray));
				
				//printMap();
			}
		});
		
		grid.add(formations, 0, 5);
		grid.add(formationOptions1, 1, 5);
		grid.add(formationBool1, 2, 5);
		grid.add(formationOptions2, 1, 6);
		grid.add(formationBool2, 2, 6);
		grid.add(formationOptions3, 1, 7);
	}
	
	public void steps() throws SQLException{
		map.put("Steps", "");
		// Steps
		ResultSet stepSet;
		stepSet = db.doQuery("SELECT name FROM step");
		ObservableList<String> stepList = FXCollections.observableArrayList("");
		while(stepSet.next()) {
			stepList.add(stepSet.getString(1));
		}
		Collections.sort(stepList);
		Label steps = new Label("Steps");
		final ComboBox<String> stepOptions1 = new ComboBox<String>(stepList);
		stepOptions1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(stepsStringArray, stepOptions1.getValue(), 0);
				map.put("Steps", arrayToString(stepsStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> stepBool1 = new ComboBox<String>();
		stepBool1.getItems().addAll("and", "or", "not");
		stepBool1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(stepsStringArray, stepBool1.getValue(), 1);
				map.put("Steps", arrayToString(stepsStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> stepOptions2 = new ComboBox<String>(stepList);
		stepOptions2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(stepsStringArray, stepOptions2.getValue(), 2);
				map.put("Steps", arrayToString(stepsStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> stepBool2 = new ComboBox<String>();
		stepBool2.getItems().addAll("and", "or", "not");
		stepBool2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(stepsStringArray, stepBool2.getValue(), 3);
				map.put("Steps", arrayToString(stepsStringArray));

				//printMap();
			}
		});
		
		final ComboBox<String> stepOptions3 = new ComboBox<String>(stepList);
		stepOptions3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				updateString(stepsStringArray, stepOptions3.getValue(), 4);
				map.put("Steps", arrayToString(stepsStringArray));
				
				//printMap();
			}
		});
		
		grid.add(steps, 0, 8);
		grid.add(stepOptions1, 1, 8);
		grid.add(stepBool1, 2, 8);
		grid.add(stepOptions2, 1, 9);
		grid.add(stepBool2, 2, 9);
		grid.add(stepOptions3, 1, 10);
	}
	
	public void updateString(String[] array, String value, int i){
		array[i] = value;
	}
	
	public String arrayToString(String[] array){
		String sb = "";
		for(int i=0; i < array.length; i++){
			sb+=array[i]+" ";
		}
		return sb;
	}
}
