package services.model.impl;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Grade;
import services.model.GradeService;


public class GradeServiceImpl extends BaseModelServiceImpl<Grade> implements GradeService {

	protected GradeServiceImpl() {
		super(Grade.class);
	}
	
	@Override
	public Grade findByName(String name) {
		Query query = JPA.em().createQuery("SELECT g FROM Grade g WHERE g.name = :name", Grade.class);
		query.setParameter("name", name);
		return (Grade) singleResultOrNull(query);
		
	}

}
