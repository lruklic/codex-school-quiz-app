package quiz;

import models.Question;

/**
 * Wrapper for question with additional details like maximum points and scoring type.
 * 
 * @author Luka Ruklic
 *
 */

public class DetailedQuestion {
	
	/**
	 * Instance of question.
	 */
	public Question question;
	/**
	 * Maximum points for this question.
	 */
	public Double maxPoints;
	/**
	 * Scoring type for this question.
	 */
	public ScoringType scoringType;
	/**
	 * Constructor for detailed question. Maximum points are set to 1 and scoring type is "all or nothing".
	 * @param question question
	 */
	public DetailedQuestion(Question question) {
		this(question, null, null);
	}

	/**
	 * Extended constructor for detailed question. User can provide maximum amount of points and type of scoring.
	 * @param question question
	 * @param maxPoints maximum points for question
	 * @param scoringType scoring type for question
	 */
	public DetailedQuestion(Question question, Double maxPoints, ScoringType scoringType) {
		this.question = question;
		
		if (maxPoints == null) {
			this.maxPoints = 1.0;
		} else {
			this.maxPoints = maxPoints;
		}
		
		if (scoringType == null) {
			this.scoringType = ScoringType.ALL_OR_NOTHING;
		} else {
			this.scoringType = scoringType;
		}
		
	}
	
	
}
