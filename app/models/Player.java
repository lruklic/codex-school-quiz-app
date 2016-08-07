package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import models.enums.UserType;

@Entity
@Table(name = "player")
@PrimaryKeyJoinColumn(name="id")
public class Player extends User {
	
	/**
	 * Empty constructor, necessary for Hibernate.
	 */
	protected Player() {
	}
	
	public Player(String username, String passwordHash, String firstName, String lastName, String email, Date createdOn) {
		super(username, passwordHash, firstName, lastName, email, createdOn);
		this.userType = UserType.PLAYER;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "games_played")
	public int gamesPlayed;

}
