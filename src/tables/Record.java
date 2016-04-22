package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.CheckBox;

/**
 * 
 * Record holds the specific fields of one of the four types of Records depending on the given state.
 * The state will determine what type of record, either Dance, Publication, Recording, or Album.
 * The fields of this class must be all of the fields desired to be shown in the columns of the table,
 * even if the fields are shared by multiple tables. The names of the fields should be identical to the
 * fields of the schema in the table, must be final, and must be used, in this case, by a getter, whose 
 * name must also match the format getField_name() for any field field_name.
 * 
 */
public class Record {
	
	private final String barsperrepeat;
	private final String name; 
	private final String type_id;					
	private final String artist_id;
	private final String devisor_id;
	private final String repetitions; 
	private CheckBox iHave;
	private String index;

	public Record(ResultSet set, String state) throws SQLException{
		//Dance
		if(state.equals("d")){
			barsperrepeat =  set.getString("barsperrepeat");
			name = set.getString("name");
			type_id = set.getString("type_id");	
			artist_id = "";
			devisor_id = "";
			repetitions = ""; 
		}
		//Publication
		else if(state.equals("p")){
			name = set.getString("name");
			devisor_id = set.getString("devisor_id");
			barsperrepeat = "";
			repetitions = ""; 
			type_id = "";
			artist_id = "";
		}
		//Recording
		else if(state.equals("r")){
			name = set.getString("name");
			type_id = set.getString("type_id");
			repetitions = set.getString("repetitions");
			barsperrepeat = set.getString("barsperrepeat");
			artist_id="";
			devisor_id = "";
		}
		//Album
		else{
			name = set.getString("name");
			artist_id = set.getString("artist_id");
			devisor_id = "";
			repetitions = ""; 
			barsperrepeat = "";
			type_id = "";
		}
		iHave = new CheckBox();
		index = "";
	}

	public String getName() {
		return name;
	}

	public String getType_id() {
		return type_id;
	}

	public String getArtist_id() {
		return artist_id;
	}

	public String getDevisor_id() {
		return devisor_id;
	}

	public String getRepetitions() {
		return repetitions;
	}

	public String getBarsperrepeat() {
		return barsperrepeat;
	}
	
	public CheckBox getIHave() {
		return iHave;
	}

	public String getIndex() {
		return index;
	}
}
