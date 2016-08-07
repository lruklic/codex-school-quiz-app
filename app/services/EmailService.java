package services;

public interface EmailService {

	public void sendEmail(String recepientAddress, String subject, String messageContent);
	
}
