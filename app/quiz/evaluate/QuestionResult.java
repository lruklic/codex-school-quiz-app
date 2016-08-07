package quiz.evaluate;

import models.Question;
import models.enums.QuestionType;
import play.i18n.Messages;
import enums.AnswerType;

 /**
 * Key value structure for question and evaluated answer provided by player.
 * 
 * @author Luka Ruklic
 *
 */

public class QuestionResult {
	
	/**
	 * Question asked.
	 */
	public Question question;
	/**
	 * Answer given by user in text format. Default assumed answer is blank field.
	 */
	public String givenAnswer = "";
	/**
	 * Correct if user answered correctly, incorrect otherwise. // TODO partially correct
	 */
	public AnswerType isCorrect;
	
	/**
	 * Structured HTML string containing correct answer, answer given by user and answer explanation if one is provided.
	 */
	public String answerRecap;
	
	/**
	 * Constructor.
	 * 
	 * @param question given question
	 */
	public QuestionResult(Question question) {
		this.question = question;
	}

	/**
	 * Creates answer recap and saves it to answerRecap field with provided answer, correct answer and explanation if one is provided.
	 */
	public void createAnswerRecap() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html> ");
		sb.append("<div><b>"+Messages.get("question.text")+":</b> " + question.questionText + "</div>");		// TODO escaping!!
		sb.append("<br>");
		
		String correctAnswer;
		if (question.questionType == QuestionType.TRUE_FALSE) {
			givenAnswer = Messages.get("quiz."+givenAnswer);
			correctAnswer = Messages.get("quiz."+question.getQuestionAnswerText());
		} else {
			correctAnswer = question.getQuestionAnswerText();
		}
		
		sb.append("<div><b>"+Messages.get("answer.given")+":</b> " + givenAnswer + "</div>");
		sb.append("<br>");
		sb.append("<div><b>"+Messages.get("answer.correct")+":</b> " + correctAnswer + "</div>");
		sb.append("<br>");
		if (question.explanation.length() > 0) {
			sb.append("<div><b>"+Messages.get("answer.explanation")+":</b></div>");
			sb.append("<br>");
			sb.append("<div>" + question.explanation + "</div>");		// TODO escaping!!
		}
		sb.append("</html>");
		
		answerRecap = sb.toString();
	}

	public String getGivenAnswer() {
		return givenAnswer;
	}

	public void setGivenAnswer(String givenAnswer) {
		this.givenAnswer = givenAnswer;
	}

	/**
	 * Getter for answer type.
	 * 
	 * @return enum <code>CORRECT</code> of <code>AnswerType</code> if question was answered correctly, 
	 * <code>INCORRECT</code> otherwise or <code>NOT_ANSWERED</code> if no answer was provided
	 */
	public AnswerType isCorrect() {
		return isCorrect;
	}
	
}
