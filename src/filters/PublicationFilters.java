package filters;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PublicationFilters extends VBox {
	
	private VBox publicationFiltersVBox;
	
	public PublicationFilters() {
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
		
		Label author = new Label("Author");
		TextField authorField = new TextField();
		grid.add(author, 0, 0);
		grid.add(authorField, 1, 0);
		
		Label name = new Label("Name");
		ComboBox<String> nameOptions = new ComboBox<String>();
		nameOptions.getItems().addAll("Name 1", "Name 2", "Name 3");
		grid.add(name, 0, 1);
		grid.add(nameOptions, 1, 1);
		
		this.publicationFiltersVBox.getChildren().add(grid);
	}
	
	public VBox getPublicationFiltersVBox() {
		return this.publicationFiltersVBox;
	}

}
