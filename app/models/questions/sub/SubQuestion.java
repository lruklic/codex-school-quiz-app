package models.questions.sub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import models.BaseModel;

/**
 * Question that is part of another question. (e.g. chunk of text that student has to read and then
 * answer series of sub questions)
 * 
 * @author Luka Ruklić
 *
 */

@Entity
@Table(name = "sub_question")
@Inheritance(strategy=InheritanceType.JOINED)
public class SubQuestion extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "questionText", length = 500)
	public String questionText;
	
}
