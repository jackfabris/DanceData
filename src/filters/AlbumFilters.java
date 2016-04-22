package filters;

import javafx.scene.control.ComboBox;
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
	
	private VBox albumFiltersVBox;
	
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
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		
		// Artist Name
		Label artist = new Label("Artist");
		TextField artistField = new TextField();
		grid.add(artist, 0, 0);
		grid.add(artistField, 1, 0);
		
		// Production Year
		Label year = new Label("Year");
		TextField yearField = new TextField();
		grid.add(year, 0, 1);
		grid.add(yearField, 1, 1);
		
		this.albumFiltersVBox.getChildren().add(grid);
	}
	
	/**
	 * Get the album filters VBox
	 * @return albumFiltersVBox
	 */
	public VBox getAlbumFiltersVBox() {
		return this.albumFiltersVBox;
	}

}
