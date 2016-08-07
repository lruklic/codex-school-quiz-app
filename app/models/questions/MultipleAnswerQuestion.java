package models.questions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
 * Question with multiple answers and random number of correct ones.
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "multiple_answer")
@PrimaryKeyJoinColumn(name="id")
public class MultipleAnswerQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * String containing correct answers, separated with semicolon.
	 */
	@Column(name = "correctAnswer")
	public String correctAnswers;
	/**
	 * String containing incorrect answers, separated with semicolon.
	 */
	@Column(name = "incorrectAnswers")
	public String incorrectAnswers;

	public MultipleAnswerQuestion() {
	}
	
	public MultipleAnswerQuestion(String questionText, QuestionType questionType, Grade grade, Subject subject, String chapters,
			String subjectContent, String specialTags, String explanation, Admin admin, String correctAnswers, String incorrectAnswers) {
		
		super(questionText, questionType, grade, subject, chapters, subjectContent, specialTags, explanation, admin);
		this.correctAnswers = correctAnswers;
		this.incorrectAnswers = incorrectAnswers;
	}
	
	public List<String> getCorrectAnswers() {
		return getAnswers(correctAnswers);
	}
	
	public List<String> getIncorrectAnswers() {
		return getAnswers(incorrectAnswers);
	}
	
	private List<String> getAnswers(String list) {
		List<String> answersList = new ArrayList<String>();
		
		if (list.length() > 0) {
			String[] answerArray = list.split(";");
			for (String answer : answerArray) {
				answersList.add(answer);
			}
		}
		
		return answersList;
	}
	
	/**
	 * Method that returns all answers shuffled.
	 * 
	 * @return list with shuffled answers, both correct and incorrect
	 */
	
	public List<String> getAllAnswersMixed() {
		List<String> allAnswers = new ArrayList<String>(getCorrectAnswers());
		allAnswers.addAll(getIncorrectAnswers());
		
		long seed = System.nanoTime();
		Collections.shuffle(allAnswers, new Random(seed));
		
		return allAnswers;
	}

	@Override
	public String getQuestionAnswerText() {
		StringBuilder sb = new StringBuilder();
		for (String answer : getCorrectAnswers()) {
			sb.append(answer);
			sb.append(", ");
		}
		
		if (sb.length() == 0) {
			return "NONE_CORRECT";
		} else {
			return sb.substring(0, sb.length()-2);
		}

	}

	@Override
	public List<String> getQuestionSpecificsAsList() {
		List<String> questionSpecifics = new ArrayList<>();
		
		questionSpecifics.add(correctAnswers);
		questionSpecifics.add(incorrectAnswers);
		
		return questionSpecifics;
	}

	public Integer getNumberOfAnswers() {
		return getIncorrectAnswers().size() + getCorrectAnswers().size();
	}
	
	@Override
	public Integer getNumberOfPossibleAnswers() {
		// This value is not correct, but common practice dictates that questions with multiple answers don't contribute to negative points
		return Integer.MAX_VALUE;
	}

	
}
