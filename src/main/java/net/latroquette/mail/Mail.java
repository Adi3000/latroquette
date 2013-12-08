package net.latroquette.mail;

import java.io.UnsupportedEncodingException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.InternetAddress;

public enum Mail {
	VALIDATION(
			MailUtil.NO_REPLY_SENDER, 
			MailUtil.RESOURCE_BUNDLE.getString("mail.verify_mail.subject"), 
			MailUtil.getMailBody(MailUtil.RESOURCE_BUNDLE.getString("mail.verify_mail.body"))),
	RESET_PASSWORD(
			MailUtil.NO_REPLY_SENDER,
			MailUtil.RESOURCE_BUNDLE.getString("mail.reset_password.subject"), 
			MailUtil.getMailBody(MailUtil.RESOURCE_BUNDLE.getString("mail.reset_password.body")));
	
	private InternetAddress sender;
	private final String subject;
	private final String body;
	private Mail(String sender,String subject, String body){
		this.sender = null;
		try {
			this.sender = new InternetAddress(sender, MailUtil.NO_REPLY_SENDER, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalCharsetNameException(StandardCharsets.UTF_8.name());
		}
		this.subject = subject;
		this.body = body;
	}
	/**
	 * @return the sender
	 */
	public InternetAddress getSender() {
		return sender;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
}
