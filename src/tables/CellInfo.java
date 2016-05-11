package tables;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CellInfo extends VBox{

	private TableView<Record> table;
	private String type;
	private ResultSet set; 
	private GridPane grid;
	private int gridY, gridX, id, linkId;
	private Database db;

	public CellInfo(double spacing, TableView<Record> table) throws MalformedURLException, SQLException{
		super(spacing);
		grid = new GridPane();
		grid.setHgap(15);
		grid.setVgap(5);
		this.table = table;
		db = new Database();
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
	public void set(ResultSet set, String type) throws SQLException{
		format();
		this.set = set;
		id = Integer.parseInt(set.getString("id"));
		this.type = type;
		gridY = 0;
		gridX = 0;
		grid.getChildren().clear();
		Label title = new Label(type.toUpperCase()+":");
		title.setStyle("-fx-text-fill: #445878;"+
						"-fx-font-weight: bold;"+
				"-fx-font-size: 18");
		grid.add(title, 0, 0);
		gridY++;
		setSpecificCellInfo();
		getChildren().add(grid);
		returnButton();
	}

	public void setSpecificCellInfo() throws SQLException{
		if(type.equals("album")) albumCellInfo();
		else if(type.equals("dance")) danceCellInfo();
		else if(type.equals("person")) personCellInfo();
		else if(type.equals("publication")) publicationCellInfo();
		else if(type.equals("recording")) recordingCellInfo();
		else tuneCellInfo();
	}
	
	public void iterateInfo(LinkedHashMap<String, String> cellInfo) throws SQLException{
		gridY=3;
		Iterator<String> i = cellInfo.keySet().iterator();
		while(i.hasNext()){
			String col = i.next();
			String info = set.getString(cellInfo.get(col));
			Label titleCol = new Label(col);
			Label infoCol = new Label(info);
			//Person Link
			if(info != null && isPerson(cellInfo.get(col))){
				linkId = Integer.parseInt(info);
				personLink(infoCol, db.getPerson(linkId));
			}
			//1/0 to Yes/No
			if(info != null && isYesOrNo(cellInfo.get(col))){
				if (info.equals("1")) infoCol.setText("Yes");
				else infoCol.setText("No");
			}
			grid.add(titleCol, 0, gridY++);
			grid.add(infoCol, 1, gridY-1);
		}
	}

	public void iterateLists(String colName, String linkType, ResultSet list) throws SQLException{
		gridY=3;
		gridX+=2;
		Label col = new Label(colName);
		grid.add(col, gridX, gridY++);
		gridY--;
		boolean empty = true;
		while(list.next()){
			empty = false;
			Label infoCol = new Label(list.getString("name"));
			linkId = Integer.parseInt(list.getString("id"));
			if(!linkType.equals("")) link(infoCol, linkType);
			grid.add(infoCol, gridX+1, gridY++);
		}
		if(empty){
			Label infoCol = new Label("None");
			infoCol.setStyle("-fx-text-fill: #b8b8bf;");
			grid.add(infoCol, gridX+1, gridY++);
		}
	}

	public void link(Label infoCol, String linkType){
		infoCol.setStyle("-fx-text-fill: blue;");
		infoCol.setUnderline(true);
		infoCol.addEventHandler(MouseEvent.MOUSE_CLICKED, new LinkHandler(linkId, db, this, linkType));
	}

	private void albumCellInfo() throws SQLException {
		LinkedHashMap<String, String> albumInfo = new LinkedHashMap<String, String>();
		albumInfo.put("Name: ", "name");
		//albumInfo.put("Artist: ", "artist_id");	
		albumInfo.put("Year: ", "productionyear");
		albumInfo.put("Available: ", "isavailable");
		iHaveAndTag();
		iterateInfo(albumInfo);
		
		Label titleCol = new Label("Medium: ");
		String medium = "";
		
		boolean cd = set.getString("oncd").equals("1");
		boolean mc = set.getString("onmc").equals("1");
		boolean lp = set.getString("onlp").equals("1");
		
		if(cd) medium+="CD ";
		else if(mc) medium+="MC ";
		else if(lp) medium+="LP ";
		
		Label infoCol = new Label(medium);
		grid.add(titleCol, 0, gridY++);
		grid.add(infoCol, 1, gridY-1);

		iterateLists("Recordings: ", "recording", db.getRecordingsByAlbum(id));
	}
	
	private void danceCellInfo() throws SQLException {
		LinkedHashMap<String, String> danceInfo = new LinkedHashMap<String, String>();
		danceInfo.put("Name: ", "name");
		danceInfo.put("Devised By: ", "devisor_id");
		iHaveAndTag();
		iterateInfo(danceInfo);
		iterateLists("Formations: ", "", db.getFormationsByDance(id));
		iterateLists("Steps: ", "", db.getStepsByDance(id));
		iterateLists("Tunes: ", "tune", db.getTunesByDance(id));
		iterateLists("Recordings: ", "recording", db.getRecordingsByDance(id));
	}

	private void personCellInfo() throws SQLException {
		LinkedHashMap<String, String> personInfo = new LinkedHashMap<String, String>();
		personInfo.put("Name: ", "name");
		iterateInfo(personInfo);
		
		Label titleCol = new Label("Type: ");
		String personType = "";
		
		boolean dev = set.getString("isdev").equals("1");
		boolean pub = set.getString("ispub").equals("1");
		boolean cmp = set.getString("iscmp").equals("1");
		boolean mus = set.getString("ismus").equals("1");
		
		if(dev) personType+="Devisor ";
		else if(pub) personType+="Publisher ";
		else if(cmp) personType+="Composer ";
		else if(mus) personType+="Musician ";
		
		Label infoCol = new Label(personType);
		
		grid.add(titleCol, 0, gridY++);
		grid.add(infoCol, 1, gridY-1);
		
		iterateLists("Dances: ", "dance", db.getDancesByPerson(id));
		iterateLists("Publications: ", "publication", db.getPublicationsByPerson(id));
		iterateLists("Tunes: ", "tune", db.getTunesByPerson(id));
		iterateLists("Recordings: ", "recording", db.getRecordingsByPerson(id));
		iterateLists("Albums: ", "album", db.getAlbumsByPerson(id));
	}
	
	public void personLink(Label infoCol, ResultSet name) throws SQLException{
		infoCol.setText(name.getString("name"));
		link(infoCol, "person");
	}
	
	public boolean isPerson(String person){
		return person.equals("devisor_id") || person.equals("artist_id") || person.equals("composer_id");
	}
	
	private void publicationCellInfo() throws SQLException {
		LinkedHashMap<String, String> publicationInfo = new LinkedHashMap<String, String>();
		publicationInfo.put("Name: ", "name");
		publicationInfo.put("Publisher: ", "devisor_id");
		publicationInfo.put("Has Dances: ", "hasdances");
		publicationInfo.put("Has Tunes: ", "hastunes");
		publicationInfo.put("On Paper: ", "onpaper");
		iHaveAndTag();
		iterateInfo(publicationInfo);
		iterateLists("Dances: ", "dance", db.getDancesByPublication(id));
		iterateLists("Tunes: ", "tune", db.getTunesByPublication(id));
	}

	private void recordingCellInfo() throws SQLException {
		LinkedHashMap<String, String> recordingInfo = new LinkedHashMap<String, String>();
		recordingInfo.put("Name: ", "name");
		recordingInfo.put("Artist: ", "artist_id");
		iHaveAndTag();
		iterateInfo(recordingInfo);
		iterateLists("Album: ", "album", db.getAlbumByRecording(id));
		iterateLists("Tunes: ", "tune", db.getTunesByRecording(id));
	}

	private void tuneCellInfo() throws SQLException {
		LinkedHashMap<String, String> tuneInfo = new LinkedHashMap<String, String>();
		tuneInfo.put("Name: ", "name");
		tuneInfo.put("Composer: ", "composer_id");		
		iterateInfo(tuneInfo);
		iterateLists("Dances: ", "dance", db.getDancesByTune(id));
		iterateLists("Recordings: ", "recording", db.getRecordingsByTune(id));
	}
	
	public boolean isYesOrNo(String field){
		return field.equals("isavailable") || field.equals("hasdances") || field.equals("hastunes") || field.equals("onpaper");
	}
	
	public void iHaveAndTag() throws SQLException{
		//I HAVE
		Label iHave = new Label("I Have: ");
		CheckBox cb = new CheckBox();
		if(set.getString("ihave").equals("1")) {
			cb.setSelected(true);
		}
		cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,Boolean old_val, Boolean new_val) {
				try {
					if(!old_val && new_val) db.iHave(type, id);
					else if(old_val && !new_val) db.iDontHave(type, id);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		grid.add(iHave, 0, gridY++);
		grid.add(cb, 1, gridY-1);
		
		//TAG
		Label tagCol = new Label("Tag: ");
		final TextField tag = new TextField();
		if(set.getString("tag") == null) tag.setText("");
		else tag.setText(set.getString("tag"));
		tag.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				int id;
				try {
					id = Integer.parseInt(set.getString("id"));
					if(!tag.getText().equals("")) db.addTag(type, id, tag.getText());
					else db.removeTag(type, id);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		grid.add(tagCol, 0, gridY++);
		grid.add(tag, 1, gridY-1);
	}

	public void returnButton(){
		//return to table
		Button tableReturn = new Button("BACK TO RESULTS");
		getChildren().add(tableReturn);
		tableReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				table.setVisible(true);
				setVisible(false);
			}
		});
	}

	public void setType(String newType){
		this.type = newType;
	}
}
