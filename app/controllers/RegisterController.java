package controllers;

import java.util.Date;
import java.util.UUID;

import models.ActivationLink;
import models.Admin;
import models.Player;
import models.enums.UserType;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import security.PasswordHash;
import services.EmailService;
import services.model.ActivationLinkService;
import services.model.UserService;
import views.html.register.register;

import com.google.inject.Inject;

import forms.RegisterForm;

/**
 * Controller that handles user registration.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class RegisterController extends Controller {

	@Inject
	public static UserService userService;

	@Inject
	public static ActivationLinkService activationLinkService;

	@Inject
	public static EmailService emailService;

	/**
	 * Renders user registration form.
	 * @return user registration form
	 */
	public static Result register() {
		return ok(register.render(Form.form(RegisterForm.class)));
	}
	
	/**
	 * Submits registration data that user filled.
	 * @return
	 */
	public static Result submit() {
		Form<RegisterForm> registerForm = Form.form(RegisterForm.class).bindFromRequest();

		// Validation
		if (registerForm.hasErrors()) {
			// validate email, validate passwords
			return badRequest(register.render(registerForm));
		}

		RegisterForm rf = registerForm.get();

		if (userService.findByUsername(rf.username) != null || userService.findByEmail(rf.email) != null) {
			// flash username or email already exists // TODO add activation mail resend option
			return badRequest(register.render(registerForm));
		}

		// If user registered as admin, account is created, but SU must activate it manually
		// TODO add preferences field? CV field?
		if (rf.userType.equals(UserType.ADMIN)) {
			Admin admin = new Admin(rf.username, PasswordHash.createHash(rf.password), rf.firstName, rf.lastName, rf.email, new Date(System.currentTimeMillis()));
			userService.save(admin);
			flash("success", Messages.get("register.admin.success"));
			return redirect(controllers.routes.LoginController.login());
		} else if (rf.userType.equals(UserType.PLAYER)) {
			
			Player player = new Player(rf.username, PasswordHash.createHash(rf.password), rf.firstName, rf.lastName, rf.email, new Date(System.currentTimeMillis()));
			
			// Create activation link
			ActivationLink activationLink = new ActivationLink(UUID.randomUUID().toString(), player);
			
			// Send email with activation link
			emailService.sendEmail(player.email, Messages.get("email.player.welcome.subject"), createEmailContent(activationLink));
			
			// Upload activation link and user on cascade to DB
			activationLinkService.save(activationLink);
			
			flash("success", Messages.get("register.player.success"));

			return redirect(controllers.routes.LoginController.login());
		}

		return ok();
	}

	private static String createEmailContent(ActivationLink activationLink) {

		StringBuilder sb = new StringBuilder();
		sb.append(Messages.get("email.player.welcome.content"));
		sb.append("\n\n");
		sb.append("http://localhost:9000/activation/" + activationLink.link);

		return sb.toString();
	}

}
