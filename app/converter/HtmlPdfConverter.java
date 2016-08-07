package converter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

public class HtmlPdfConverter {

//	public static ByteArrayOutputStream convertHtmlToPdf(List<String> scripts) {
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		
//		Document document = new Document();
//		
//		for (String script : scripts) {
//			
//		}
//		
//		return null;
//		
//	}
	
	public static void convertHtmlToPdf(ByteArrayOutputStream baos, Document document, String scriptContent) {
		
		PdfFont font = new PdfFont();
		
		org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(scriptContent, "UTF-8");
		
		Elements body = jsoupDoc.body().children();
		
		try {
			PdfWriter.getInstance(document, baos);
			document.open();
			
			for (Element element : body) {
				applyElementToPdf(document, font, element);
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		document.close();
		
	}
	
	public static void applyElementToPdf(Document doc, PdfFont font, Element element) {
		
		// Font is resetted to document default for each top level tag
		font.resetFont();
		
		com.lowagie.text.Element pdfElement = null;
		
		String topLevelTag = element.tag().toString();
		
		if (topLevelTag.equals("p") || topLevelTag.startsWith("h")) {
			
			Paragraph p = new Paragraph();
			
			if (topLevelTag.startsWith("h")) {
				font.setHeadingFont(topLevelTag);
			}
			
			p.setFont(font.getFont());
			
			if (element.toString().contains("&nbsp;")) {
				p.add(new Chunk(" "));
			} else {
				applyText(p, element, font.getFont());
			}
			
			applyTopLevelTagStyle(p, element.attr("style"));
			
			pdfElement = p;
			
		} else if (topLevelTag.endsWith("l")) { // ul, ol - check if that is enough
			
			ListDetails listDetails = new ListDetails();
			
			com.lowagie.text.List list = new com.lowagie.text.List(isOrdered(topLevelTag), 15);
			
			applyList(list, font, listDetails, element, topLevelTag);
			
			pdfElement = list;
		}
		
		try {
			doc.add(pdfElement);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void applyList(com.lowagie.text.List list, PdfFont font, ListDetails listDetails, Node node, String topLevelTag) {
		
		boolean isOrdered = isOrdered(topLevelTag);
		
		list.setIndentationLeft(listDetails.getListIndentation());
		
		if (!isOrdered) {
			list.setListSymbol(listDetails.getListSymbol());
		}
		
		for (Node n : node.childNodes()) {
			
			if (n instanceof Element && ((Element) n).tag().toString().endsWith("l")) {
				ListDetails newListDetails = new ListDetails(listDetails);
				newListDetails.increaseSymbol();
				
				com.lowagie.text.List newList = new com.lowagie.text.List(isOrdered(topLevelTag), 15);
				applyList(newList, font, newListDetails, n, topLevelTag);
				list.add(newList);
				
			} else if (!n.toString().trim().equals("")) {
				Paragraph p = new Paragraph();
				applyText(p, n, font.getFont()); // different indentations for nodes
				
				ListItem item = new ListItem(p);
				list.add(item);
			}

		}
		
	}
	
	private static void applyText(Phrase phrase, Node node, Font font) {
		
		Font currentFont = new Font(font);
		
		if (node instanceof TextNode) {
			phrase.add(new Phrase(node.toString(), currentFont));
			return;
		}
		
		List<Node> nodes = node.childNodes();
		
		for (Node n : nodes) {
			Tag tag = null;
			Attributes attr = null;
			if (n instanceof Element) {
				tag = ((Element) n).tag();
				attr = ((Element) n).attributes();
			}
			
			applyText(phrase, n, applyTagToFont(currentFont, tag, attr));
		}
		
	}
	
	private static Font applyTagToFont(Font font, Tag tag, Attributes attr) {
		
		Font newFont = new Font(font);
		int style = font.getStyle();
		
		if (tag != null) {
			if (tag.toString().equals("strong")) {
				style = style | Font.BOLD;
			} else if (tag.toString().equals("em")) {
				style = style | Font.ITALIC;
			} 
		}
		
		if (attr != null && attr.get("style") != null) {
			String styleAttr = attr.get("style");
			style = style | getStyleAttr(styleAttr);
		}

		newFont.setStyle(style);
		
		return newFont;
	}
	
	private static int getStyleAttr(String styleAttr) {
		String[] styleAttrArray = styleAttr.split(":");
		if (styleAttrArray[0].equals("text-decoration")) {
			// what if two different styles are present? rework ; problem maybe?
			if (styleAttrArray[1].replaceAll(";", "").trim().equals("underline")) {
				return Font.UNDERLINE;
			}
		}
		
		return 0;
	}
	
	private static void applyTopLevelTagStyle(Paragraph p, String styleAttr) {
		String[] styleAttrArray = styleAttr.split(":");
		if (styleAttrArray[0].equals("padding-left")) {
			String paddingAmount = styleAttrArray[1].replaceAll("[^\\d.]", "");
			p.setIndentationLeft(Integer.parseInt(paddingAmount));
		}
	}
	
	/**
	 * Method that checks if list in HTML is ordered.
	 * @param topLevelTag tag that defines HTML list; can be <ol> or <ul>
	 * @return true if list is <ol>, false otherwise
	 */
	private static boolean isOrdered(String topLevelTag) {
		if (topLevelTag.equals("ol")) {
			return com.lowagie.text.List.ORDERED;
		} else {	// under else is considered <ul> tag
			return com.lowagie.text.List.UNORDERED;
		}
	}
}
