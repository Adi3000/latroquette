package net.latroquette.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailBody {
	private static final Logger logger = LoggerFactory.getLogger(MailBody.class);

	private MailBody(){}
	
	public static final String HTML_VALIDATION_MAIL = getMailBody("verification-mail.html");
	public static final String HTML_RESET_PASSWORD_MAIL = getMailBody("reset-password-mail.html");

	private static final String getMailBody(String resource){
		InputStream in = MailBody.class.getResourceAsStream(resource);
		StringWriter out = new StringWriter();
		try {
			IOUtils.copy(in, out, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("Error will tryng to set mailBody of " + resource, e);
			return null;
		}
		return out.toString();
	}

}
