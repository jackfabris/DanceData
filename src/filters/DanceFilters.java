package filters;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DanceFilters extends VBox {
	
	private VBox danceFiltersVBox;
	
	public DanceFilters() {
		danceFiltersVBox = new VBox(10);
		danceFiltersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		
		// https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm#BABDBJBD
		// Make observable list using database query to get options?
		
		// Type
		Label type = new Label("Type");
		ComboBox<String> typeOptions = new ComboBox<String>();
		typeOptions.getItems().addAll("Type 1", "Type 2", "Type 3");
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
		
		// Bars
		Label bars = new Label("Bars");
		TextField barsField = new TextField();
		grid.add(bars, 0, 1);
		grid.add(barsField, 1, 1);
		
		// Couples
		Label couples = new Label("Couples");
		ComboBox<String> couplesOptions = new ComboBox<String>();
		couplesOptions.getItems().addAll("Couples 1", "Couples 2", "Couples 3");
		grid.add(couples, 0, 2);
		grid.add(couplesOptions, 1, 2);
		
		// Set Shape
		Label setShape = new Label("Set Shape");
		ComboBox<String> setShapeOptions = new ComboBox<String>();
		setShapeOptions.getItems().addAll("Shape 1", "Shape 2", "Shape 3");
		grid.add(setShape, 0, 3);
		grid.add(setShapeOptions, 1, 3);
		
		// Author
		Label author = new Label("Author");
		TextField authorField = new TextField();
		grid.add(author, 0, 4);
		grid.add(authorField, 1, 4);
		
		// Formations
		
		// Steps
		
		this.danceFiltersVBox.getChildren().add(grid);
	}
	
	public VBox getDanceFiltersVBox() {
		return this.danceFiltersVBox;
	}

}
