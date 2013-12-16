package net.freechoice.action;

import com.opensymphony.xwork2.ActionSupport;


/**
 * @author 白强
 * @version 1.0
 * 2013-11-13
 */
public abstract class ActionTemplate<T> extends ActionSupport {

	private static final long serialVersionUID = 1L;

	/**
	 * 须与struts配置同步
	 */
	public static final String ACT_LOGIN = "login";

	public static final String ACT_USER = "user";
	
	public static final String USR_LOGGEDIN = "user-logged-in";
	
	public abstract String 	save();
	
	public abstract String 	update();
	
	public abstract String 	query();
	
	public abstract String 	delete();
	
	public abstract String 	get();
	
}
