/** This is a service class for email sending.
 * 
 */
package com.ripon.service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;
	@Value("${spring.mail.username}")
	private String senderEmail;
	

	/**
	 * This method sends simple text based mail.
	 * 
	 * @param to      This is the destination email address.
	 * @param subject This is the subject of the email.
	 * @param text    This is main body text.
	 * @return Boolean true for successful email sending, otherwise returns false.
	 */
	@Async
	public void sendSimpleMessage(String to, String subject, String text) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(senderEmail);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			emailSender.send(message);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * This method sends email which contains attachment like pdf, jpeg, png etc.
	 * 
	 * @param to      This is the destination email address.
	 * @param subject This is the subject of the email.
	 * @param text    This is main body text.
	 * @return Boolean true for successful email sending, otherwise returns false.
	 */
	public boolean sendEmailWithAttachment(String to, String subject, String body) {

		try {
			MimeMessage message = emailSender.createMimeMessage();

			message.setFrom("riponoja9@gmail.com");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);

			// create MimeBodyPart object and set your message text
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(body);

			// create new MimeBodyPart object and set DataHandler object to this object
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			String file = "D://IMP DOC//RENT.pdf";// change accordingly
			DataSource source = new FileDataSource(file);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName("document.pdf");

			// create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);

			// set the multiplart object to the message object
			message.setContent(multipart);

			// 7) send message
			emailSender.send(message);

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	

}
