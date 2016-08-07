package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import models.enums.QuestionType;
import cache.models.ModelCache;
import cache.models.ModelCacheType;

/**
 * <p>Question <code>class</code> represents quiz questions contained in the application.</p>
 * 
 * <p>Every class has inherited <code>long</code> identificator from BaseModel, <code>questionText</code>
 * that contains question text, <code>questionType</code> that determines type of the question, grade that 
 * specifies at which level of school curriculum can answer to this question be found, <code>subject</code> 
 * ,<code>chapter</code> and <code>subjectContent</code> which define which subject content question references. 
 * Also, each question can have optional <code>explanation</code> that closely explains correct answer and special
 * tags that note that same question already appeared on competition or nationwide leaving exam. </p> 
 * 
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "question")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Question extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="created")
	public Long created;
	
	@Column(name="lastEdited")
	public Long lastEdited;
	
	@Column(name = "questionText", length = 500)
	public String questionText;
	
	@Enumerated(EnumType.STRING)
	public QuestionType questionType;
	
	@ManyToOne
	public Grade grade;
	
	@ManyToOne
	public Subject subject;
	
	@Column(name = "subjectChapter")
	public String chapters;
	
	@Column(name = "subjectContent")
	public String subjectContent;
	
	@Column(name = "specialTags")
	public String specialTags;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)		// prevent eager loading because explanation can be big chunk of text
	@Column(name = "explanation")
	public String explanation;
	
	@Column(name = "approved")
	public Boolean approved;
	
	@OneToOne(cascade = CascadeType.ALL)
	public Image image;
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Admin author;
	
	/**
	 * Empty contructor.
	 */
	public Question() {
	}
	/**
	 * Constructor containing all question parameters. 
	 * 
	 * @param questionText
	 * @param questionType
	 * @param grade
	 * @param subject
	 * @param chapters
	 * @param subjectContent
	 * @param admin
	 */
	public Question(String questionText, QuestionType questionType, Grade grade, Subject subject, String chapters,
			String subjectContent, String specialTags, String explanation, Admin author) {
		this.created = System.currentTimeMillis();
		this.lastEdited = System.currentTimeMillis();
		this.questionText = questionText;
		this.questionType = questionType;
		this.grade = grade;
		this.subject = subject;
		this.chapters = chapters;
		this.subjectContent = subjectContent;
		this.specialTags = specialTags;
		this.explanation = explanation;
		this.approved = false;
		this.author = author;
	}
	
	/**
	 * Method that returns question correct answer(s) in text form. Every question type must have own implementation.
	 * @return question answer in text form
	 */
	public abstract String getQuestionAnswerText();
	/**
	 * Method that returns question specific details for question which other question types don't have.
	 * @return
	 */
	public abstract List<String> getQuestionSpecificsAsList();
	
	/**
	 * Returns number of possible answers that can be given for this question.
	 * 
	 * @return number of possible answers
	 */
	public abstract Integer getNumberOfPossibleAnswers();

	/**
	 * Getter for chapters that turns string into list of Chapters.
	 * 
	 * @return Chapter list
	 */
	public List<Chapter> getChapters() {
		List<Chapter> chaptersList = new ArrayList<Chapter>(); 
		
		String[] chaptersArray = chapters.split(";");
		for (String chapter : chaptersArray) {
			chaptersList.add((Chapter) ModelCache.getInstance().getSet(ModelCacheType.CHAPTER, chapter));	// bad design
		}
		
		return chaptersList;
	}
	
}
