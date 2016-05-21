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
	
	public void printMap(){
		//TESTING
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			System.out.println(x+", "+map.get(x));
		}
		System.out.println();
	}
	
	public void searchTitle(){
		map.put("title", "");
		Label title = new Label("Title");
		titleField = new TextField();
		titleField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
			}
		});
		titleField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue()) setTitleText(titleField.getText()); 
            }
        });
		grid.add(title, 0, gridY);
		grid.add(titleField, 1, gridY);
	}
	
	public abstract void setTitleText(String text);

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
			public void handle(MouseEvent e) {
				searchBool = true;
			}
		});
		filtersVBox.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				searchBool = false;
			}
		});
	}
	
	public void goAndClearButtons(){
		gridY++;
		goButton();
		clearButton();
		this.filtersVBox.getChildren().add(grid);
	}
	
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
	
	public abstract void clearButton();
	
	public LinkedHashMap<String, String> getDanceMap() {
		return map;
	}
	
	public abstract void callQuery();

	/**
	 * Get the dance filters VBox
	 * @return danceFiltersVBox
	 */
	public VBox getFiltersVBox() {
		return this.filtersVBox;
	}
	
	public boolean getSearchBool(){
		return searchBool;
	}
	
	public void setTitleField(String text){
		titleField.setText(text);
	}

}
