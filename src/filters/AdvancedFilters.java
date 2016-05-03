package filters;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AdvancedFilters {

	protected VBox filtersVBox;
	protected GridPane grid;
	protected LinkedHashMap<String, String> map;
	protected Database db;

	public AdvancedFilters() throws MalformedURLException, SQLException{
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		db = new Database();
		map = new LinkedHashMap<String, String>();
		setUpFilters();
	}

	public void setUpFilters(){
		filtersVBox = new VBox(10);
		filtersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		filtersVBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent arg0){
				if(arg0.getCode() == KeyCode.ENTER){
					//QUERY CALL
					System.out.println("QUERY CALL ENTER!");
				}
			}
		});
	}
	
	public void goButton(){
		Button goBtn = new Button("Advanced Search");
		goBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				//QUERY CALL
				callQuery();	
			}
		});
		this.filtersVBox.getChildren().add(goBtn);
	}
	
	public void buttonGrid(){
		this.filtersVBox.getChildren().add(grid);
		goButton();
	}
	
	public LinkedHashMap<String, String> getDanceMap() {
		return map;
	}
	
	public void callQuery(){
		System.out.println("QUERY CALL BUTTON!");
	}

	/**
	 * Get the dance filters VBox
	 * @return danceFiltersVBox
	 */
	public VBox getFiltersVBox() {
		return this.filtersVBox;
	}

}
