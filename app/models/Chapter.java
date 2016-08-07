package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chapter")
public class Chapter extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Chapter() {		
	}
	
	@Column(name = "name")
	public String name;

	@ManyToOne
	public Grade grade;
	
	@ManyToOne
	public Subject subject;

	public Chapter(String name, Grade grade, Subject subject) {
		this.name = name;
		this.grade = grade;
		this.subject = subject;
	}
	
}
