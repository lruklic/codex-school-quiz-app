package services.model.impl;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Subject;
import services.model.SubjectService;

public class SubjectServiceImpl extends BaseModelServiceImpl<Subject> implements SubjectService {

	protected SubjectServiceImpl() {
		super(Subject.class);
	}

	@Override
	public Subject findByName(String subjectName) {
		Query query = JPA.em().createQuery("SELECT s FROM Subject s WHERE s.name = :name", Subject.class);
		query.setParameter("name", subjectName);
		return (Subject) singleResultOrNull(query);
	}

}
