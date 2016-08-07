package quiz;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Question;

/**
 * Set of questions for single quiz.
 * 
 * @author Luka Ruklic
 *
 */

public class QuestionSet {
	
	/**
	 * Question set id.
	 */
	public long id;
	
	/**
	 * Map with questions with question id as key.
	 */
	private Map<Long, DetailedQuestion> questionMap = new HashMap<Long, DetailedQuestion>();
	
	/**
	 * Constructor that receives collection of question and saves them to map with id as key.
	 * 
	 * @param questionCollection collection of questions
	 */
	
	public QuestionSet(Collection<Question> questionCollection, List<Double> maxPoints, ScoringType scoringType) {
		
		if (maxPoints == null || scoringType == null || questionCollection.size() != maxPoints.size()) {
			for (Question question : questionCollection) {
				questionMap.put(question.id, new DetailedQuestion(question));
			}
		} else {
			int index = 0;
			for (Question question : questionCollection) {
				questionMap.put(question.id, new DetailedQuestion(question, maxPoints.get(index), scoringType));
			}
		}
		

	}
	
	public DetailedQuestion getDetailedQuestion(Long id) {
		return questionMap.get(id);
	}
	
	public Question getQuestion(Long id) {
		return questionMap.get(id).question;
	}
}
