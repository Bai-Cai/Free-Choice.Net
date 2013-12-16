package net.freechoice.action;

import net.freechoice.service.WebMailService;
import com.opensymphony.xwork2.ActionSupport;

/**
 * ActionMail.java
 * @author 白强
 * @version 1.0
 * 2013-12-5
 */
public class ActionMail extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	/**
	 * non-blocking method
	 */
	@Override
	public String execute() throws Exception {

		mailService.send(contact_name, contact_address, contact_message);
		
		return SUCCESS;
	}
	
	private String contact_name;
	private String contact_address;
	private String contact_message;

	
	private WebMailService mailService;
	
	public void setMailService(WebMailService mailService) {
		this.mailService = mailService;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}


	public void setContact_address(String contact_address) {
		this.contact_address = contact_address;
	}

	public void setContact_message(String contact_message) {
		this.contact_message = contact_message;
	}
	
}
