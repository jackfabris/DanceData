package filters;

import java.util.LinkedHashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * AlbumFilters is a VBox which is placed on the Search page. It contains
 * various filter options for conduction an album search, such as specifying
 * an artist name or year of production
 * @author Jack
 *
 */
public class AlbumFilters extends VBox {

	private GridPane grid;
	private VBox albumFiltersVBox;
	private LinkedHashMap<String, String> albumMap;

	/**
	 * Create the VBox which will contain the filters for an album search
	 */
	public AlbumFilters() {
		albumFiltersVBox = new VBox(10);
		albumFiltersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		albumMap = new LinkedHashMap<String, String>();
		grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);

		artist();
		year();

		this.albumFiltersVBox.getChildren().add(grid);
	}
	
	public void artist(){
		// Artist Name
		Label artist = new Label("Artist");
		final TextField artistField = new TextField();
		artistField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumMap.put("Artist", artistField.getText());
			}
		});
		grid.add(artist, 0, 0);
		grid.add(artistField, 1, 0);
	}

	public void year(){
		// Production Year
		Label year = new Label("Year");
		final TextField yearField = new TextField();
		yearField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				albumMap.put("Year", yearField.getText());
			}
		});
		grid.add(year, 0, 1);
		grid.add(yearField, 1, 1);
	}
	
	public LinkedHashMap<String, String> getAlbumMap() {
		return albumMap;
	}

	/**
	 * Get the album filters VBox
	 * @return albumFiltersVBox
	 */
	public VBox getAlbumFiltersVBox() {
		return this.albumFiltersVBox;
	}
}
