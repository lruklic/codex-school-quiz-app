package services.model.impl;

import javax.persistence.Query;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import services.model.UserService;
import models.User;

/**
 * Implementation of user service that contains overriden methods for finding user in DB
 * by their username, email or both.
 * 
 * @author Luka Ruklic
 *
 */

@Transactional
public class UserServiceImpl extends BaseModelServiceImpl<User> implements UserService {

	protected UserServiceImpl() {
		super(User.class);
	}

	@Override
	public User findByUsername(String username) {
		Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
		query.setParameter("username", username);
		return (User) singleResultOrNull(query);
		
	}

	@Override
	public User findByEmail(String email) {
		Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
		query.setParameter("email", email);
		return (User) singleResultOrNull(query);
	}

	@Override
	public User findByUsernameOrEmail(String credential) {
		Query query = JPA.em().createQuery("SELECT u FROM User u WHERE u.email = :email OR u.username = :username", User.class);
		query.setParameter("username", credential);
		query.setParameter("email", credential);
		return (User) singleResultOrNull(query);
	}
	
}
