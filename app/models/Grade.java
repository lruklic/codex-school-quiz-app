package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "grade")
public class Grade extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Grade() {
	}
	
	@Column(name = "name")
	public String name;

	public Grade(String name) {
		this.name = name;
	}
	
}
