package views;

import java.sql.SQLException;

import database.Database;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Collection {
	
	private VBox collectionVBox;
	private Database db;

	public Collection() throws SQLException{
		collectionVBox = new VBox(10);
		db = new Database();
		addLabel();
	}
	
	public void addLabel(){
		Label l = new Label();
		l.setText("Collection Page");
		this.collectionVBox.getChildren().add(l);
	}

	public VBox getCollectionVBox() {
		return collectionVBox;
	}
}
