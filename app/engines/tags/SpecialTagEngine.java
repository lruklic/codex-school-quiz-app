package engines.tags;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import play.i18n.Messages;
import models.Question;
import forms.QuestionForm;

/**
 * Implements all methods for handling special tags in questions, converting them to and from String form.
 * 
 * @author Luka Ruklic
 *
 */

public class SpecialTagEngine {

	/**
	 * Competition abbreviation, in Croatian NATJ (short for 'Natjecanje').
	 */
	public static String COMPETITION_ABBR = "NATJ";	
	/**
	 * Nationwide leaving exam, in Croatian DM (short for 'Državna Matura').
	 */
	public static String LEAVING_EXAM_ABBR = "DM";
	
	/**
	 * Method that creates special tags from user inputs in HTML.
	 * 
	 * @param form form containing relevant fields
	 * @return specialTags String
	 */
	
	public static String createSpecialTagString(QuestionForm form) {
		StringBuilder sb = new StringBuilder();
		
		// TODO check if some value is not set
		if (form.competition != null && form.competition.equals("true")) {
			// Chosen question has previously appeared on competition
			sb.append(COMPETITION_ABBR+"-");
			sb.append(form.competitionLevel+"-");
			sb.append(form.competitionYear+";");
		}
		
		if (form.finalExam != null && form.finalExam.equals("true")) {
			sb.append(LEAVING_EXAM_ABBR+"-");
			sb.append(form.finalsYear+";");
			// summer or autumn exam?
		}
		
		return sb.toString();
	}
	
	public static void fillQuestionForm(Question question, QuestionForm form) {
		
		List<String> specialTags = new ArrayList<>();
		if (question.specialTags != null) {
			specialTags = Arrays.asList(question.specialTags.split(";"));
		}
		
		for (String tag : specialTags) {
			if (tag.startsWith(COMPETITION_ABBR)) {
				String[] tagParts = tag.split("-");
				form.competition = "true";
				form.competitionLevel = tagParts[1];
				form.competitionYear = tagParts[2];
			} else if (tag.startsWith(LEAVING_EXAM_ABBR)) {
				String[] tagParts = tag.split("-");
				form.finalExam = "true";
				form.finalsYear = tagParts[1];
			}
		}
		
	}
	
	/**
	 * Method that creates special tag from question special tag field. It primary usage is meant for question display in active quiz.  
	 * 
	 * @param specialTagString special tags field in question
	 * @return list of special tags
	 */
	
	public static List<SpecialTag> createSpecialTagsFromString(String specialTagString) {
		
		List<SpecialTag> specialTagsList = new ArrayList<>();
		
		String[] specialTags = specialTagString.split(";");
		
		for (String tag : specialTags) {
			if (tag.startsWith(COMPETITION_ABBR)) {
				String[] tagParts = tag.split("-");
				
				String displayName = Messages.get(tagParts[1]) + " " + Messages.get("specialTag.competition") + " " + tagParts[2];
				
				SpecialTag specialTag = new SpecialTag(SpecialTagType.LEAVING_EXAM, displayName, Color.GREEN, tagParts[1], tagParts[2], null);
				specialTagsList.add(specialTag);
				
			} else if (tag.startsWith(LEAVING_EXAM_ABBR)) {
				String[] tagParts = tag.split("-");
				
				String displayName = Messages.get("specialTag.leavingExam") + " " + tagParts[1];
				
				SpecialTag specialTag = new SpecialTag(SpecialTagType.COMPETITION, displayName, Color.BLUE, tagParts[1], null, null);
				specialTagsList.add(specialTag);
			}
		}
		
		return specialTagsList;
	}
	
	
	
	
}
