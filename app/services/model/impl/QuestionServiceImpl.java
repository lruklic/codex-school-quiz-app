package services.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.Grade;
import models.Question;
import models.Subject;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import services.model.QuestionService;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

/**
 * Implementation of question service.
 * 
 * @author Luka Ruklic
 * @author Ivan Weber
 *
 */

@Transactional
public class QuestionServiceImpl extends BaseModelServiceImpl<Question> implements QuestionService {

	protected QuestionServiceImpl() {
		super(Question.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> findQuestionsByAdmin(String username) {
		Query query = JPA.em().createQuery("SELECT q FROM Question q WHERE q.author.username = :username", Question.class);
		query.setParameter("username", username);
		return query.getResultList();
	}
	
	@Override
	public long countQuestions(Grade grade, Subject subject, List<String> chapters) {
		
		String queryString = "SELECT COUNT(*) FROM Question q WHERE q.grade = :grade AND q.subject = :subject";
		
		queryString = addChaptersQueryPart(chapters, queryString);
		
		Query query = JPA.em().createQuery(queryString);
		query.setParameter("grade", grade);
		query.setParameter("subject", subject);
		
		if (chapters != null) {
			int i = 1;
			for (String chapter : chapters) {
				query.setParameter((i++), "%"+chapter+"%");
			}
		}
		
		return (long) query.getSingleResult();
	}
	
	@Override
	public List<Question> getSimilarQuestions(Long id) {
		Question current = findById(id);
		List<Question> others = findAll();
		
		
		List<Question> similar = new ArrayList<Question>();
		JaroWinkler jaroWinkler = new JaroWinkler();
		for (Question question : others) {
			if (jaroWinkler.getSimilarity(current.questionText, question.questionText) > 0.5) {
				similar.add(question);
			}
		}
		
		return similar;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getQuestionsBySubjects(List<Subject> subjects) {
		List<Question> questions = new ArrayList<>();
		
		for (Subject subject : subjects) {
			Query query = JPA.em().createQuery("SELECT q FROM Question q WHERE q.subject = :subject", Question.class);
			query.setParameter("subject", subject);
			
			questions.addAll(query.getResultList());
		}
		
		return questions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getQuestions(Grade grade, Subject subject, List<String> chapters) {
		List<Question> questions = new ArrayList<>();
		
		String queryString = "SELECT q FROM Question q WHERE q.grade = :grade AND q.subject = :subject";
		
		queryString = addChaptersQueryPart(chapters, queryString);
		
		Query query = JPA.em().createQuery(queryString, Question.class);
		query.setParameter("grade", grade);
		query.setParameter("subject", subject);
		
		if (chapters != null) {
			int i = 1;
			for (String chapter : chapters) {
				query.setParameter((i++), "%"+chapter+"%");
			}
		}
		
		questions.addAll(query.getResultList());
		
		return questions;
	}
	
	/**
	 * Add chapter conditions to query that searches questions in DB.
	 * 
	 * @param chapters list of chapter conditions
	 * @param queryString query string to which conditions are appended
	 * @return query string with chapter conditions
	 */
	private String addChaptersQueryPart(List<String> chapters, String queryString) {
		if (chapters != null && chapters.size() > 0) {
			queryString += " AND (";
			for (int i = 1; i <= chapters.size(); i++) {
				queryString += " q.chapters LIKE ?" + i;
				if (i < chapters.size()) {
					queryString += " OR";
				} else {
					queryString += ")";
				}
			}
		}
		return queryString;
	}

}
