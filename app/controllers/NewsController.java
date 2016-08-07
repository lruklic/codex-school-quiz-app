package controllers;

import models.Admin;
import models.Novelty;
import models.User;

import com.google.inject.Inject;

import constants.Constants;
import factories.NewsFactory;
import forms.NoveltyForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.NoveltyService;
import services.model.QuestionService;
import services.model.UserService;
import views.html.news.news_add;

/**
 * Controller that handles operations over questions.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class NewsController extends Controller {

	@Inject
	public static UserService userService;
	
	@Inject
	public static QuestionService questionService;
	
	@Inject
	public static NoveltyService noveltyService;
	
	public static Result add() {
		
		return ok(news_add.render(Form.form(NoveltyForm.class)));
		
	}
	
	public static Result submit() {
		Form<NoveltyForm> boundForm = Form.form(NoveltyForm.class).bindFromRequest();
		
		if (boundForm.hasErrors()) {
			// flash errors
			return badRequest(news_add.render(boundForm));
		}
		
		NoveltyForm noveltyForm = boundForm.get();
		
		String username = session().get(Constants.USERNAME);
		User user = userService.findByUsernameOrEmail(username);
		
		Novelty novelty = NewsFactory.createNews(noveltyForm, (Admin) user);		
		// extract user fetch to new static class and use it in every current user fetch occasion?
		
		noveltyService.save(novelty);
		
		return redirect(routes.AdminController.adminHome()); 
	}
	
	public static Result delete(Long id) {
		
		// TODO deadbolt or some other handler to disable attempts for non-admin users to change question values
		
		return ok();
		
	}
	
//	public static Result getChapters(String grade, String subject) {
//		// JsonNode json = Json.toJson(Chapter.getByGradeAndSubject(Grade.valueOf(grade), Subject.valueOf(subject)));
//		return ok();
//	}
	
}
