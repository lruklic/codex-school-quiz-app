package engines;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import models.questions.InputAnswer;

/**
 * Class that implements methods that evaluate if user answer correctly to input answer question.
 * 
 * @author Luka Ruklic
 *
 */

public class InputAnswerEvaluateEngine {

	public static boolean evaluate(String userAnswer, InputAnswer question) {
		
		String[] viableAnswers = question.getAnswer().split(";");
		
		if (viableAnswers.length == 0) {
			// no answer provided, error
			return false;
		}
		
		for (String viableAnswer : viableAnswers) {
			if (viableAnswer.equals(userAnswer)) {
				// answer provided is valid, true
				return true;
			}
		}
		
		for (String viableAnswer : viableAnswers) {
			if (viableAnswer.equalsIgnoreCase(userAnswer)) {
				// answer provided is valid, ignoring the case
				return true;
			}
		}
		
		int closestAnswer = 0;
		for (String viableAnswer : viableAnswers) {
			// TODO compare every viableAnswer with answer provided by use
			// return % of similarity between closest viable answer and user answer 
		}
		
		// TODO if answer is numeric, return delta
		// TODO if answer is numeric, try converting user text answer to numeric
		
		return false;
	}

	
	
}
