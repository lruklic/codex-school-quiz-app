package session;

import com.google.inject.Inject;

import models.User;
import constants.Constants;
import play.mvc.Controller;
import services.model.UserService;

/**
 * Wrapper class with static methods that manipulate session data.
 * 
 * @author Luka Ruklic
 *
 */

public class Session extends Controller {

	@Inject
	public static UserService userService;
	
	/**
	 * Getter for username in session.
	 * @return username
	 */
	public static String getUsername() {
		return session().get(Constants.USERNAME);
	}
	
	/**
	 * Getter for current user type.
	 * @return user type string
	 */
	public static String getUserType() {
		return session().get(Constants.USER_TYPE);
	}
	
	/**
	 * Method that adds username, first name and type of user to current session.
	 * @param 
	 */
	public static void addUserData(User user) {
		session(Constants.USERNAME, user.username);
		session(Constants.FIRST_NAME, user.firstName);
		session(Constants.USER_TYPE, user.userType.toString());
	}
	
	/**
	 * Clears session.
	 */
	public static void clear() {
		session().clear();
	}
	
}
