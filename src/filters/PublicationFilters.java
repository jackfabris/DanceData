package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		
		db = new Database();
		
		// Author
		Label author = new Label("Author");
		TextField authorField = new TextField();
		grid.add(author, 0, 0);
		grid.add(authorField, 1, 0);
		
		// Name
		ResultSet nameSet;
		nameSet = db.doQuery("SELECT name FROM publication");
		ObservableList<String> nameList = FXCollections.observableArrayList();
		while(nameSet.next()) {
			nameList.add(nameSet.getString(1));
		}
		Collections.sort(nameList);
		Label name = new Label("Name");
		ComboBox<String> nameOptions = new ComboBox<String>(nameList);
		grid.add(name, 0, 1);
		grid.add(nameOptions, 1, 1);
		
		this.publicationFiltersVBox.getChildren().add(grid);
	}
	
	/**
	 * Get the publication filters VBox
	 * @return publicationFiltersVBox
	 */
	public VBox getPublicationFiltersVBox() {
		return this.publicationFiltersVBox;
	}

}
