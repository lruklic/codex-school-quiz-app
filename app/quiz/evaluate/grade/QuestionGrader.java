package quiz.evaluate.grade;

import quiz.DetailedQuestion;
import quiz.ScoringFormula;
import quiz.evaluate.QuestionResult;

public class QuestionGrader {
	
	/**
	 * Method that assigns appropriate amount of points to the question.
	 * 
	 * @param questionResult
	 * @return
	 */
	public static QuestionScore gradeQuestion(QuestionResult questionResult, DetailedQuestion detailedQuestion) {
		
		ScoringFormula scoringFormula = new ScoringFormula(detailedQuestion.scoringType);
		
		Double maxPoints = detailedQuestion.maxPoints;
		
		// TODO other scoring types; this is only ALL_OR_NOTHING
		// Regardless of scoring type for question, CORRECT answer bring max amount of points
		switch (questionResult.isCorrect) {
		case CORRECT:
			return new QuestionScore(scoringFormula.getCorrectScore(maxPoints), maxPoints);
		case INCORRECT:
			return new QuestionScore(scoringFormula.getIncorrectScore(maxPoints, detailedQuestion.question.getNumberOfPossibleAnswers()), maxPoints);
		case NOT_ANSWERED:
			return new QuestionScore(scoringFormula.getUnansweredScore(maxPoints), maxPoints);
		case PARTIALLY_CORRECT:
			return new QuestionScore(scoringFormula.getUnansweredScore(maxPoints), maxPoints);	// TODO partial answers
		default:
			break;
		
		}

		return null;
	
	}
	
}
