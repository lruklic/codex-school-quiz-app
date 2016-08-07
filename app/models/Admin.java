package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import models.enums.UserType;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name="id")
public class Admin extends User {

	/**
	 * Empty constructor, necessary for Hibernate.
	 */
	protected Admin() {		
	}
	
	public Admin(String username, String passwordHash, String firstName, String lastName, String email, Date createdOn) {
		super(username, passwordHash, firstName, lastName, email, createdOn);
		this.userType = UserType.ADMIN;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	@ElementCollection(targetClass = Subject.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "permissions")
	@Column(name="subjects")
	public List<Subject> subjectPermissions;
	
	@Column(name = "questions_added")
	public int questionsAdded;
	
	@Column(name = "clearanceLevel")
	public int clearanceLevel;			// TODO change clearanceLevel from int to some kind of enum; just a number is too vague

}
