package tables;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LinkHandler implements EventHandler<MouseEvent>{
	
	protected int linkId;
	protected Database db;
	protected CellInfo c;
	private String table;
	
	public LinkHandler(int linkId, Database db, CellInfo c, String table){
		this.linkId = linkId;
		this.db = db;
		this.c = c;
		this.table = table;
	}
	
	@Override
	public void handle(MouseEvent event) {
		if (event.getClickCount() > 1) {
			try {
				ResultSet set = db.getAllByIdFromTable(table, linkId);
				c.set(set, table);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
