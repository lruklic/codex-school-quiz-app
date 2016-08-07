package controllers;

import models.Admin;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import cache.models.ModelCache;

import com.google.inject.Inject;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.NoveltyService;
import services.model.QuestionService;
import services.model.UserService;
import session.Session;
import views.html.admin.admin_question;
import views.html.admin.admin_home;
import views.html.admin.admin_questionlist;
import forms.QuestionForm;

/**
 * Controller that handles admin page.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class AdminController extends Controller {

	@Inject
	public static QuestionService questionService;
	
	@Inject
	public static NoveltyService noveltyService;
	
	@Inject
	public static UserService userService;
	
	public static Result adminList() {
		
		Admin currentAdmin = (Admin) userService.findByUsernameOrEmail(Session.getUsername());
		
		return ok(admin_questionlist.render(questionService.getQuestionsBySubjects(currentAdmin.subjectPermissions)));
		
		// return ok(admin_questionlist.render(questionService.findAll()));
	}
	
	/**
	 * Login method that renders form for user login. Mapped under GET in <i>routes</i>.
	 * 
	 * @return rendered login.scala.html view
	 */
	public static Result adminQuestion() {
		
		Admin currentAdmin = (Admin) userService.findByUsernameOrEmail(Session.getUsername());
		
		return ok(admin_question.render(Form.form(QuestionForm.class), currentAdmin.subjectPermissions, ModelCache.getInstance().getAllChapters(), ModelCache.getInstance().getAllGrades()));
	}
	
	/**
	 * Login method that renders html for admin home page. Mapped under GET in <i>routes</i>
	 * @return rendered admin_home.scala.html view
	 */
	@Restrict(@Group("ADMIN"))
	public static Result adminHome() {
		String clearanceString = session().get("clearance");
		
		Integer clearance = 0;
		try {
			clearance = Integer.parseInt(clearanceString);
		} catch (NumberFormatException ex) {
			// no session clearance
		}
		return ok(admin_home.render(clearance, noveltyService.findAll())); // TODO instead of findAll, only for admins
	}

}
