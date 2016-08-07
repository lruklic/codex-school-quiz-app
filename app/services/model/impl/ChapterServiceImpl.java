package services.model.impl;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Chapter;
import services.model.ChapterService;

public class ChapterServiceImpl extends BaseModelServiceImpl<Chapter> implements ChapterService {

	protected ChapterServiceImpl() {
		super(Chapter.class);
	}

	@Override
	public Chapter findByName(String name) {
		Query query = JPA.em().createQuery("SELECT c FROM Chapter c WHERE c.name = :name", Chapter.class);
		query.setParameter("name", name);
		return (Chapter) singleResultOrNull(query);
		
	}
}
