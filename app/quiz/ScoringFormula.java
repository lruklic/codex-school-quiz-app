package quiz;

/**
 * Formulas by which amount of points for question answers are given.
 * 
 * @author Luka Ruklic
 *
 */

public class ScoringFormula {

	private ScoringType scoringType;
	
	private Double correctPercentage;
	private Double incorrectPercentage;
	private Double unansweredPercentage;
	private Double partialPercentage;
	
	/**
	 * Constructor that defines percentage of points given for each scoring type based on scoring type parameter.
	 * 
	 * @param scoringType enum member that defines how many points are awarded for correct, incorrect, unanswered and partially correct answers
	 */
	public ScoringFormula(ScoringType scoringType) {
		
		this.scoringType = scoringType;
		
		switch(scoringType) {
		case ALL_OR_NOTHING:
			correctPercentage = 1.0;
			incorrectPercentage = unansweredPercentage = partialPercentage = 0.0;
			break;
		case STANDARD_COLLEGE:
			correctPercentage = 1.0;
			incorrectPercentage = partialPercentage = null;
			unansweredPercentage = 0.0;
			break;
		case ENTRANCE_EXAM:
			correctPercentage = 3.0;
			incorrectPercentage = partialPercentage = 0.0;
			unansweredPercentage = 1.0;
			break;
		default:
			break;
			
		}
	}
	
	/**
	 * Get amount of points for correct answer.
	 * 
	 * @param maxPoints maximum amount of points
	 * @return points scored for correct answer
	 */
	public Double getCorrectScore(Double maxPoints) {
		return correctPercentage*maxPoints;
	}
	
	/**
	 * Get amount of points for incorrect answer.
	 * 
	 * @param maxPoints maximum amount of points
	 * @return points scored for incorrect answer
	 */
	public Double getIncorrectScore(Double maxPoints) {
		return getIncorrectScore(maxPoints, null);
	}
	
	/**
	 * Extended method to get amount of points for incorrect answer. User can define how many options for 
	 * answer were available. This information is important to define amount of negative points.
	 * 
	 * @param maxPoints maximum amount of points
	 * @param numberOfAnswers number of possible answer for question type
	 * @return points scored for incorrect answer
	 */
	public Double getIncorrectScore(Double maxPoints, Integer numberOfAnswers) {
		if (incorrectPercentage != null || numberOfAnswers == null) {
			return incorrectPercentage*maxPoints;
		} else {
			if (scoringType == ScoringType.STANDARD_COLLEGE) {
				return (-1.0) * (maxPoints/numberOfAnswers);
			} else {
				return 0.0;	// TODO other types
			}
		}
		
	}

	public Double getUnansweredScore(Double maxPoints) {
		return unansweredPercentage*maxPoints;
	}

	public Double getPartialScore(Double maxPoints) {
		if (partialPercentage != null) {
			return partialPercentage*maxPoints;
		} else {
			return 0.0;	// TODO implement partial points
		}
		
	}
	
}
