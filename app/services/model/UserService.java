package services.model;

import models.User;

/**
 * Interface that defines methods for CRUD (Create, Read, Update, Delete) operations on user data in database.
 * 
 * @author Luka Ruklic
 *
 */

public interface UserService extends BaseModelService<User> {

	/**
	 * Find user by email. Every email is unique.
	 * 
	 * @param email  
	 * @return
	 */
	public User findByEmail(String email);
	
	public User findByUsername(String username);
	
	public User findByUsernameOrEmail(String credential);
}
