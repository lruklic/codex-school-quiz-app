package services.model;

import models.Chapter;

public interface ChapterService extends BaseModelService<Chapter> {

	public Chapter findByName(String name);
	
}
