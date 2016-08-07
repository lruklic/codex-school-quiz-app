package controllers;

import models.ActivationLink;
import models.User;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import services.model.ActivationLinkService;
import services.model.UserService;

import com.google.inject.Inject;

/**
 * Controller that handles account activation.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class ActivationController extends Controller {
	
	@Inject
	public static UserService userService;
	
	@Inject
	public static ActivationLinkService activationLinkService;
	
	public static Result activate(String activationLink) {
		
		ActivationLink link = activationLinkService.findByLink(activationLink);
		
		User user = link.user;
		user.activated = true;
		
		userService.save(user);
		
		flash("success", Messages.get("success.user.activated"));
		return redirect(controllers.routes.LoginController.login());
	
	}

}
