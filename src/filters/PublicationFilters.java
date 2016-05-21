package filters;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	
	private TextField authorField;
	private ComboBox<String> nameOptions;
	private CheckBox RSCDSCB;

	/**
	 * Create the VBox which will contain the filters for a publication search
	 * @throws SQLException 
	 * @throws MalformedURLException 
	 */
	public PublicationFilters(Database db, SearchDataView sc) throws SQLException, MalformedURLException {
		super(db, sc, "publication");
		author();
		name();
		RSCDS();
		super.goAndClearButtons();
	}

	@Override
	public void setTitleText(String text){
		SearchCollection.setPublicationTitle(text);
		SearchCollection.getSearch().setText(text);
	}
	
	public void author(){
		map.put("author", "");
		Label author = new Label("Author");
		authorField = new TextField();
		authorField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("Devisor", authorField.getText());
				callQuery();
			}
		});
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
		nameOptions = new ComboBox<String>(nameList);
		nameOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				map.put("name", nameOptions.getValue());
			}
		});
		gridY++;
		grid.add(name, 0, gridY);
		grid.add(nameOptions, 1, gridY);
	}

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
	public void callQuery(){
		try {
			ResultSet data = db.advancedTableSearch("publication", titleField.getText(), map, SearchCollection.isCollection());
			//ResultSet data = db.searchTableByName("publication", "dog", SearchCollection.isCollection());
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
	public void clearButton(){
		Button clearBtn = new Button("Clear Fields");
		clearBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				titleField.clear();
				authorField.clear();
				nameOptions.setValue("");
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
		});
		grid.add(clearBtn, 1, gridY);
	}
}
