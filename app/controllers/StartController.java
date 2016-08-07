package controllers;

import com.google.inject.Inject;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.QuestionService;
import session.Session;

/**
 * Controller that is called when user attempts to access /, top application domain (i.e. localhost:9000/).
 * It redirect him to login if no session attributes are present or to corresponding site if user is 
 * already logged in.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class StartController extends Controller {
	
	@Inject
	public static QuestionService questionService;
	
	public static Result redirect() {
		
		String type = Session.getUserType();
		
		if(type == null) {
			return redirect(controllers.routes.LoginController.login());
		} else {
			switch(type) {
			case "ADMIN":
				// TODO unauthorized attempt?
				// error with session().get("clearance")
				return redirect(routes.AdminController.adminHome());
				// TODO instead of findAll send news by type and priority
			case "PLAYER":
				return redirect(routes.PlayerController.playerHome());
			}
		}
		
		return ok();
		
	}
	
}
