package net.latroquette.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.latroquette.common.database.data.profile.User;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailUtil {
	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private MailUtil(){}
	
	private static final String BUNDLE_NAME = MailUtil.class.getPackage().getName() + ".mails";
	static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME); 
	private static final String SMTP_SERVER = RESOURCE_BUNDLE.getString("mail.smtp.host");
	static final String DISPLAY_NAME_SENDER = RESOURCE_BUNDLE.getString("mail.display_name");
	public static final String NO_REPLY_SENDER = RESOURCE_BUNDLE.getString("mail.no_reply.mail");
	static final String SUPPORT_SENDER = RESOURCE_BUNDLE.getString("mail.support.mail");
	private static final Session SESSION = getMailSession();

	static final String getMailBody(String resource){
		InputStream in = MailUtil.class.getResourceAsStream(resource);
		StringWriter out = new StringWriter();
		try {
			IOUtils.copy(in, out, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("Error will tryng to set mailBody of " + resource, e);
			return null;
		}
		return out.toString();
	}

	private static Session getMailSession(){
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", SMTP_SERVER);
		Session session = Session.getDefaultInstance(properties);
		return session;
	}
	
	public static void sendMailToUser(User user, Mail mail, Serializable... args) throws MailException{
		List<Serializable> arguments = new ArrayList<>(args.length + 1); 
		arguments.add(user.getLogin());
		arguments.addAll(Arrays.asList(args));
		try{
			Message message = new MimeMessage(SESSION);
			message.setFrom(mail.getSender());
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail(), user.getLogin()));
			message.setSubject(String.format(mail.getSubject(),arguments.toArray()));
			message.setContent(String.format(mail.getBody(),arguments.toArray()), "text/html; charset=utf-8");
			message.setSentDate(new Date());
			Transport.send(message);
		}catch(MessagingException | UnsupportedEncodingException me){
			logger.error("Unable to send a mail " + mail+ " from "+ mail.getSender().getAddress() + " to " + user.getMail() , me );
			throw new MailException("Unable to send a mail " + mail+ " from "+ mail.getSender().getAddress() + " to " + user.getMail() , me );
		}
	}
}
