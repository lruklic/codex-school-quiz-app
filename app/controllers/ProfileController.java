package controllers;

import models.User;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.google.inject.Inject;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.UserService;
import session.Session;
import views.html.profile.profile;

/**
 * Controller that handles user profile page.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class ProfileController extends Controller {

	@Inject
	public static UserService userService;

	@SubjectPresent
	public static Result profileHome() {
		
		User currentUser = userService.findByUsernameOrEmail(Session.getUsername());
		
		return ok(profile.render(currentUser));
	}

}
