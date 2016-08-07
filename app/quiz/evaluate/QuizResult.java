package quiz.evaluate;

import java.util.ArrayList;
import java.util.List;

import enums.AnswerType;

/**
 * Class that contains details about quiz that was recently completed.
 * 
 * @author Luka Ruklic
 *
 */

public class QuizResult {
	
	/**
	 * List of evaluated question after the user has completed the quiz.
	 */
	private List<EvaluatedQuestion> evaluatedQuestions = new ArrayList<>();
	
	/**
	 * Empty constructor.
	 */
	public QuizResult() {	
	}
	/**
	 * Getter for evaluated questions.
	 * @return list of evaluated questions
	 */
	public List<EvaluatedQuestion> getEvaluatedQuestions() {
		return evaluatedQuestions;
	}
	/**
	 * Add evaluted question to quiz result.
	 * @param evaluatedQuestion evaluated question consisting of quiz result and quiz score
	 */
	public void addEvaluatedQuestion(EvaluatedQuestion evaluatedQuestion) {
		evaluatedQuestions.add(evaluatedQuestion);
	}
	/**
	 * Get number of evaluated questions.
	 * @return
	 */
	public int getNumberOfQuestions() {
		return evaluatedQuestions.size();
	}
	/**
	 * Get number of correct answers provided by user.
	 * @return number of correct answers
	 */
	public int getNumberOfCorrectAnswers() {
		return countAnswers(AnswerType.CORRECT);
	}
	/**
	 * Get number of incorrect answers provided by user.
	 * @return number of incorrect answers
	 */
	public int getNumberOfIncorrectAnswers() {
		return countAnswers(AnswerType.INCORRECT);
	}
	/**
	 * Get number of unanswered questions.
	 * @return number of unanswered questions
	 */
	public int getNumberOfUnansweredQuestions() {
		return countAnswers(AnswerType.NOT_ANSWERED);
	}
	/**
	 * Get maximal score user can achieve on this quiz.  
	 * @return maximal score automatically converted to most fitting number format
	 */
	public Number getQuizMaxScore() {
		Double maxScore = 0.0;
		for (EvaluatedQuestion eq : evaluatedQuestions) {
			maxScore += eq.questionScore.maxPoints;
		}
		
		if ((maxScore == Math.floor(maxScore))) {
		    return maxScore.intValue();
		} else {
			return maxScore;
		}
		
	}
	/**
	 * Get quiz score achieved by user.
	 * @return quiz score automatically converted to most fitting number format
	 */
	public Number getQuizScore() {
		Double quizScore = 0.0;
		for (EvaluatedQuestion eq : evaluatedQuestions) {
			quizScore += eq.questionScore.pointsScored;
		}
		
		if ((quizScore == Math.floor(quizScore))) {
		    return quizScore.intValue();
		} else {
			return quizScore;
		}
	}
	/**
	 * Count number of answers of certain type (correct, incorrect, unanswered).
	 * @param answerType type of answer
	 * @return number of answers of certain type
	 */
	private int countAnswers(AnswerType answerType) {
		int counter = 0;	
		for (EvaluatedQuestion evaluatedQuestion : evaluatedQuestions) {
			if (evaluatedQuestion.isCorrect().equals(answerType)) {
				counter++;
			}
		}
		
		return counter;
	}
	
	
}
