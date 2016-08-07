package services;

import java.io.File;
import java.util.List;
import java.util.Map;

import models.Question;

public interface PortService {

	public Map<Integer, String> importQuestions(File file);

	public byte[] exportAsCSV(List<Question> questionList);

	public byte[] exportAsXLS(List<Question> questionList);
	
}
