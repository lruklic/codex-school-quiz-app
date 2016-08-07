package services.model;

import models.Grade;

public interface GradeService extends BaseModelService<Grade> {

	public Grade findByName(String name);
	
}
