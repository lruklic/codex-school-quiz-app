package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Class for unactivated accounts, contains activation link and matching account.
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "activationLink")
public class ActivationLink extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ActivationLink() {
	}
	
	@Column(name = "link")
	public String link;

	@OneToOne(cascade = {CascadeType.ALL})
	public User user;
	
	public ActivationLink(String link, User user) {
		this.link = link;
		this.user = user;
	}
	
	
}
