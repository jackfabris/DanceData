package tables;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class CellInfo extends VBox{
	
	private TableView<Record> table;
	
	public CellInfo(double spacing, TableView<Record> table){
		super(spacing);
		this.table = table;
	}
	
	public void format(){
		getChildren().clear();
		setStyle(
				"-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 1;" +
				"-fx-border-insets: 5;" +
				"-fx-border-color: #cfcfcf;");
	}
	
	/**
	 * sets this class' cellInfo according to colName and id and 
	 * creates a button for toggling between table and cellInfo
	 * @param colName String column name to display
	 * @param id String id of the column to display
	 * @throws SQLException 
	 */
	public void set(ResultSet set) throws SQLException{
		format();

		Label text = new Label();
		ResultSetMetaData rsmd = set.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
			String columnValue = set.getString(i);
			text.setText(text.getText() + rsmd.getColumnName(i) + ": " + columnValue + "\n");
		}
		getChildren().add(text);
		returnButton();
	}

	public void returnButton(){
		//return to table
		Button tableReturn = new Button("BACK");
		getChildren().add(tableReturn);
		tableReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				table.setVisible(true);
				setVisible(false);
			}
		});
	}
}
