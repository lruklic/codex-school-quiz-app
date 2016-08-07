package models.questions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import models.Admin;
import models.Grade;
import models.Question;
import models.Subject;
import models.enums.QuestionType;

/**
 * Question with only two options (TRUE or FALSE). Correct answer is contained in <code>answer</code>
 * field, with 1 symbolizing TRUE and 0 symbolizing FALSE.
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "true_false")
@PrimaryKeyJoinColumn(name="id")
public class TrueFalseQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "answer")
	public Boolean answer;

	public TrueFalseQuestion() {
	}
	
	/**
	 * Constructor. 
	 * 
	 * @param questionText
	 * @param questionType
	 * @param grade
	 * @param subject
	 * @param chapters
	 * @param subjectContent
	 * @param specialTags
	 * @param difficulty
	 * @param explanation
	 * @param admin
	 * @param answer
	 */
	public TrueFalseQuestion(String questionText, QuestionType questionType, Grade grade, Subject subject, String chapters,
			String subjectContent, String specialTags, String explanation, Admin admin, Boolean answer) {
		
		super(questionText, questionType, grade, subject, chapters, subjectContent, specialTags, explanation, admin);
		this.answer = answer;
	}

	@Override
	public String getQuestionAnswerText() {
		if (answer) {
			return "true";
		} else {
			return "false";
		}
	}

	@Override
	public List<String> getQuestionSpecificsAsList() {
		List<String> questionSpecifics = new ArrayList<>();
		questionSpecifics.add(answer.toString());
		return questionSpecifics;
	}

	@Override
	public Integer getNumberOfPossibleAnswers() {
		return 2;
	}
	
}
