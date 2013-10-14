package de.devsurf.echo.sync.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

import com.google.common.base.Strings;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.common.lang.obfuscation.ObfuscatedString;

public class Mailing {

	@InjectLogger
	private Logger logger;

	private MailConfiguration configuration;

	@Inject
	public void initialize(MailConfiguration configuration)
			throws MailConfigurationException {
		this.configuration = configuration;
		// at least SMTP server and port must be set
		if (Strings.isNullOrEmpty(this.configuration.server)) {
			throw new MailConfigurationException("No SMTP server is configured");
		}

		if (this.configuration.port <= 0) {
			throw new MailConfigurationException("No SMTP port is configured");
		}
	}

	private Session buildSessionConfig() throws MailConfigurationException {
		Properties sessionProperties = new Properties();
		if (configuration.useSSL) {
			sessionProperties.putAll(configuration.auth.options);
		}

		UsernamePasswordAuthenticator auth = null;
		if (configuration.authenticationRequired) {
			auth = new UsernamePasswordAuthenticator(configuration.username,
					configuration.password.deobfuscate());
			sessionProperties.put("mail.smtp.auth", "true");
		}

		sessionProperties.put("mail.smtp.host", configuration.server);
		sessionProperties.put("mail.smtp.port", configuration.port);

		return Session.getInstance(sessionProperties, auth);
	}

	/**
	 * Sends a mail by the given parametes.
	 * 
	 * @param senderEMail
	 *            the sender's mail address. May not be null or empty.
	 * @param senderName
	 *            the sender's name. May not be null or empty.
	 * @param to
	 *            the receiver's mail address. May not be null or empty.
	 * @param subject
	 *            the mail subject. May not be null or empty.
	 * @param text
	 *            the mail body. May not be null or empty.
	 * 
	 * @throws MessagingException
	 *             if the mail could not be sent.
	 * @throws java.io.UnsupportedEncodingException
	 *             if the mail could not be sent due to an EncodingException.
	 * @throws MailConfigurationException
	 *             if this instance was not initialized properly.
	 */
	public void sendMail(String senderEMail, String senderName, String to,
			String subject, String text) throws MessagingException,
			UnsupportedEncodingException, MailConfigurationException {
		// sanity checks
		checkArgument(senderEMail != null && senderEMail.length() > 0,
				"sender email was not specified [%s]", senderEMail);
		checkArgument(senderName != null && senderName.length() > 0,
				"sender name was not specified [%s]", senderName);
		checkArgument(to != null && to.length() > 0,
				"recipient was not specified [%s]", to);
		checkArgument(subject != null && subject.length() > 0,
				"subject was not specified [%s]", subject);
		checkArgument(text != null && text.length() > 0,
				"text was not specified [%s]", text);

		Session session = buildSessionConfig();

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(senderEMail, senderName));
		msg.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(to, false));
		msg.setSubject(subject);
		msg.setContent(text, "text/html; charset=utf-8");
		msg.setSentDate(new Date());

		Transport t = session.getTransport("smtp");
		try {
			t.connect();
			t.sendMessage(msg, msg.getAllRecipients());
		} finally {
			t.close();
		}
	}

	public static class MailConfigurationException extends Exception {
		private static final long serialVersionUID = -6189064492668378608L;

		public MailConfigurationException(String message) {
			super(message);
		}
	}

	public static class UsernamePasswordAuthenticator extends Authenticator {
		private String user;
		private String password;

		public UsernamePasswordAuthenticator(final String user,
				final String password) {
			this.user = user;
			this.password = password;
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.user, this.password);
		}
	}

	public static class MailConfiguration {
		public static final String PROTOCOL_SMTP = "smtp";

		protected int port;
		protected String server;
		protected String username;
		protected ObfuscatedString password;
		protected boolean useSSL;
		protected boolean authenticationRequired;
		protected TransportSecurity auth = TransportSecurity.NONE;

		public static MailConfiguration google() {
			MailConfiguration config = new MailConfiguration();
			config.server = "smtp.gmail.com";
			config.port = 587;
			config.useSSL = true;
			config.authenticationRequired = true;
			config.auth = TransportSecurity.TLS;

			return config;
		}

		public void setPassword(String password) {
			this.password = ObfuscatedString.obfuscate(password);
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public static enum Protocol {
			SMTP("smtp"), SMTPS("smtps");
			private String value;

			Protocol(String value) {
				this.value = value;
			}

			public String value() {
				return value;
			}
		}

		public static enum TransportSecurity {
			NONE(Collections.<String, String> emptyMap()), SSL(Collections
					.<String, String> emptyMap()), TLS(Collections
					.singletonMap("mail.smtp.starttls.enable", "true"));
			private Map<String, String> options;

			TransportSecurity(Map<String, String> options) {
				this.options = options;
			}

			public Map<String, String> options() {
				return options;
			}
		}
	}
}
