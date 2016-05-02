package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * PublicationFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conducting a recording search, such as author
 * and name.
 * @author Jack
 *
 */
public class PublicationFilters extends VBox {
	
	private VBox publicationFiltersVBox;
	private Database db;
	private LinkedHashMap<String, String> publMap;
	private GridPane grid;
	
	/**
	 * Create the VBox which will contain the filters for a publication search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public PublicationFilters() throws SQLException, MalformedURLException {
		publicationFiltersVBox = new VBox(10);
		publicationFiltersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		publMap = new LinkedHashMap<String, String>();
		db = new Database();
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
	
		author();
		name();
		
		this.publicationFiltersVBox.getChildren().add(grid);
	}
	
	public void author(){
		Label author = new Label("Author");
		final TextField authorField = new TextField();
		authorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				publMap.put("Author", authorField.getText());
			}
		});
		grid.add(author, 0, 0);
		grid.add(authorField, 1, 0);
	}
	
	public void name() throws SQLException{
		ResultSet nameSet;
		nameSet = db.doQuery("SELECT name FROM publication");
		ObservableList<String> nameList = FXCollections.observableArrayList("");
		while(nameSet.next()) {
			nameList.add(nameSet.getString(1));
		}
		Collections.sort(nameList);
		Label name = new Label("Name");
		final ComboBox<String> nameOptions = new ComboBox<String>(nameList);
		nameOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				publMap.put("Name", nameOptions.getValue());
			}
		});
		grid.add(name, 0, 1);
		grid.add(nameOptions, 1, 1);
	}
	
	public LinkedHashMap<String, String> getPublMap() {
		return publMap;
	}

	/**
	 * Get the publication filters VBox
	 * @return publicationFiltersVBox
	 */
	public VBox getPublicationFiltersVBox() {
		return this.publicationFiltersVBox;
	}

}
