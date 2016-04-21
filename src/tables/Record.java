package tables;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.scene.control.CheckBox;

public class Record {
	
	private final String barsperrepeat;
	private final String name; 
	private final String type_id;					
	private final String artist_id;
	private final String devisor_id;
	private final String repetitions; 
	private CheckBox iHave;
	private String index;

	public Record(ResultSet set, String state, List<String> l) throws SQLException{
		if(state.equals("d")){
			barsperrepeat =  set.getString("barsperrepeat");
			name = set.getString("name");
			type_id = set.getString("type_id");	
			artist_id = "";
			devisor_id = "";
			repetitions = ""; 
		}
		else if(state.equals("p")){
			name = set.getString("name");
			devisor_id = set.getString("devisor_id");
			barsperrepeat = "";
			repetitions = ""; 
			type_id = "";
			artist_id = "";
		}
		else if(state.equals("r")){
			name = set.getString("name");
			type_id = set.getString("type_id");
			repetitions = set.getString("repetitions");
			barsperrepeat = set.getString("barsperrepeat");
			artist_id="";
			devisor_id = "";
		}
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
