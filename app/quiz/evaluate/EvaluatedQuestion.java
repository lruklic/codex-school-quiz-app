package quiz.evaluate;

import quiz.evaluate.grade.QuestionScore;
import enums.AnswerType;

/**
 * Instance of question after it has been evaluated and graded.
 * 
 * @author Luka Ruklic
 *
 */

public class EvaluatedQuestion {

	/**
	 * Question result, containing question, given answer, correct answer and answer recap.
	 */
	public QuestionResult questionResult;
	/**
	 * Question score, containing maximum and scored points.
	 */
	public QuestionScore questionScore;

	/**
	 * Constructor for evaluated question.
	 * 
	 * @param questionResult question result
	 * @param questionScore question score
	 */
	public EvaluatedQuestion(QuestionResult questionResult, QuestionScore questionScore) {
		this.questionResult = questionResult;
		this.questionScore = questionScore;
	}
	
	/**
	 * Checks if the user playing the quiz has answered the question correctly.
	 *
	 * @return AnswerType enum defining is answer correct, partially correct, incorrect, or if the question is unanswered 
	 */
	public AnswerType isCorrect() {
		return questionResult.isCorrect();
	}
	
}
