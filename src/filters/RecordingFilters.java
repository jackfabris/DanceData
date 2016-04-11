package filters;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RecordingFilters extends VBox {
	
	private VBox recordingFiltersVBox;
	
	public RecordingFilters() {
		recordingFiltersVBox = new VBox(10);
		recordingFiltersVBox.setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		
		// Type
		Label type = new Label("Type");
		ComboBox<String> typeOptions = new ComboBox<String>();
		typeOptions.getItems().addAll("Type 1", "Type 2", "Type 3");
		grid.add(type, 0, 0);
		grid.add(typeOptions, 1, 0);
		
		// Medley Type
		Label medleyType = new Label("Medley Type");
		ComboBox<String> medleyTypeOptions = new ComboBox<String>();
		medleyTypeOptions.getItems().addAll("Medley 1", "Medley 2", "Medley 3");
		grid.add(medleyType, 0, 1);
		grid.add(medleyTypeOptions, 1, 1);
		
		// Repetitions
		Label repetitions = new Label("Repetitions");
		TextField repetitionsField = new TextField();
		grid.add(repetitions, 0, 2);
		grid.add(repetitionsField, 1, 2);
		
		// Bars
		Label bars = new Label("Bars");
		TextField barsField = new TextField();
		grid.add(bars, 0, 3);
		grid.add(barsField, 1, 3);
		
		this.recordingFiltersVBox.getChildren().add(grid);
	}
	
	public VBox getRecordingFiltersVBox() {
		return this.recordingFiltersVBox;
	}

}
