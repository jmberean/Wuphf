package com.pkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.fax4j.*;
import org.fax4j.FaxJob.FaxJobPriority;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/*
 * useful links
 * 
 * https://theoffice.fandom.com/wiki/WUPHF.com_(Website)
 * https://www.digitaltrends.com/mobile/how-to-send-a-text-from-your-email-account/
 * https://sourceforge.net/projects/fax4j/
 * https://github.com/sagiegurari/fax4j
 * https://faxauthority.com/how-to-send-a-fax/
 * https://twitter4j.org/en/index.html
 * https://github.com/Twitter4J/Twitter4J
 * https://twitter4j.org/javadoc/index.html
 * https://github.com/Twitter4J/Twitter4J/blob/master/twitter4j-examples/src/main/java/twitter4j/examples/directmessage/SendDirectMessage.java
 * https://twitter4j.org/en/code-examples.html
 * https://developer.twitter.com/en/docs/platform-overview
 * https://developers.facebook.com/docs/instagram/oembed
 * 
 */

public class Wuphf {
	
	public static void main(String[] args) {
		start();
	}
	
	public static void start() {
		
		System.out.println("Wuphf starting.");
		
		String emailRecipient = "jmberean@gmail.com";
		String smsRecipient = "8457090867";
		String smsCarrier = "verizon";
		String faxNumber = "555-555";
		String faxRecipientName = "John";
		String twitterUsername = "GohanSun69";
		String msg = "Hi john";
		
		try {
			sendSms(smsRecipient,smsCarrier,msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sendEmail(emailRecipient,msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sendFax(faxNumber, msg,faxRecipientName);
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sendTwitterDM(twitterUsername, msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public static void sendSms(String smsRecipient,String smsCarrier, String msg) throws AddressException, MessagingException {
		
		String carrierDomain = "";
		
		if(smsCarrier.equals("verizon")) {
			carrierDomain = "@vtext.com";
		} else {
			return;
		}
		
		leverageEmailReq(smsRecipient+carrierDomain,msg);
		System.out.println("SMS message sent successfully...");

	}
	
	public static void sendEmail(String emailRecipient, String msg) throws AddressException, MessagingException {
		leverageEmailReq(emailRecipient,msg);
		System.out.println("Email sent successfully...");

	}
	
	public static void leverageEmailReq(String recipient,String msg) throws AddressException, MessagingException {
		String host = "smtp.gmail.com";
		final String user = "WuphfCommunication";// change accordingly
		final String password = "Z8`$j7N!L9g_`YeG";// change accordingly

		String recipientLocal = recipient;// change accordingly

		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "587");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// Compose the message

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(user));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientLocal));
		message.setSubject("Wuphf");
		message.setText(msg);

		// send the message
		Transport.send(message);

	}
	
	public static void sendFax(String faxNumber, String msg, String faxRecipientName) throws FileNotFoundException {
		
		PrintWriter out = new PrintWriter("faxText.txt");
		out.println(msg);
		out.close();
		
		//get new instance of a fax client (based on internal + external fax4j.properties file data)
		FaxClient faxClient=FaxClientFactory.createFaxClient();

		//create a new fax job
		FaxJob faxJob=faxClient.createFaxJob();

		//set fax job values
		faxJob.setFile(new File("faxText.txt"));
		faxJob.setPriority(FaxJobPriority.MEDIUM_PRIORITY);
		faxJob.setTargetAddress(faxNumber);
		faxJob.setTargetName(faxRecipientName);
		faxJob.setSenderEmail("WuphfCommunication@gmail.com");
		faxJob.setSenderName("Wuphf");

		//submit fax job
		faxClient.submitFaxJob(faxJob);

		//print submitted fax job ID (may not be supported by all SPIs)
		System.out.println("Fax job submitted, ID: "+faxJob.getID());
	}
	
	public static void sendTwitterDM(String recipientId, String msg) throws TwitterException {
		// The factory instance is re-useable and thread safe.
	    Twitter sender = TwitterFactory.getSingleton();
	    sender.sendDirectMessage(recipientId, msg);
	    System.out.println("Sent: " + msg + " to @" + recipientId);
	}
}
