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
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import database.Database;

/**
 * This class contains the methods necessary to create a printable PDF of the
 * user's collection of albums or publications.
 * @author Jack
 *
 */
public class ExportPDF {
	private String fileName;
	private Database db;
	private String state;
	private String table;

	/**
	 * Constructor for ExportPDF with the proper information
	 * @param db - the database instance for this application
	 * @param fileName - the name of the file to export the list to
	 * @param state - the state to indicate which list to export
	 * @param table - the table of the list to export
	 */
	public ExportPDF(Database db, String fileName, String state, String table){
		this.fileName = fileName;
		this.db = db;
		this.state = state;
		this.table = table;
	}

	/**
	 * Create the PDF file and save it as "My Albums" or "My Collections" 
	 * depending on which one is being saved.
	 */
	public void createPdf() {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileName));
			Font header = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
			document.open();
			// set the title on the page as "My Albums"
			if (state.equals("a")) {
				Paragraph aHead = new Paragraph();
				aHead.setFont(header);
				aHead.add("My Albums\n\n");
				document.add(aHead);
			}
			// or set the title on the page as "My Publications"
			else if (state.equals("p")) {
				Paragraph pHead = new Paragraph();
				pHead.setFont(header);
				pHead.add("My Publications\n\n");
				document.add(pHead);
			}
			// create the table of albums/publications
			document.add(createTable());
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
	
	/**
	 * This method creates the table on the PDF which will contain either the
	 * name and artist for an album or the name and devisor for a publication.
	 * @return PdfPTable
	 */
	public PdfPTable createTable() {
		PdfPTable t = new PdfPTable(2);
		t.setWidthPercentage(100);
		t.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		// set up the table headers for albums
		if (state.equals("a")) {
			Font bold = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
			
			PdfPCell nameCell = new PdfPCell();
			nameCell.setBorder(Rectangle.NO_BORDER);
			Phrase name = new Phrase("Name", bold);
			nameCell.addElement(name);
			
			PdfPCell artistCell = new PdfPCell();
			artistCell.setBorder(Rectangle.NO_BORDER);
			Phrase artist = new Phrase("Artist", bold);
			artistCell.addElement(artist);
			
			t.addCell(nameCell);
			t.addCell(artistCell);
		}
		// set up the table headers for publications
		else if (state.equals("p")) {
			Font bold = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
			
			PdfPCell nameCell = new PdfPCell();
			nameCell.setBorder(Rectangle.NO_BORDER);
			Phrase name = new Phrase("Name", bold);
			nameCell.addElement(name);
			
			PdfPCell devisorCell = new PdfPCell();
			devisorCell.setBorder(Rectangle.NO_BORDER);
			Phrase devisor = new Phrase("Devisor", bold);
			devisorCell.addElement(devisor);
			
			t.addCell(nameCell);
			t.addCell(devisorCell);
		}
		try {
			ResultSet results = db.searchTableByName(table, "", true);
			while(results.next()) {
				// add album info to table
				if (state.equals("a")) {
					t.addCell(results.getString("name"));
					t.addCell(results.getString("artist"));
				}
				// add publication info to table
				else if (state.equals("p")) {
					t.addCell(results.getString("name"));
					t.addCell(results.getString("devisor"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
}
