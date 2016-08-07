package cache.question;

import java.util.HashMap;
import java.util.Map;

import quiz.Quiz;

/**
 * Singleton class that caches questions.
 * 
 * @author Luka Ruklic
 *
 */

public class QuestionCache {

	private static QuestionCache questionCache;
	
	private static Map<String, Quiz> quizMap = new HashMap<>();
	
	private QuestionCache() {
	}
	
	/**
	 * Method that returns question cache.
	 * 
	 * @return instance of question cache
	 */
	public static QuestionCache getInstance() {
		
		if (questionCache == null) {
			questionCache = new QuestionCache();
		}
		return questionCache;
		
	}
	
	public Quiz getQuiz(String key) {
		return quizMap.get(key);
	}
	
	/**
	 * Add quiz to cache. If already exists quiz with same key, it is replaced.
	 * @param key quiz key; username of user playing the quiz
	 * @param quiz
	 */
	public void addQuiz(String key, Quiz quiz) {
		quizMap.put(key, quiz);
	}
	
	public void removeSet(String key) {
		quizMap.remove(key);
	}
}
