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
	
	private String barsperrepeat;
	private String name; 
	private String type;					
	private String artist_id;
	private String artist;
	private String devisor_id;
	private String repetitions; 
	private String publication;
	private CheckBox iHave;
	private String index;

	public Record(ResultSet set, String state) throws SQLException{
		//Dance
		if(state.equals("d")){
			barsperrepeat =  set.getString("barsperrepeat");
			name = set.getString("name");
			type = set.getString("type");	
			publication = set.getString("publication");
		}
		//Publication
		else if(state.equals("p")){
			name = set.getString("name");
			devisor_id = set.getString("devisor_id");
		}
		//Recording
		else if(state.equals("r")){
			name = set.getString("name");
			type = set.getString("type");
			repetitions = set.getString("repetitions");
			barsperrepeat = set.getString("barsperrepeat");
			artist = set.getString("artist");
		}
		//Album
		else{
			name = set.getString("name");
			artist = set.getString("artist");
		}
		iHave = new CheckBox();
		if(set.getString("ihave").equals("1")) {
			iHave.setSelected(true);
		}
		index = set.getString("tag");
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
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
	
	public String getArtist(){
		return artist;
	}
	
	public String getPublication() {
		return publication;
	}
}
