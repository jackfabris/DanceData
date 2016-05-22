package tables;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Database;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * LinkHandler implements EventHandler for a MouseEvent and handles linking between two CellInfos.
 * @author lisaketcham
 *
 */
public class LinkHandler implements EventHandler<MouseEvent>{
	
	private int linkId;
	private Database db;
	private CellInfo c;
	private String table;
	
	/**
	 * Constructor for a LinkHandler
	 * @param linkId - the id of the cellIno to link to
	 * @param db - the Database instane of this application
	 * @param c - the current CellInfo
	 * @param table - the String of the table to get entries from for the new linked CellInfo
	 */
	public LinkHandler(int linkId, Database db, CellInfo c, String table){
		this.linkId = linkId;
		this.db = db;
		this.c = c;
		this.table = table;
	}
	
	@Override
	/**
	 * On double click, sets the CellInfo to the entry given by the linkId
	 */
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
