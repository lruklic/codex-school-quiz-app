package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Facebook authentication that translates into User.
 *
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "facebookAuth")
public class FacebookAuth extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "userId", unique = true)
	public Long userId;
	
	@OneToOne(cascade = {CascadeType.ALL})
	public User user;
	
	protected FacebookAuth() {
	}

	public FacebookAuth(Long userId, User user) {
		super();
		this.userId = userId;
		this.user = user;
	}

}
