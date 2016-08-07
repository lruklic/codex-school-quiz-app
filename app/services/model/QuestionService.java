package services.model;

import java.util.List;

import models.Grade;
import models.Question;
import models.Subject;

public interface QuestionService extends BaseModelService<Question> {
	
	public List<Question> findQuestionsByAdmin(String username);
	
	public long countQuestions(Grade grade, Subject subject, List<String> chapters);
	
	/**
	 * Retrives questions by selected subjects. Used when fetching questions for admins with permissions for certain subjects.
	 * 
	 * @param subjects list of subjects that user chose
	 * @return list of questions under selected subjects
	 */
	public List<Question> getQuestionsBySubjects(List<Subject> subjects);
	
	public List<Question> getQuestions(Grade grade, Subject subject, List<String> chapters);
	
	public List<Question> getSimilarQuestions(Long id);
	
}
