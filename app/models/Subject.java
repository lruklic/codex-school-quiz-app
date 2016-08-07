package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Subject() {
	}
	
	@Column(name = "name")
	public String name;

	public Subject(String name) {
		this.name = name;
	}
	
	
}
