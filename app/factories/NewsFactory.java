package factories;

import forms.NoveltyForm;
import models.Admin;
import models.Novelty;

public class NewsFactory {

	public static Novelty createNews(NoveltyForm form, Admin admin) {
		
		Novelty novelty = new Novelty(escape(form.noveltyTitle), escape(form.noveltyText), form.newsType, form.newsPriority, admin);
		
		return novelty;
		
	}
	
	public static String escape(String text) {
		
		// TODO parse script tags, apply to both title and text
		
		String escapedText = text.replaceAll("\r\n", "<br>");
		return escapedText;
		
	}
	
}
