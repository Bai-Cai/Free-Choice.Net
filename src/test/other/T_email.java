package test.other;

import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.MessagingException;

import net.freechoice.application.MailSender;
import net.freechoice.service.WebMailService;
import net.freechoice.service.WebMailService.ContactMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author BowenCai
 *
 */
public class T_email {
	
	
//	public static void main(String[] args) {
//
//		ApplicationContext appContext;
//		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		System.out.println("spring is up and running!");
//		
////		MailSender sender = (MailSender) appContext.getBean("mailSender");
////		int i = 3;
////		ContactMessage msg = new ContactMessage();
////		msg.name = "fc-test";
////		msg.subject = "request for new features";
////		msg.email = "asoijfioe@yy.com";
////		sender.beginTransaction();
////		while(i-- != 0) {
////			Date d = new Date();
////			msg.content = "contentttt  " + d;
////			sender.capsule(msg);
////			System.err.println(d);
////		}
////		System.out.println("result :" + sender.commit());
//	}
	
	
	ApplicationContext appContext;
	@Before
	public void setUp() throws Exception {
		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println("spring is up and running!");
	}
//
//	@After
//	public void tearDown() throws Exception {
//	}

	@Test
	public void testt() {
//		mailSender
//Mail
		WebMailService mailService = (WebMailService) appContext.getBean("mailService");
		int i = 3;
		ContactMessage msg = new ContactMessage();
		msg.name = "fc-test";
		msg.subject = "request for new features";
		msg.email = "asoijfioe@yy.com";
//		sender.beginTransaction();
		while(i-- != 0) {
			Date d = new Date();
			msg.content = "contentttt  " + d;
//			sender.capsule(msg);
			mailService.send(msg.name, msg.email, msg.content);
			System.err.println(d);
		}
//		System.out.println("result :" + sender.commit());
	}
	
	public void test() throws MessagingException {
		WebMailService mailService = (WebMailService) appContext.getBean("mailService");
		int i = 10;
		while(i-- != 0) {
			Date d = new Date();
			mailService.send("cbw", "asoijfioe@yy.com", 
				"contentttt  " + d);
			System.err.println(d);
		}
	}

}




