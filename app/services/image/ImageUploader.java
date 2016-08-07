package services.image;

import java.io.File;

/**
 * Class that implements methods for image upload, since images are kept on different location than the database.
 * 
 * @author Luka Ruklic
 *
 */

public interface ImageUploader {

	/**
	 * Method that uploads image to predefined location/server.
	 * 
	 * @param imageFile image file
	 * @param newImageName name under which will image be stored
	 */
	public void uploadImage(String subjectName, File imageFile, String newImageName);
	
	/**
	 * Method that deletes image from file system. Used when question containing image is deleted.
	 * 
	 * @param imageName 
	 */
	public void deleteImage(String subjectName, String imageName);
	
}
