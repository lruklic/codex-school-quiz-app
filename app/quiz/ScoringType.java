package quiz;

/**
 * Type of scoring for question. Defines how points are distributed in cases of correct, incorrect, 
 * unanswered or partially answered questions.
 * 
 * @author Luka Ruklic
 *
 */

public enum ScoringType {
	/**
	 * Correct answer gets all points; incorrect, unanswered or partially correct answers bring no points. 
	 */
	ALL_OR_NOTHING,
	/**
	 * Corect answer gets all points; incorrect answer brings no points for input answer questions and negative points
	 * for questions with multiple choice (- maximum points/number of options). Unanswered questions bring no points,
	 * negative or positive. Partially correct answers bring partial points. 
	 */
	STANDARD_COLLEGE,
	/**
	 * Every correct answer gets maximum amount of points. Every unanswered question gets maximum/3 points. Every 
	 * incorrect answer gets zero points.  
	 */
	ENTRANCE_EXAM;
	
}
