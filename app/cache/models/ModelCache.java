package cache.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.BaseModel;
import models.Chapter;
import models.Grade;
import models.Subject;
import services.model.ChapterService;
import services.model.GradeService;
import services.model.SubjectService;

import com.google.inject.Inject;

/**
 * Singleton class that caches subjects, chapters and grades.
 * 
 * @author Luka Ruklic
 *
 */

public class ModelCache {

	@Inject
	private static SubjectService subjectService;
	
	@Inject
	private static GradeService gradeService;
	
	@Inject
	private static ChapterService chapterService;
	
	private static ModelCache cache;
	
	private static Map<String, Subject> subjectMap = new LinkedHashMap<>();
	private static Map<String, Chapter> chapterMap = new LinkedHashMap<>();
	private static Map<String, Grade> gradeMap = new LinkedHashMap<>();

	private ModelCache() {
	}
	
	/**
	 * Method that returns question cache.
	 * 
	 * @return instance of question cache
	 */
	public static ModelCache getInstance() {
		if (cache == null) {
			cache = new ModelCache();
			
			List<Grade> grades = gradeService.findAll();
			for (Grade g : grades) {
				addSet(g.name, g);
			}
			
			List<Subject> subjects = subjectService.findAllSorted("name", "asc");
			for (Subject s : subjects) {
				addSet(s.name, s);
			}
			
			List<Chapter> chapters = chapterService.findAll();
			for (Chapter c : chapters) {
				addSet(c.name, c);
			}
			
		}
		return cache;
	}
	
	public BaseModel getSet(ModelCacheType mct, String key) {
		if (mct == ModelCacheType.CHAPTER)
			return chapterMap.get(key);
		else if (mct == ModelCacheType.GRADE)
			return gradeMap.get(key);
		else 
			return subjectMap.get(key);
	}
	
	/**
	 * Method that fetches all subjects from cache.
	 * 
	 * @return list containing all subject values in cache
	 */
	public List<Subject> getAllSubjects() {
		return new ArrayList<Subject>(subjectMap.values());
	}
	
	public List<Chapter> getAllChapters() {
		return new ArrayList<Chapter>(chapterMap.values());
	}
	
	public List<Grade> getAllGrades() {
		return new ArrayList<Grade>(gradeMap.values());
	}
	

	private static void addSet(String key, BaseModel baseModel) {
		if (baseModel instanceof Chapter)
			chapterMap.put(key, (Chapter) baseModel);
		else if (baseModel instanceof Grade) 
			gradeMap.put(key, (Grade) baseModel);
		else if (baseModel instanceof Subject) 
			subjectMap.put(key, (Subject) baseModel);
		else 
			return;// error - not valid BaseModel
			
	}
	
}
