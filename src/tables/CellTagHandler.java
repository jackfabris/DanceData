package tables;

import java.sql.SQLException;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * CellTagHandler is an EventHandler that handles Adding or Removing Tags from inside a CellInfo
 * @author lisaketcham
 *
 */
public class CellTagHandler implements EventHandler<ActionEvent> {
	
	private Database db;
	private TextField tag;
	private String type;
	private int id;
	private RecordTable rt;
	
	/**
	 * Constructor for a CellTagHandler
	 * @param db - the Database instance for this application
	 * @param tag - the TextField of the Tag in the CellInfo
	 * @param type - the type of CellInfo corresponding to the appropriate table
	 * @param id - id of the entry that is being tagged
	 * @param rt - the appropriate RecordTable that has this entry as a row in its TableView
	 */
	public CellTagHandler(Database db, TextField tag, String type, int id, RecordTable rt){
		this.db = db;
		this.tag = tag;
		this.type = type;
		this.id = id;
		this.rt = rt;
	}
	
	@Override
	/**
	 * add or remove tag of given entry and refresh the table
	 */
	public void handle(ActionEvent arg0) {
		try {
			if(!tag.getText().equals("")) db.addTag(type, id, tag.getText());
			else db.removeTag(type, id);
			rt.refresh(type);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
