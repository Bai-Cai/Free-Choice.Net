package net.freechoice.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.freechoice.service.WebMailService.ContactMessage;
import net.freechoice.util.StrUtil;

/**
 * @author BowenCai
 * 
 */
public class MailSender {


	public MailSender() {
		
		adapter = new AddressAdapter();
		
		sessionConfig = new Properties();
		
		sessionConfig.put("mail.transport.protocol", "smtp");
		sessionConfig.put("mail.smtp.starttls.enable", "true");
		sessionConfig.put("mail.smtp.auth", "true");
		sessionConfig.put("mail.imap.connectiontimeout", 500);
		sessionConfig.put("mail.imap.timeout", 500);
	}
	/**
	 *  javax.mail.Session is thread saft
	 */
	Session 	session  = null;
	Properties 	sessionConfig = null;
	// ---------------------------------------------------------
	// to be injected
	// ---------------------------------------------------------
	public String 			senderAddr;
	public AddressAdapter 	adapter;
	public String 			senderPsw;
	public boolean 			debugMode = false;

	/**
	 * mail content template
	 */
	public String			template;

	public void setSenderHostName(String senderHostName) {
		sessionConfig.put("mail.smtp.host", senderHostName);
	}

	public void setSenderAddr(String senderAddr) {
		sessionConfig.put("mail.smtp.user", senderAddr);
		this.senderAddr = senderAddr;
	}

	public void setTargetList(ArrayList<String> targets) throws AddressException {
		adapter.adapte(targets);
	}
	
	public void setSenderPsw(String senderPsw) {
		sessionConfig.put("mail.smtp.password", senderPsw);
		this.senderPsw = senderPsw;
	}

	public void setPort(int port) {
		sessionConfig.put("mail.smtp.port", port);
	}
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	private Session getSession(boolean getNew) {
		
		Authenticator pswAuth = new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderAddr, senderPsw);
			}
		};
		if (getNew) {
			session = Session.getInstance(sessionConfig, pswAuth);
		} else {
			if (session == null) {
				session = Session.getInstance(sessionConfig, pswAuth);
			}
		}
		session.setDebug(debugMode);
		return session;
	}
	
	@SuppressWarnings("unused")
	private Session getSession() {
		return getSession(true);
	}


	public boolean send(final String name, 
						final String senderAdd,
						final String subject, 
						final String content) {

		session = getSession(false);
		MimeMessage message = new MimeMessage(session);
		try {
			message.addRecipients(RecipientType.TO, adapter.getAddresses());
			
			message.setFrom(new InternetAddress(senderAddr));
			message.setSubject(subject);
			
			message.setContent(
					StrUtil.format(template, senderAdd, name, subject, content)
						.toString()
					,"text/plain");
			
			Transport.send(message, message.getAllRecipients());
			
		} catch (MessagingException e) {
			if (debugMode) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}
	
	/**
	 * if one message failed to be sent, return it back
	 */
	List<ContactMessage>	unsendMsg = null;
	
	public void beginTransaction() {
		this.session = getSession(true);
	}
	
	public void dispatch(ContactMessage msg) {
		
		MimeMessage message = new MimeMessage(session);
		try {
			message.addRecipients(RecipientType.TO, adapter.getAddresses());
			
			message.setFrom(new InternetAddress(senderAddr));
			message.setSubject(msg.subject);
			
			

			message.setContent(
				StrUtil.format(template, msg.name, msg.email, msg.subject, msg.content)
						.toString()
					,"text/plain");
		
			Transport.send(message);
		
		} catch (MessagingException e) {
			if (debugMode) {
				e.printStackTrace();
			}
			if (unsendMsg == null) {
				unsendMsg = new ArrayList<ContactMessage>(8);
			}
			unsendMsg.add(msg);
		}
	}

	public List<ContactMessage> commit() {
		return unsendMsg;
	}
	
	
	public static class AddressAdapter {
		
		Address[] targeAddresses;
		
		public void adapte(ArrayList<String> addrLs) throws AddressException {
			int sz = addrLs.size();
			targeAddresses = new Address[sz];
			for (int i = 0; i != sz; ++i) {
				targeAddresses[i] = new InternetAddress(addrLs.get(i));
			}
		}
		
		public Address[] getAddresses() {
			return targeAddresses;
		}
	}
}


