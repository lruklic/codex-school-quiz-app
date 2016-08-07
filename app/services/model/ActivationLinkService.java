package services.model;

import models.ActivationLink;

/**
 * Interface that defines methods for manipulation with ActivationLink objects.
 * 
 * @author Luka Ruklic
 *
 */

public interface ActivationLinkService extends BaseModelService<ActivationLink> {
	
	public ActivationLink findByLink(String link);

}
