package forms;

import factories.NewsFactory;
import models.Admin;
import models.Novelty;
import models.enums.NewsPriority;
import models.enums.NewsType;
import play.data.validation.Constraints.Required;

public class NoveltyForm {
	
	@Required
	public String noveltyTitle;
	
	@Required
	public String noveltyText;
	
	@Required
	public NewsType newsType;
	
	@Required
	public NewsPriority newsPriority;
	
	public Novelty createNovelty(Admin admin) {
		return NewsFactory.createNews(this, admin);
		
	}

}
