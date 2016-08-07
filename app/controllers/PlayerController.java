package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.google.inject.Inject;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.NoveltyService;
import services.model.QuestionService;
import views.html.player.player_home;

/**
 * Controller that handles player page.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class PlayerController extends Controller {

	@Inject
	public static QuestionService questionService;
	
	@Inject
	public static NoveltyService noveltyService;

	@SubjectPresent
	public static Result playerHome() {
		return ok(player_home.render(LoginController.getRandomMottoNumber()));
	}

}
