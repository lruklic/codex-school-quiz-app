package controllers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import models.Admin;
import models.FacebookAuth;
import models.Player;
import models.User;
import models.enums.UserType;

import org.apache.commons.codec.binary.Base64;

import play.Play;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.BodyParser;
import play.mvc.BodyParser.Json;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;
import security.PasswordHash;
import services.model.FacebookAuthService;
import services.model.UserService;
import session.Session;
import views.html.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import enums.AuthReply;
import forms.LoginForm;

/**
 * Controller that handles login attempts.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class LoginController extends Controller {

	@Inject
	public static UserService userService;
	
	@Inject
	public static FacebookAuthService facebookAuthService;

	/**
	 * Login method that renders form for user login. Mapped under GET in <i>routes</i>.
	 * 
	 * @return rendered login.scala.html view
	 */
	public static Result login() {
		
		String userType = Session.getUserType();
		if (userType == null) {
			return ok(login.render(Form.form(LoginForm.class), getRandomMottoNumber()));
		} else {
			return redirect(routes.StartController.redirect());
		}

	}

	/**
	 * Method that is called when User attempts to login using his/her Facebook account.
	 * 
	 * @return 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	@BodyParser.Of(Json.class)
	public static Result facebookAuthenticate() throws NoSuchAlgorithmException, InvalidKeyException {

		// TODO encapsulated in error catch
		RequestBody body = request().body();
		JsonNode js = body.asJson();
		
		String isConnected = js.get("status").asText();
		
		if (isConnected.equals("connected")) {
			
			JsonNode signedRequest = js.get("authResponse").get("signedRequest");
				
			JsonNode envelope = parseSignedRequest(signedRequest.asText(), Play.application().configuration().getString("fb.appSecret"), 3600);
			
			if (envelope == null) {
				return null; // TODO parse of signed request was unsuccessful, write error and repeat
			} else {
				Long userId = envelope.get("user_id").asLong();
				
				JsonNode name = js.get("name");
				
				FacebookAuth facebookAuth = facebookAuthService.findByUserId(userId);
				
				User user = null;
				if (facebookAuth == null) {		// prvi login
					user = new Player(userId.toString(), "some_random_hash", name.get("firstName").asText(), name.get("lastName").asText(), "", new Date(System.currentTimeMillis()));
					
					FacebookAuth auth = new FacebookAuth(userId, user);
					
					facebookAuthService.save(auth);
				
				} else {
					user = facebookAuth.user;
				}
				
				Session.clear();
				Session.addUserData(user);
				
				return ok();
				
			}

		}

		return null;
	}

	/**
	 * Logout method that clears current session with current user info. Mapped under GET in <i>routes</i>.
	 * 
	 * @return rendered login.scala.html view
	 */
	public static Result logout() {
		Session.clear();

		return login();
	}

	/**
	 * Authenticate method that checks user credentials after login attempt. Mapped under POST in <i>routes</i>.
	 * 
	 * @return login form is credentials are incorrect, user form otherwise
	 */
	public static Result authenticate() {
		Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			flash("error", Messages.get("login.error.values"));
			return login();
		} else {
			User user = userService.findByUsernameOrEmail(loginForm.get().usernameOrEmail);
			AuthReply passwordOk = passwordOk(user, loginForm.get().password);
			
			switch (passwordOk) {
			case LOGIN_OK:
					Session.clear();
					Session.addUserData(user);

					if (user.userType.equals(UserType.ADMIN)) {
						session("clearance", String.valueOf(((Admin) user).clearanceLevel));
						return redirect(routes.AdminController.adminHome());
					} else {
						return redirect(routes.PlayerController.playerHome());
					}
			case NO_USER:
				flash("error", Messages.get("login.error.username"));
				break;
			case WRONG_PASSWORD:
				flash("error", Messages.get("login.error.password"));
				break;
			case NOT_ACTIVATED:
				flash("error", Messages.get("error.user.activated"));
				break;
			}	
			
			return badRequest(login.render(Form.form(LoginForm.class), 0));

		}
	}

	/**
	 * Password that checks if provided username exists and if <code>inputPassword</code> matches provided password.
	 * 
	 * @param inputUsername
	 *            username input from form
	 * @param inputPassword
	 *            password input from form
	 * @return AuthReply that describes if authentication credentials were correct
	 */
	private static AuthReply passwordOk(User user, String inputPassword) {

		if (user == null) {
			return AuthReply.NO_USER;
		} else {
			if (!user.activated) {
				return AuthReply.NOT_ACTIVATED;
			}
			
			if (PasswordHash.validatePassword(inputPassword, user.passwordHash)) {
				return AuthReply.LOGIN_OK;
			}
		}

		return AuthReply.WRONG_PASSWORD;
	}
	
	private static JsonNode parseSignedRequest(String signedRequest, String secret, int maxAge) {
		
		String[] split = signedRequest.split("[.]", 2);
		
		String encodedSignature = split[0];
		String encodedEnvelope = split[1];
		
		try {
			String envelope = new String(new Base64(true).decode(encodedEnvelope));
			
			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode envelopeNode = null;

			envelopeNode = mapper.readTree(envelope);
			
			String algorithm = envelopeNode.get("algorithm").asText();
			
		    if (!algorithm.equals("HMAC-SHA256")) {
		        throw new Exception("Invalid request. (Unsupported algorithm.)");
		    }

		    if ((envelopeNode.get("issued_at").asLong()) < System.currentTimeMillis() / 1000 - maxAge) {
		        throw new Exception("Invalid request. (Too old.)");
		    }
			
			byte[] key = secret.getBytes();
			SecretKey hmacKey = new SecretKeySpec(key, "HMACSHA256");
			Mac mac = Mac.getInstance("HMACSHA256");
		    mac.init(hmacKey);
		    byte[] digest = mac.doFinal(encodedEnvelope.getBytes());
			
		    if (!Arrays.equals(new Base64(true).decode(encodedSignature), digest)) {
		        throw new Exception("Invalid request. (Invalid signature.)");
		    }
		    
		    return envelopeNode;
		    
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Returns random number that represents one of the viable mottos in messages file.
	 * 
	 * @return motto number
	 */
	public static Integer getRandomMottoNumber() {
		Random rand = new Random();
	    int mottoNumber = rand.nextInt(4) + 1;	// TODO number of viable mottos from messages.hr
	    
	    return mottoNumber;
	}

}
