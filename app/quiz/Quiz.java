package quiz;

public class Quiz {

	private QuestionSet questionSet;
	
	private QuizType quizType;

	public QuestionSet getQuestionSet() {
		return questionSet;
	}

	public QuizType getQuizType() {
		return quizType;
	}

	public Quiz(QuestionSet questionSet, QuizType quizType) {
		this.questionSet = questionSet;
		this.quizType = quizType;
	}
	
}
