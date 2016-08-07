package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.EmailService;
import services.PortService;
import services.model.QuestionService;
import services.model.UserService;
import session.Session;
import views.html.profile.profile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import models.Question;
import models.User;

/**
 * Controller that handles question import and export.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class PortController extends Controller {

	@Inject
	public static QuestionService questionService;
	
	@Inject
	public static UserService userService;

	@Inject
	public static EmailService emailService;
	
	@Inject
	public static PortService portService;
	
	@Restrict(@Group("ADMIN"))
	public static Result portHome() {
		
		User currentUser = userService.findByUsernameOrEmail(Session.getUsername());
		
		return ok(profile.render(currentUser));
	}
	
	public static Result importQuestions() {
		
		// Get uploaded file
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file = body.getFile("import-input");
		
		Map<Integer, String> importResults = portService.importQuestions(file.getFile());
		
		String mapAsJson = null;
		try {
			mapAsJson = new ObjectMapper().writeValueAsString(importResults);
		} catch (JsonProcessingException e) {
			// bad import results
		}
		
		return ok(mapAsJson);
		
	}
	
	@Restrict(@Group("ADMIN"))
	public static Result exportQuestions(String exportType) throws IOException {
		
		byte[] output = null;
		
		List<Question> questionList = questionService.findQuestionsByAdmin(Session.getUsername());
		
		// return error if user has no questions
		
		if (exportType.equals("csv")) {
			output = portService.exportAsCSV(questionList);
		} else if (exportType.equals("xls")) {
			output = portService.exportAsXLS(questionList);
		} else {
			return TODO;
		}
		
		response().setContentType("application/x-download");
		response().setHeader("Content-disposition", "attachment; filename=questionList."+exportType);
		return ok(output);
				
	}

}
