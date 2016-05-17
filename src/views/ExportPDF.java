package views;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import database.Database;


public class ExportPDF {
	String fileName;
	Database db;
	String state;
	String table;

	public ExportPDF(Database db, String fileName, String state, String table){
		this.fileName = fileName;
		this.db = db;
		this.state = state;
		this.table = table;
	}

	public void createPdf() {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			document.open();
			document.add(new Paragraph(writeString()));
			document.close();
			File f = new File(fileName);
			try {
				Desktop.getDesktop().browse(f.toURI());
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public String writeString() {
		String s = "";
		if(state.equals("a")) s += "My Albums\n\nName  -  Artist\n-------------------------\n";
		else if(state.equals("p")) s += "My Publications\n\nName  -  Devisor\n-------------------------\n";
		try {
			ResultSet results = db.searchTableByName(table, "", true);
			while(results.next()){
				if (state.equals("a")) s+= results.getString("name") +"  -  "+ results.getString("artist");
				else if (state.equals("p")) s+= results.getString("name") +"  -  "+ results.getString("devisor");
				s+="\n";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
}
