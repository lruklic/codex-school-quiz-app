package forms;

import java.util.List;

import play.data.validation.Constraints.Required;

/**
 * Form that is instatiated and populated when user chooses a quiz to partake.
 * 
 * @author Luka Ruklic
 *
 */

public class QuizForm {
	/**
	 * Grade which user chose.
	 */
	@Required
	public String grade;
	
	/**
	 * Subject which user chose.
	 */
	@Required
	public String subject;
	
	/**
	 * Chapter(s) of a subject that user chose.
	 */
	public List<String> chapters;
	
	/**
	 * Number of questions user wants in his quiz.
	 */
	public Integer numberOfQuestions;

}
