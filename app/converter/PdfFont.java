package converter;

import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class PdfFont {
	
	private BaseFont baseFont;
	
	private Font font;
	
	/**
	 * Constructor for font. Default values are Ariel Universal and Cp1250 encoding.
	 */
	public PdfFont() {
		
		try {
			this.baseFont = BaseFont.createFont("fonts/arialuni.ttf", "Cp1250", BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e1) {
			e1.printStackTrace();
		}
		
		this.font = new Font(this.baseFont);
	}

	public Font getFont() {
		return font;
	}
	
	public void resetFont() {
		this.font.setSize(12);
		this.font.setStyle(Font.NORMAL);
	}
	
	public void setHeadingFont(String heading) {
		switch(heading) {
		case "h1": 
			this.font.setSize(32);
			break;
		case "h2":
			this.font.setSize(24);
			break;
		case "h3": 
			this.font.setSize(19);
			break;
		case "h4":
			this.font.setSize(16);
			break;
		case "h5": 
			this.font.setSize(14);
			break;
		case "h6":
			this.font.setSize(13);
			break;
		}
	}
	
	public void setStyle() {
		this.font.setStyle(Font.BOLD | Font.ITALIC);
	}
	
}
