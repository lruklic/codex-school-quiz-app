package enums;

/**
 * Reply token which LoginController uses to specify login attempt result.
 * 
 * @author Luka Ruklic
 *
 */
public enum AuthReply {
	
	/**
	 * User with that name doesn't exist in database.
	 */
	NO_USER, 
	/**
	 * User with that name exists, but the password is wrong.
	 */
	WRONG_PASSWORD, 
	/**
	 * User credentials are ok.
	 */
	LOGIN_OK, 
	/**
	 * User account is not activated.
	 */
	NOT_ACTIVATED

}
