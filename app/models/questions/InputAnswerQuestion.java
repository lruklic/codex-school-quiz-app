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
 * Question
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "input_answer")
@PrimaryKeyJoinColumn(name="id")
public class InputAnswerQuestion extends Question implements InputAnswer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "answer")
	public String answer;

	public InputAnswerQuestion() {
	}
	
	public InputAnswerQuestion(String questionText, QuestionType questionType, Grade grade, Subject subject, String chapters,
			String subjectContent, String specialTags, String explanation, Admin admin, String answer) {
		
		super(questionText, questionType, grade, subject, chapters, subjectContent, specialTags, explanation, admin);
		this.answer = answer;
	}

	@Override
	public String getQuestionAnswerText() {
		return answer;
	}

	@Override
	public List<String> getQuestionSpecificsAsList() {
		List<String> questionSpecifics = new ArrayList<>();
		questionSpecifics.add(answer);
		return questionSpecifics;
	}

	@Override
	public Integer getNumberOfPossibleAnswers() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getAnswer() {
		return answer;
	}

}
