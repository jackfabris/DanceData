package tables;

import java.sql.SQLException;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class CellTagHandler implements EventHandler<ActionEvent> {
	
	private Database db;
	private TextField tag;
	private String type;
	private int id;
	private RecordTable rt;
	
	public CellTagHandler(Database db, TextField tag, String type, int id, RecordTable rt){
		this.db = db;
		this.tag = tag;
		this.type = type;
		this.id = id;
		this.rt = rt;
	}
	
	@Override
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
