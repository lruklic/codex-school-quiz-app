package services.model;

import models.FacebookAuth;

public interface FacebookAuthService extends BaseModelService<FacebookAuth> {

	public FacebookAuth findByUserId(long userID);
	
}
