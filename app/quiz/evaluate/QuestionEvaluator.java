package quiz.evaluate;

import java.util.ArrayList;
import java.util.List;

import models.Question;
import models.enums.QuestionType;
import models.questions.ComposedQuestion;
import models.questions.ConnectCorrectQuestion;
import models.questions.InputAnswerQuestion;
import models.questions.MultipleAnswerQuestion;
import models.questions.MultipleChoiceQuestion;
import models.questions.TrueFalseQuestion;
import models.questions.sub.InputAnswerSubQuestion;
import models.questions.sub.SubQuestion;
import quiz.DetailedQuestion;
import quiz.QuestionSet;
import quiz.Quiz;
import quiz.evaluate.EvaluatedQuestion;
import quiz.evaluate.grade.QuestionGrader;
import quiz.evaluate.grade.QuestionScore;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

import engines.InputAnswerEvaluateEngine;
import enums.AnswerType;

/**
 * Class that contains methods for evaluating and scoring quizes.
 * 
 * @author Luka Ruklic
 *
 */

public class QuestionEvaluator {
	
	/**
	 * Method that receives list of questions and evaluates it.
	 * 
	 * @param questionList list of JSON nodes from HTML that are questions and contain user answers
	 * @param questionSet set of questions for one quiz
	 * @return result of a quiz
	 */
	public QuizResult evaulateQuiz(List<JsonNode> questionList, Quiz quiz) {
		
		QuizResult result = new QuizResult();
		
		QuestionSet questionSet = quiz.getQuestionSet();
		
		for (JsonNode givenAnswer : questionList) {
			
			Long id = givenAnswer.get("id").asLong();
			DetailedQuestion detailedQuestion = questionSet.getDetailedQuestion(id);
			
			QuestionResult questionResult = evaluateQuestion(detailedQuestion.question, givenAnswer);
			QuestionScore questionScore = QuestionGrader.gradeQuestion(questionResult, detailedQuestion);
			
			EvaluatedQuestion evaluatedQuestion = new EvaluatedQuestion(questionResult, questionScore);
			
			result.addEvaluatedQuestion(evaluatedQuestion);
			
		}
		
		return result;
		
	}
	
	/**
	 * Method that evaluates if the answer given by user is correct.
	 * 
	 * @param question given question
	 * @param givenAnswer answer given by user
	 * @return QuestionResult that contains all the information about question and given answer
	 */
	private QuestionResult evaluateQuestion(Question question, JsonNode givenAnswer) {
		
		QuestionResult qrp = new QuestionResult(question);
		
		QuestionType qType = question.questionType;
		
		StringBuilder givenAnswerText = new StringBuilder();
		
		JsonNode answersNode = givenAnswer.get("answers");
		
		switch(qType) {
		case CONNECT_CORRECT:
			ConnectCorrectQuestion ccq = (ConnectCorrectQuestion) question;
			
			qrp.isCorrect = AnswerType.CORRECT;
			
			// if no answer is provided, mark as Unanswered
			if (answersNode != null) {
				List<JsonNode> answersNodes = Lists.newArrayList(answersNode.elements());
				
				boolean unanswered = true;
				
				for (JsonNode answerNode : answersNodes) {
					List<JsonNode> elements = Lists.newArrayList(answerNode.elements());
					
					String value = elements.get(0).asText();
					String key = elements.get(1).asText();
					
					if (!value.equals("null")) {
						unanswered = false;
					}
					
					if (value.equals("")) {
						value = "EMPTY_STRING";
					} else {
						givenAnswerText.append(key + " : ");
						if (!value.equals("null")) {
							givenAnswerText.append(value);
						} else {
							givenAnswerText.append("/");
						}
						
						givenAnswerText.append(", ");
					}
					
					String correctValueForKey = ccq.getAnswerPairs().get(key);
					if (unanswered) {
						qrp.isCorrect = AnswerType.NOT_ANSWERED;
					} else if (!correctValueForKey.equals(value)) {
						qrp.isCorrect = AnswerType.INCORRECT;
					}
				}
				
			}
			qrp.givenAnswer = givenAnswerText.toString().substring(0, givenAnswerText.length()-2);
			break;
		case INPUT_ANSWER:
			if (answersNode != null && answersNode.asText().length() > 0) {
				
				boolean answerCorrect = InputAnswerEvaluateEngine.evaluate(answersNode.asText(), (InputAnswerQuestion) question);
				
				if (answerCorrect) {
					qrp.isCorrect = AnswerType.CORRECT;
				} else {
					qrp.isCorrect = AnswerType.INCORRECT;
				}
				
				qrp.givenAnswer = answersNode.asText();
				
			} else {
				qrp.isCorrect = AnswerType.NOT_ANSWERED;
			}
			
			break;
		case MULTIPLE_ANSWER:
			qrp.isCorrect = AnswerType.CORRECT;
			
			if (answersNode != null) {
				List<JsonNode> answersNodes = Lists.newArrayList(answersNode.elements());
				
				List<String> givenAnswers = new ArrayList<>();
				for (JsonNode answerNode : answersNodes) {
					String answer = answerNode.asText();
					givenAnswers.add(answer);
					givenAnswerText.append(answer + ", ");
				}
				
				if (givenAnswers.size() != 0) {
					qrp.givenAnswer = givenAnswerText.toString().substring(0, givenAnswerText.length()-2);
				} 
				
				MultipleAnswerQuestion maq = (MultipleAnswerQuestion) question;
				for (String correctAnswer : maq.getCorrectAnswers()) {
					if (givenAnswers.contains(correctAnswer)) {
						// do nothing
					} else {
						qrp.isCorrect = AnswerType.INCORRECT;
					}
				}
				
				for (String incorrectAnswer : maq.getIncorrectAnswers()) {
					
					if (givenAnswers.contains(incorrectAnswer)) {
						qrp.isCorrect = AnswerType.INCORRECT;
					}
				}
				
			}
			break;
		case MULTIPLE_CHOICE:
			String answer = "";
			if (answersNode != null) {
				answer = answersNode.asText();
				
				if (answer.equals(((MultipleChoiceQuestion)question).correctAnswer)) {
					qrp.isCorrect = AnswerType.CORRECT;
				} else {
					qrp.isCorrect = AnswerType.INCORRECT;
				}
			} else {
				qrp.isCorrect = AnswerType.NOT_ANSWERED;
			}
			
			qrp.givenAnswer = answer;
			
			break;
		case TRUE_FALSE:
			if (answersNode != null) {
				TrueFalseQuestion tfq = (TrueFalseQuestion) question;
				if ((answersNode.asText().equals("true") && tfq.answer) || (answersNode.asText().equals("false") && !tfq.answer)) {
					qrp.isCorrect = AnswerType.CORRECT;
				} else {
					qrp.isCorrect = AnswerType.INCORRECT;
				}
				
				qrp.givenAnswer = answersNode.asText();
				
			} else {
				// this depends on how user answers question in HTML
				qrp.isCorrect = AnswerType.NOT_ANSWERED;
			}
			
			break;
		case COMPOSED:
			qrp.isCorrect = AnswerType.CORRECT;
			
			if (answersNode != null) {
				ComposedQuestion cq = (ComposedQuestion) question;
				List<JsonNode> answersNodes = Lists.newArrayList(answersNode.elements());
				
				boolean unanswered = true;
				
				for (JsonNode answerNode : answersNodes) {
					List<JsonNode> elements = Lists.newArrayList(answerNode.elements());
					
					String subquestionId = elements.get(0).asText();
					String subquestionAnswer = elements.get(1).asText();
					
					if (!subquestionAnswer.equals("null")) {
						unanswered = false;
					}
					
					for (SubQuestion sq : cq.subquestions) {
						
						if (sq instanceof InputAnswerSubQuestion) { // TODO other types od sub questions
 							if (subquestionId.equals(String.valueOf(sq.id))) {
								boolean answerCorrect = InputAnswerEvaluateEngine.evaluate(subquestionAnswer, (InputAnswerSubQuestion) sq);
								
								if (unanswered) {
									qrp.isCorrect = AnswerType.NOT_ANSWERED;
								} else if (!answerCorrect) {
									qrp.isCorrect = AnswerType.INCORRECT;
								}
							}
						}

					}
				}
			}
			break;
		default:
			break;
		}
		
		qrp.createAnswerRecap();
		
		return qrp;
		
	}
	
}
