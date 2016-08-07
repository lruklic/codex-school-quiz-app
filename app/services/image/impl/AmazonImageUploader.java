package services.image.impl;

import java.io.File;

import play.Play;
import services.image.ImageUploader;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * Class that implements methods for image upload to Amazon server.
 * 
 * @author Luka Ruklic
 *
 */

public class AmazonImageUploader implements ImageUploader {
	
	@Override
	public void uploadImage(String subjectName, File imageFile, String newImageName) { 
	
		AWSCredentials credentials = new BasicAWSCredentials(
				Play.application().configuration().getString("amazon.accessKey"), 
				Play.application().configuration().getString("amazon.secretKey"));
		
		AmazonS3 s3client = new AmazonS3Client(credentials);
		
		String imageBucketName = Play.application().configuration().getString("amazon.imageBucketName");
		String filePath = "images/" + subjectName + "/" + newImageName;	

		s3client.putObject(new PutObjectRequest(imageBucketName, filePath, imageFile).withCannedAcl(CannedAccessControlList.PublicRead));	
		
	}

	@Override
	public void deleteImage(String subjectName, String imageName) {
		
		AWSCredentials credentials = new BasicAWSCredentials(
				Play.application().configuration().getString("amazon.accessKey"), 
				Play.application().configuration().getString("amazon.secretKey"));
		
		AmazonS3 s3client = new AmazonS3Client(credentials);
		
		String imageBucketName = Play.application().configuration().getString("amazon.imageBucketName");
		
		s3client.deleteObject(imageBucketName, "images/" + subjectName + "/" + imageName);
		
	}

}
