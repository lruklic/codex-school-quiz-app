package services.impl;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import play.Play;
import services.EmailService;

/**
 * Implementation of email service, using GMail smtp as default.
 * 
 * @author Luka Ruklic
 *
 */

public class EmailServiceImpl implements EmailService {
	
	@Override
	public void sendEmail(String recepientAddress, String subject, String messageContent) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", Play.application().configuration().getString("mail.smtp.host"));
		props.put("mail.smtp.port", Play.application().configuration().getString("mail.smtp.port"));
		
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(
					Play.application().configuration().getString("mail.address"),
					Play.application().configuration().getString("mail.password")
				);
			}
		});
		
		try {
			MimeMessage message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(Play.application().configuration().getString("mail.address")));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientAddress));
			message.setSubject(subject);
			message.setText(messageContent);
			
			Transport.send(message);
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
	
	
}
