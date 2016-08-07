package services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Admin;
import models.Image;
import models.Question;
import models.enums.QuestionType;
import models.questions.ConnectCorrectQuestion;
import models.questions.InputAnswerQuestion;
import models.questions.MultipleAnswerQuestion;
import models.questions.MultipleChoiceQuestion;
import models.questions.TrueFalseQuestion;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import play.i18n.Messages;
import services.PortService;
import services.model.GradeService;
import services.model.QuestionService;
import services.model.SubjectService;
import services.model.UserService;
import session.Session;

/**
 * Class that implements methods that receive question list and return 
 * 
 * @author Luka Ruklic
 *
 */

public class PortServiceImpl implements PortService {

	@Inject
	public static SubjectService subjectService;
	
	@Inject
	public static GradeService gradeService;
	
	@Inject
	public static UserService userService;
	
	@Inject
	public static QuestionService questionService;
	
	private final static int QUESTION_TEXT_INDEX = 0;
	private final static int QUESTION_TYPE_INDEX = 1;
	private final static int GRADE_INDEX = 2;
	private final static int SUBJECT_INDEX = 3;
	private final static int CHAPTERS_INDEX = 4;
	private final static int SUBJECT_CONTENT_INDEX = 5;
	private final static int SPECIAL_TAG_INDEX = 6;
	private final static int EXPLANATION_INDEX = 7;
	private final static int PICTURE_INDEX = 8;
	private final static int CONNECT_CORRECT_INDEX = 9;
	private final static int TRUE_FALSE_INDEX = 9;
	private final static int INPUT_ANSWER_INDEX = 9;
	private final static int MC_CORRECT = 9;
	private final static int MA_CORRECT = 9;
	private final static int MC_INCORRECT = 10;
	private final static int MA_INCORRECT = 10;
	
	private final static String IMPORT_STATUS = "import.status";
	private final static String IMPORT_ERROR = "import.error";
	
	
	
	/**
	 * Export question list as CSV file.
	 * 
	 * @param questionList
	 * @return CSV turned to bytes for display in controller
	 */
	public byte[] exportAsCSV(List<Question> questionList) {
		
		byte[] output = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
			CSVPrinter printer = new CSVPrinter(sb, csvFileFormat);
			
			for (Question q : questionList) {
				List<String> question = questionsAsList(q);
				// somehow add fields outside standard question
				printer.printRecord(question);
			}
		
			output = sb.toString().getBytes();
			
			printer.close();
			
		} catch (IOException ex) {

		}

		return output;
		
	}

	/**
	 * Export question list as XLS Excel file.
	 * @param questionList
	 * @return XLS file turned to byte form for display in controller
	 */
	public byte[] exportAsXLS(List<Question> questionList) {
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(Messages.get("questions"));
		
		int i = 0;
		for (Question q : questionList) {
			Row row = sheet.createRow(i);
			// add width for question tags
			List<String> question = questionsAsList(q);
			
			int j = 0;
			for (String field : question) {
				Cell cell = row.createCell(j);
				cell.setCellValue(field);
				j++;
			}
			i++;
		}
		
		try {
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wb.getBytes();
		
	}
	
	public byte[] exportAsPDF(List<Question> questionList) {
		
		Document document = new Document();
		document.open();
		
		try {
			PdfWriter.getInstance(document, new ByteArrayOutputStream());
			
			for (Question question : questionList) {
				document.add(new Paragraph(question.questionText));
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		document.close();
		
		return null;
	}
	
	public Map<Integer,String> importQuestions(File file) {
	
		Map<Integer,String> importResultMap = new HashMap<>();
		 
		try {
			CSVParser parser = CSVParser.parse(file, Charsets.UTF_8, CSVFormat.RFC4180);
			
			int i = 1;
			for (CSVRecord record : parser) {
				
				Map<String,String> map = new HashMap<>();
				String importResult = null;
				
				try {
					createQuestion(record);
					map.put(IMPORT_STATUS, "true");
				} catch (IllegalArgumentException e) {
					map.put(IMPORT_STATUS, "false");
					map.put(IMPORT_ERROR, Messages.get("error.import.question"));	// TODO više vrsta errora
				}
				
				importResult = new ObjectMapper().writeValueAsString(map);
				importResultMap.put(i, importResult);
				i++;
			}
			
		} catch (IOException e) {	
			importResultMap.put(0, Messages.get("error.import.file"));		
		}
		
		return importResultMap;
	
	}
	
	private boolean createQuestion(CSVRecord record) {
		
		QuestionType qt = QuestionType.valueOf(QuestionType.class, record.get(QUESTION_TYPE_INDEX));
		
		Question q = null;
		
		switch(qt) {
		case MULTIPLE_CHOICE:
			MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
			mcq.correctAnswer = record.get(MC_CORRECT);
			mcq.incorrectAnswers = record.get(MC_INCORRECT);
			q = mcq;
			break;
		case MULTIPLE_ANSWER:
			MultipleAnswerQuestion maq = new MultipleAnswerQuestion();
			maq.correctAnswers = record.get(MA_CORRECT);
			maq.incorrectAnswers = record.get(MA_INCORRECT);
			q = maq;
			break;
		case TRUE_FALSE:
			TrueFalseQuestion tfq = new TrueFalseQuestion();
			tfq.answer = (record.get(TRUE_FALSE_INDEX) == "true") ? true : false;
			q = tfq;
			break;
		case INPUT_ANSWER:
			InputAnswerQuestion iaq = new InputAnswerQuestion();
			iaq.answer = record.get(INPUT_ANSWER_INDEX);
			q= iaq;
			break;
		case CONNECT_CORRECT:
			ConnectCorrectQuestion ccq = new ConnectCorrectQuestion();
			
			Map<String, String> answerPairs = new HashMap<>();
			
			String answerPairsString = record.get(CONNECT_CORRECT_INDEX);
			if (answerPairsString != null) {
				String[] answerPairsArray = answerPairsString.split("\\|");
				
				for (String answerPair : answerPairsArray) {
					String[] answerPairArray = answerPair.split(":");
					answerPairs.put(answerPairArray[0], answerPairArray[1]);
				}
			}
			ccq.answerPairs = answerPairs;
			q = ccq;
			break;
		default:
			break;
		
		}
		
		q.questionType = qt;
		q.lastEdited = System.currentTimeMillis();
		q.approved = true;
		
		q.questionText = record.get(QUESTION_TEXT_INDEX);
		q.grade = gradeService.findByName(record.get(GRADE_INDEX));		// TODO cache instead of DB
		q.subject = subjectService.findByName(record.get(SUBJECT_INDEX));
		q.chapters = record.get(CHAPTERS_INDEX);
		q.subjectContent = record.get(SUBJECT_CONTENT_INDEX);
		q.specialTags = record.get(SPECIAL_TAG_INDEX);
		q.explanation = record.get(EXPLANATION_INDEX);
		
		String imagePath = record.get(PICTURE_INDEX);		// TODO add image size
		if (imagePath.length() > 0) {
			q.image = new Image(imagePath);
		}
		
		q.author = (Admin) userService.findByUsername(Session.getUsername());
		
		questionService.save(q);
		
		return true;
	}
	
	private List<String> questionsAsList(Question q) {
		List<String> question = new ArrayList<>();
		
		question.add(q.questionText);
		question.add(q.questionType.toString());
		question.add(q.grade.name);
		question.add(q.subject.name);
		question.add(q.chapters);
		question.add(q.subjectContent);
		question.add(q.specialTags);
		question.add(q.explanation);
		
		if (q.image != null) {
			question.add(q.image.filePath);
		} else {
			question.add("");
		}
		
		
		for (String specificData : q.getQuestionSpecificsAsList()) {
			question.add(specificData);
		}
		
		return question;
	}
}
