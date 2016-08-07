package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import constants.Constants;

/**
 * Model that serves as mark for question image stored on file server.
 * 
 * @author Luka Ruklic
 *
 */

@Entity
@Table(name = "image")
public class Image extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected Image() {		
	}
	
	public Image(String filePath) {
		super();
		this.filePath = filePath;
	}

	@Column(name = "filePath")
	public String filePath;
	
	@Column(name = "height")
	public int height;
	
	@Column(name = "width")
	public int width;
	
	public int getSuggestedHeight() {
		return Constants.SUGGESTED_BASE_HEIGHT;
	}
	
	public int getSuggestedWidth() {
		float ratio = ((float) width) / ((float) height);
		return (int) (Constants.SUGGESTED_BASE_HEIGHT * ratio);
	}

}
