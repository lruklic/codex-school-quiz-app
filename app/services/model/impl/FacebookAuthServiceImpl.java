package services.model.impl;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.FacebookAuth;
import services.model.FacebookAuthService;

public class FacebookAuthServiceImpl extends BaseModelServiceImpl<FacebookAuth> implements FacebookAuthService {

	protected FacebookAuthServiceImpl() {
		super(FacebookAuth.class);
	}

	@Override
	public FacebookAuth findByUserId(long userID) {
		Query query = JPA.em().createQuery("SELECT f FROM FacebookAuth f WHERE f.userId = :userId", FacebookAuth.class);
		query.setParameter("userId", userID);
		return (FacebookAuth) singleResultOrNull(query);
		
	}

}
