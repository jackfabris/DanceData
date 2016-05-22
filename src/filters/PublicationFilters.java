package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
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

	private TextField authorField;
	private CheckBox RSCDSCB;

	/**
	 * Create the VBox which will contain the filters for a publication search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public PublicationFilters(Database db, SearchDataView sc) throws SQLException, MalformedURLException {
		super(db, sc, "publication");
		author();
		RSCDS();
		super.goAndClearButtons();
	}

	@Override
	/**
	 * Sets the text of the Publication Title and Search Bar
	 */
	public void setTitleText(String text){
		SearchCollection.setPublicationTitle(text);
		SearchCollection.getSearch().setText(text);
	}

	/**
	 * Sets up the Author (devisor) options for Publication
	 */
	public void author(){
		map.put("author", "");
		Label author = new Label("Author");
		authorField = new TextField();
		//Search on ENTER
		authorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Devisor", authorField.getText());
				callQuery();
			}
		});
		//Commit on Leave
		authorField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue.booleanValue())
					map.put("author", authorField.getText());
			}
		});
		gridY++;
		grid.add(author, 0, gridY);
		grid.add(authorField, 1, gridY);
	}

	/**
	 * Sets up the RSCDS options for Publication
	 */
	public void RSCDS(){
		map.put("RSCDS", "0");
		Label RSCDS = new Label("Only RSCDS Publications ");
		RSCDSCB = new CheckBox();
		RSCDSCB.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(RSCDSCB.isSelected()) map.put("RSCDS", "1");
				else if(!RSCDSCB.isSelected()) map.put("RSCDS", "0");
			}
		});
		gridY++;
		grid.add(RSCDS, 0, gridY);
		grid.add(RSCDSCB, 1, gridY);
	}

	@Override
	/**
	 * Call the Advance Search query, populate the table, set visibility so that the 
	 * Publication table is showing and everything else is hidden
	 */
	public void callQuery(){
		try {
			ResultSet data = db.advancedTableSearch("publication", titleField.getText(), map, SearchCollection.isCollection());
			RecordTable publicationTable = SearchCollection.getPublicationTable();
			publicationTable.setTableData(publicationTable.populate(data));
			publicationTable.getCellInfo().setVisible(false);
			publicationTable.getCellInfo().setVis(false);
			publicationTable.getTable().setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * Clear the fields, clear the map, reset the titles, reset the table
	 */
	public void clear(){
		//clear fields
		titleField.clear();
		authorField.clear();
		RSCDSCB.setSelected(false);

		SearchCollection.setPublicationTitle("");
		SearchCollection.getSearch().clear();
		SearchCollection.getSearch().setPromptText("Search by Publication Title");

		//clear map
		Iterator<String> i = map.keySet().iterator();
		while(i.hasNext()){
			String x = i.next();
			map.put(x, "");
		}

		//reset table
		ResultSet data;
		try {
			data = db.searchTableByName("publication", "", SearchCollection.isCollection());
			RecordTable publicationTable = SearchCollection.getPublicationTable();
			publicationTable.setTableData(publicationTable.populate(data));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
