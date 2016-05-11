package views;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * The TextFormatter class is used to parse a .txt file which will be displayed
 * on the Home screen. It allows for the use of text formatting tags to make
 * formatting the text easier (rather than creating numerous Text nodes and
 * styling them manually. Tags should be placed around text.
 * [b][/b] - BOLD
 * @author Jack
 *
 */
public class TextFormatter {
	
	public TextFormatter() {
		
	}
	
	/**
	 * Given the path of a file, createTextFlow reads the text in the file
	 * and returns a TextFlow which contains Text nodes for the formatted
	 * text (which will be placed on the Home screen)
	 * @param path
	 * @return TextFlow
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public TextFlow createTextFlow(String path) throws IOException {
		FileReader inputStream = null;
		inputStream = new FileReader(path);
		
		ArrayList<Node> textArrayList = new ArrayList<Node>();
		String bufferText = "";
		
		int c;
		while((c = inputStream.read()) != -1) {
			bufferText += (char)c;
		}
		
		String tempText = "";
		Text text = new Text();
		text.setFont(Font.font(null, FontWeight.NORMAL, 13));
		for(int i=0; i<bufferText.length(); i++) {
			char ltr = bufferText.charAt(i);
			// is it potentially the start of a tag?
			if(ltr == '[') {
				// check to see if this is a START bold tag
				if(bufferText.charAt(i+1) == 'b' && bufferText.charAt(i+2) == ']') {
					// it is! Create a new Text with everything read so far
					text.setText(tempText);
					textArrayList.add(text);
					// now replace it with a blank one for formatted text
					text = new Text();
					text.setFont(Font.font(null, FontWeight.BOLD, 13));
					tempText = "";
					i += 2;
				}
				// check to see if this is an END bold tag
				if(bufferText.charAt(i+1) == '/' && bufferText.charAt(i+2) == 'b' 
						&& bufferText.charAt(i+3) == ']') {
					// it is! Add the formatted text to the array
					textArrayList.add(text);
					// create a new text (effectively wiping the current one)
					text = new Text();
					text.setFont(Font.font(null, FontWeight.NORMAL, 13));
					tempText = "";
					i += 3;
				}
			} // if it's just a regular character, then add it to the text
			else {
				tempText += bufferText.charAt(i);
				text.setText(tempText);
			}
		}
		text.setText(tempText);
		textArrayList.add(text);
		
		Node[] textArray = new Node[textArrayList.size()];
		textArray = textArrayList.toArray(textArray);
		TextFlow homeFlow = new TextFlow(textArray);
				
		return homeFlow;
	}
}