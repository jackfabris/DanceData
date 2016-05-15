package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tables.RecordTable;
import views.SearchDataView;

/**
 * PublicationFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conducting a recording search, such as author
 * and name.
 * @author Jack
 *
 */
public class PublicationFilters extends AdvancedFilters {
	
	/**
	 * Create the VBox which will contain the filters for a publication search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public PublicationFilters(SearchDataView sc) throws SQLException, MalformedURLException {
		super(sc, "publication");
		author();
		name();
		buttonGrid();
	}
	
	public void author(){
		map.put("author", ""); //artist_id?
		Label author = new Label("Author");
		final TextField authorField = new TextField();
		authorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Author", authorField.getText());
				callQuery();
			}
		});
       authorField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue.booleanValue())
                	map.put("author", authorField.getText());
            }
        });
		grid.add(author, 0, 0);
		grid.add(authorField, 1, 0);
	}
	
	public void name() throws SQLException{
		map.put("name", "");
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
				map.put("name", nameOptions.getValue());
			}
		});
		grid.add(name, 0, 1);
		grid.add(nameOptions, 1, 1);
	}
	
	@Override
	public void callQuery(){
		try {
			//Result set data = db.advancedTableSearch("publication", SearchCollection.getPublicationTitle(), map, SearchCollection.isCollection());
			ResultSet data = db.searchTableByName("publication", "dog", SearchCollection.isCollection());
			RecordTable publicationTable = SearchCollection.getPublicationTable();
			publicationTable.setTableData(publicationTable.populate(data));
			publicationTable.getCellInfo().setVisible(false);
			publicationTable.getCellInfo().setVis(false);
			publicationTable.getTable().setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
