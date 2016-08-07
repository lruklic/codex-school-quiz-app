package services.model.impl;

import javax.persistence.Query;

import models.ActivationLink;
import play.db.jpa.JPA;
import services.model.ActivationLinkService;

public class ActivationLinkServiceImpl extends BaseModelServiceImpl<ActivationLink> implements ActivationLinkService {

	protected ActivationLinkServiceImpl() {
		super(ActivationLink.class);
	}

	@Override
	public ActivationLink findByLink(String link) {
		Query query = JPA.em().createQuery("SELECT al FROM ActivationLink al WHERE al.link = :link", ActivationLink.class);
		query.setParameter("link", link);
		return (ActivationLink) singleResultOrNull(query);
	}

}
