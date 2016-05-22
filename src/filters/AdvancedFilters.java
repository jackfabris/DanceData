package filters;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import views.SearchDataView;


/**
 * Advanced Filters is the abstract superclass containing shared information and behaviors from 
 * Album Filters, Dance Filters, Publication Filters, and Recording Filters
 * @author lisaketcham
 *
 */
public abstract class AdvancedFilters {

	protected VBox filtersVBox;
	protected GridPane grid;
	protected LinkedHashMap<String, String> map;
	protected Database db;
	protected boolean searchBool;
	protected SearchDataView SearchCollection;
	protected String table;
	protected int gridY;
	protected TextField titleField;

	/**
	 * Constructor for AdvancedFilters initializes variables
	 * @param db - Database instance of this application
	 * @param sc - Search or Collection View the filters will be in
	 * @param table - name of the database table of the given state
	 * @throws MalformedURLException
	 * @throws SQLException
	 */
	public AdvancedFilters(Database db, SearchDataView sc, String table) throws MalformedURLException, SQLException{
		this.SearchCollection = sc;
		this.table = table;
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		gridY = 0;
		this.db = db;
		map = new LinkedHashMap<String, String>();
		setUpFilters();
		searchBool = false;
		searchTitle();
	}
	
	//TODO: REMOVE
	public void printMap(){
		//TESTING
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			System.out.println(x+", "+map.get(x));
		}
		System.out.println();
	}
	
	/**
	 * Sets up the Title Field in the Advanced Search
	 */
	public void searchTitle(){
		Label title = new Label("Title");
		titleField = new TextField();
		//searches on ENTER
		titleField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setTitleText(titleField.getText()); 
				callQuery();
			}
		});
		//commits on Leave
		titleField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue()) setTitleText(titleField.getText()); 
            }
        });
		grid.add(title, 0, gridY);
		grid.add(titleField, 1, gridY);
	}
	
	/**
	 * updates the corresponding title of the Search Bar in SearchDataView
	 * @param text - the text to set it to
	 */
	public abstract void setTitleText(String text);

	/**
	 * Formats the VBox and sets up handle key and mouse events to search when
	 * ENTER is pressed or the mouse is within the VBox
	 */
	public void setUpFilters(){
		filtersVBox = new VBox(10);
		filtersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		//ENTER KEY
		filtersVBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					callQuery();
				}
			}
		});
		//MOUSE HOVER
		filtersVBox.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) { searchBool = true;}
		});
		filtersVBox.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {searchBool = false;}
		});
	}
	
	/**
	 * Add the Go and Clear Buttons to the Grid
	 */
	public void goAndClearButtons(){
		gridY++;
		goButton();
		clearButton();
		this.filtersVBox.getChildren().add(grid);
	}
	
	/**
	 * Set up the Go Button
	 */
	public void goButton(){
		Button goBtn = new Button("Submit Search");
		goBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				callQuery();	
			}
		});
		grid.add(goBtn, 0, gridY);
	}
	
	/**
	 * Set up the clear button depending on the state and filter
	 */
	public void clearButton(){
		Button clearBtn = new Button("Clear Fields and Reset");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				clear();
			}
		});
		grid.add(clearBtn, 1, gridY);
	}
	
	/**
	 * Clear the fields, clear the map, reset the titles, reset the table
	 */
	public abstract void clear();
	
	/**
	 * Return the map of fields to their values
	 * @return the map of fields to their values
	 */
	public LinkedHashMap<String, String> getMap() {
		return map;
	}
	
	/**
	 * Calls the query to populate the table based on the Advanced Search
	 */
	public abstract void callQuery();

	/**
	 * Get the specific filters VBox
	 * @return filtersVBox
	 */
	public VBox getFiltersVBox() {
		return this.filtersVBox;
	}
	
	/**
	 * Get the searchBool
	 * @return true if the mouse is in the VBox, false otherwise
	 */
	public boolean getSearchBool(){
		return searchBool;
	}
	
	/**
	 * Set the Advanced Search's Title Field
	 * @param text - the text to set it to
	 */
	public void setTitleField(String text){
		titleField.setText(text);
	}
	
	/**
	 * Checks if a given string is numeric
	 * @param str
	 * @return true if it is numeric, false otherwise
	 */
	public boolean isNumeric(String str)  
	{  
	  try  {  
	    @SuppressWarnings("unused")
		int i = Integer.parseInt(str);  
	  } catch(NumberFormatException nfe)  {  
	    return false;  
	  }  
	  return true;  
	}
}
