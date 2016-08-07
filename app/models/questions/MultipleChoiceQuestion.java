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
 * Question with multiple answer options where only one is correct.
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "multiple_choice")
@PrimaryKeyJoinColumn(name="id")
public class MultipleChoiceQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "correctAnswer")
	public String correctAnswer;
	
	@Column(name = "incorrectAnswers")
	public String incorrectAnswers;

	public MultipleChoiceQuestion() {
	}
	
	public MultipleChoiceQuestion(String questionText, QuestionType questionType, Grade grade, Subject subject, String chapters,
			String subjectContent, String specialTags, String explanation, Admin admin, String correctAnswer, String incorrectAnswers) {
		
		super(questionText, questionType, grade, subject, chapters, subjectContent, specialTags, explanation, admin);
		this.correctAnswer = correctAnswer;
		this.incorrectAnswers = incorrectAnswers;
	}
	
	/**
	 * Method that returns the list of all incorrect answers.
	 * 
	 * @return list containing all of the incorrect answers
	 */
	public List<String> getIncorrectAnswers() {
		List<String> incorrectAnswersList = new ArrayList<String>();
		String[] incorrectAnswerArray = incorrectAnswers.split(";");
		for (String incorrectAnswer : incorrectAnswerArray) {
			incorrectAnswersList.add(incorrectAnswer);
		}

		return incorrectAnswersList;
	}
	
	/**
	 * Method that returns all answers, shuffled. Used for answer display in HTML.
	 * 
	 * @return shuffled list of answers, both correct and incorrect
	 */
	public List<String> getAllAnswersMixed() {
		List<String> allAnswers = new ArrayList<>();
		allAnswers = getIncorrectAnswers();
		allAnswers.add(correctAnswer);
		
		long seed = System.nanoTime();
		
		Collections.shuffle(allAnswers, new Random(seed));
		
		return allAnswers;
	}

	@Override
	public String getQuestionAnswerText() {
		return correctAnswer;
	}

	@Override
	public List<String> getQuestionSpecificsAsList() {
		List<String> questionSpecifics = new ArrayList<>();
		
		questionSpecifics.add(correctAnswer);
		questionSpecifics.add(incorrectAnswers);
		
		return questionSpecifics;
	}

	@Override
	public Integer getNumberOfPossibleAnswers() {
		return getIncorrectAnswers().size() + 1;
	}

	
}
