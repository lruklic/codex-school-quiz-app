package models.questions;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import models.Admin;
import models.Grade;
import models.Question;
import models.Subject;
import models.enums.QuestionType;
import models.questions.sub.SubQuestion;

/**
 * Question that is composed of multiple sub questions.
 * 
 * @author Luka Ruklić
 *
 */

@Entity
@Table(name = "composed")
@PrimaryKeyJoinColumn(name="id")
public class ComposedQuestion extends Question {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@OneToMany(cascade = {CascadeType.ALL})
	public List<SubQuestion> subquestions;
	
	public ComposedQuestion() {
	}

	public ComposedQuestion(String questionText, QuestionType questionType, Grade grade, Subject subject,
			String chapters, String subjectContent, String specialTags, String explanation, Admin author, List<SubQuestion> subquestions) {
		
		super(questionText, questionType, grade, subject, chapters, subjectContent, specialTags, explanation, author);
		this.subquestions = subquestions;
	}

	
	
	@Override
	public String getQuestionAnswerText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getQuestionSpecificsAsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getNumberOfPossibleAnswers() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
