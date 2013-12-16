package net.freechoice.service;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import net.freechoice.application.MailSender;
import net.freechoice.application.load.LoadLevel;
import net.freechoice.service.RegulationService.LoadChangedEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;

/**
 * 
 * provide e-mail service based on current load level
 * @author BowenCai
 *
 */
public class WebMailService implements ApplicationListener<LoadChangedEvent> {
	
	LoadLevel currentLevel = LoadLevel.I_LIGHT;
	
	@Override
	public void onApplicationEvent(LoadChangedEvent event) {
		
		LoadLevel newLevel = event.newLoadLevel;
		
		if ( this.currentLevel.equals(LoadLevel.IIII_OVERLOAD)
				&& !newLevel.equals(LoadLevel.IIII_OVERLOAD)) {
			flush();
		}
		this.currentLevel = event.newLoadLevel;
	}

////////////////////////////////////////////////////////////
	
	public static class ContactMessage {
		public String name;
		public String email;
		public String subject;
		public String content;
	}
	
	MailSender mailSender;
	TaskExecutor taskExecutor;
	
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public WebMailService() {
		messageQueue = new LinkedBlockingQueue<>(8);
	}

	LinkedBlockingQueue<ContactMessage> messageQueue;
	
	public static final String CONTACT_MSG = "FC contact-message";
	
	/**
	 * non blocking
	 * 
	 * @param name
	 * @param senderAdd
	 * @param content
	 */
	public void send(final String name,
						final String senderAdd,
						final String content) {
		/**
		 * if we are free, send immediately
		 * other wise push the message to queue
		 * and wait for LoadLevel.I_LIGHT
		 */
		if ( !currentLevel.equals(
				LoadLevel.IIII_OVERLOAD)) {

			taskExecutor.execute( new Runnable() {
				@Override
				public void run() {
					mailSender.send(name, senderAdd, CONTACT_MSG, content);
				}
			});
		} else {
			ContactMessage msg = new ContactMessage();
			msg.name = name;
			msg.email = senderAdd;
			msg.content = content;
			msg.subject = CONTACT_MSG;
			messageQueue.add(msg);
		}
	}
	/**
	 * send all message in queue
	 */
	public void flush() {
		if (messageQueue.size() > 0) {
			mailSender.beginTransaction();
			while (!messageQueue.isEmpty()) {
				mailSender.dispatch(messageQueue.remove());
			}

			List<ContactMessage> unsent = mailSender.commit();
			if (unsent != null) {
				for (ContactMessage m : unsent) {
					messageQueue.add(m);
				}
			}
		}
	}
	
}
