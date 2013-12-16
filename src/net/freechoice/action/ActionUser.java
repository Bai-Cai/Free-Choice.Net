package net.freechoice.action;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.freechoice.dao.IDaoUser;
import net.freechoice.dao.IDaoUser.Result;
import net.freechoice.model.FC_User;
import net.freechoice.model.role.AvgUser;
import net.freechoice.service.EncryptionService;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author 白强
 * @version 1.0
 * @since 2013-11-14
 * 
 * @author BowenCai
 * @version 2013-11-21
 */
public class ActionUser  extends ActionTemplate<FC_User> {
	
	private static final long serialVersionUID = 1L;
	
	private IDaoUser 	userDao;

	private FC_User 	user;
	private int 		userId;

	/**
	 * 来自用户输入的 name
	 */
	private String 		name;
	

	public EncryptionService	encryptionService;
	
	/**
	 * 来自用户输入的 password
	 */

	private String 		loginPsw;

	private String 		newPsw;

	private String 		pswConfirmed;
	
	public String save() {
		String newName = user.name_login;
		if(newName != null 
				&& userDao.isLoginNameUnique(user.getName_login())){

			userDao.save(user);
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.put(ACT_USER, user);
			return SUCCESS;
		}
		this.addFieldError("loginUserError", "用户已经存在!");
		return INPUT;
	}

	public String update() {
		
		if (getNewPsw() != null) {	
			if (getPswConfirmed() != null
				&& getNewPsw().equals(getPswConfirmed())) {
				
				user.password = getNewPsw();
			} else {
				this.addFieldError("loginUserError", "two passwords do not match");
				return INPUT;
			}
		}
		userDao.update(user);
		return SUCCESS;
	}

	/**
	 * 这是干嘛的？
	 */
	@Override
	public String query() {
		
		user = userDao.getUsersByLoginString(name);
		
		return SUCCESS;
	}

	@Override
	public String delete() {

		if (userDao.getById(getUserId()) != null) {
			userDao.delete(getUserId());
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
	
	@Override
	public String get() {
		user = (FC_User)ActionContext.getContext().getSession().get(ACT_USER);
		return SUCCESS;
	}

	@SuppressWarnings("unused")
	public String login() {
		
		final String nameOrEmail = getName().trim();
		final String loginPsw = getLoginPsw().trim();

		if (null == nameOrEmail
				|| (null != nameOrEmail && nameOrEmail.length() < 1)) {
			
			this.addFieldError("loginUserError", 
								"Please enter user name.");
			return INPUT;
		} else if (null == loginPsw
				|| (null != loginPsw && loginPsw.length() < 1)) {
			
			this.addFieldError("loginUserError", 
								"Please enter your password.");
			return INPUT;
		}
		
		@SuppressWarnings("deprecation")
		Result psw = userDao.getPasswordOfUser(nameOrEmail);
		
		if (null == psw) {
			this.addFieldError("loginUserError", "Account does not exesit.");
			return INPUT;
		}

		ActionContext actContext = ActionContext.getContext();

//		ServletContext servContext = ServletActionContext.getServletContext();

		if (true) {
//		if (EncryptionService
//				.validatePasswords(loginPsw,psw.password)) {
//		if (loginPsw.equals(role.hashedPswWithSalt.trim())) {
		
			this.user = userDao.getById(psw.id);
			actContext.getSession().put(ACT_USER, user);
//System.err.println("Session().put(user " + user.name_display);
			return SUCCESS;	
		}
		else {
			this.addFieldError("loginPasswordError", "Wrong password");
			return INPUT;
		}
	}
	
	public String logout() {
		
		// 还有其他的要remove吗？
		ActionContext.getContext().getSession().remove(ACT_USER);
		return SUCCESS;
	}
	
	public String getNewPsw() {
		return newPsw;
	}

	public void setNewPsw(String newPsw) {
		this.newPsw = newPsw;
	}

	public String getPswConfirmed() {
		return pswConfirmed;
	}

	public void setPswConfirmed(String pswConfirmed) {
		this.pswConfirmed = pswConfirmed;
	}

	public String getLoginPsw() {
		return loginPsw;
	}

	public void setLoginPsw(String loginPsw) {
		this.loginPsw = loginPsw;
	}
	public IDaoUser getUserDao() {
		return userDao;
	}

	public void setUserDao(IDaoUser userDao) {
		this.userDao = userDao;
	}
	
	public FC_User getUser() {
		return user;
	}

	public void setUser(FC_User user) {
		this.user = user;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int id) {
		this.userId = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
	
	
	
	
	public String _11__login() {
		
		final String nameOrEmail = getName().trim();
		final String loginPsw = getLoginPsw().trim();
System.err.println("nameOrEmail:" + nameOrEmail 
					+ "  loginpsw" + loginPsw);

		if (null == nameOrEmail
				|| (null != nameOrEmail && nameOrEmail.length() < 1)) {
			
			this.addFieldError("loginUserError", 
								"Please enter user name.");
			return INPUT;
		} else if (null == loginPsw
				|| (null != loginPsw && loginPsw.length() < 1)) {
			
			this.addFieldError("loginUserError", 
								"Please enter your password.");
			return INPUT;
		}
		
		AvgUser role = nameOrEmail.contains("@") ? 
					userDao.getRoleByEmail(nameOrEmail)
				:	userDao.getRoleByLoginName(nameOrEmail);
		
		if (null == role) {
			this.addFieldError("loginUserError", "Account does not exesit.");
			return INPUT;
		}
		
		ActionContext actContext = ActionContext.getContext();
		
System.err.println("from db   :" + role.hashedPswWithSalt);
System.err.println("from user : " + loginPsw);

		if (EncryptionService
				.validatePasswords(loginPsw,
						role.hashedPswWithSalt)) {
//		if (loginPsw.equals(role.hashedPswWithSalt.trim())) {
		
			this.user = userDao.getById(role.id);

			actContext.getSession().put(ACT_USER, user);

			return SUCCESS;	
		}
		else {
			this.addFieldError("loginPasswordError", "Wrong password");
			return INPUT;
		}
	}
	/**
	 * 第三版
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	public String _login() {
		
		final String nameOrEmail = getName().trim();

		final String loginPsw = getLoginPsw().trim();
		
		AvgUser role = nameOrEmail.contains("@") ? 
					userDao.getRoleByEmail(nameOrEmail)
				:	userDao.getRoleByLoginName(nameOrEmail);
		
		if (null == role) {
			this.addFieldError("loginUserError", "Account does not exesit.");
			return INPUT;
		}

		ActionContext actContext = ActionContext.getContext();
		
		@SuppressWarnings("unchecked")
		Map<String, String> userLoggedin
					= (Map<String, String>) actContext.get(USR_LOGGEDIN);
		
		if (null == userLoggedin) {
System.err.println("new User");
			userLoggedin = new ConcurrentHashMap<String, String>(16);
		}
		if (userLoggedin.get(role.name_login) != null) {			
			
			this.addFieldError("user-already-logged-in", 
								"You have already logged in");
				return INPUT;
		}
//		HttpServletRequest
//		if (EncryptionService
//				.validatePasswords(loginPsw,
//						role.hashedPswWithSalt))
		if (true) {
		
			this.user = userDao.getById(role.id);
			userLoggedin.put(user.name_login, "test");
			actContext.put(USR_LOGGEDIN, userLoggedin);
			
			/**
			 * for the legacy system
			 */
			actContext.getSession().put(ACT_USER, user);

			return SUCCESS;	
		}
		else {
			this.addFieldError("loginPasswordError", "Wrong password");
			return INPUT;
		}
	}

	/**
	 * 先验证 password 和 name_login 或 email的对应关系，
	 * 成功后 再加载 user
	 * 见
	 * Dao_User
	 * 
	 * 	boolean	isPasswordCorrect(final String password,
	 *							final String loginNameOrEmail);
	 * 和
	 * getUsersByLoginString
	 * 
	 * 可参考下面的一个  _login
	 * @return
	 */
	public String ___login() {
		

		if (userDao.getUsersByLoginString(getName()) != null) {
			user = userDao.getUsersByLoginString(getName());
//			System.out.println("from form:"+getPassword());
//			System.out.println("user password:"+user.password);
			if ((user.password).trim().equals(getLoginPsw())) {
				
				/*
				 * 这个地方是应该加user，还是user.name_display?
				 * 如果是user.name_display
				 * 最好这样命名:
				 * 
				 * .put("user", user.name_display);
				 * 换成
				 * .put("user-name_display", user.name_display);
				 *        ^         ^
				 *          实体 - 属性名		（Context名字， class名字，数据库名字 都一样）
				 * 
				 */
				ActionContext.getContext().getSession()
						.put("user", user);
				
				return SUCCESS;
			} else {
				this.addFieldError("loginPasswordError", "您的密码错误！");
				return INPUT;
			}
		}
		this.addFieldError("loginUserError", "账户不存在!");
		return INPUT;
	}
	@SuppressWarnings("unused")
	@Deprecated
	public String __login() {

		/**
		 * 应该可以是登录名 name_login 或邮箱 email, 不是只能是登录名
		 * 可以 改 getName()  -> getLoginString
		 * 
		 */
		final String nameOrEmail = getName().trim();
		final String loginPsw = getLoginPsw().trim();
		/**
		 * 先执行一次数据库查询，使用string查
		 * 
		 * 你看看能不能缓存这个result，这样用户如果输错密码而用户名是对的，就不用再查一次用户名了
		 */
		Result result = userDao.getPasswordOfUser(nameOrEmail);
		
		
		if (null == result) {
			/**
			 * 没查到
			 */
			this.addFieldError("loginUserError", "账户不存在!");
			return INPUT;
		}
		/**
		 * 查到了，比密码。
		 * 此处不能直接String.equals， 因为密码是加盐哈希过的。
		 * 两个密码不要写反了。
		 */
		if (true) {
			/**
			 * 密码正确，取用户
			 * 
			 * 再次查时，通过 id 查询，快于使用 string 查询
			 */
			this.user = userDao.getById(result.id);
			
			/**
			 * 是不是因该直接把该user加入此session的context？
			 *  在需要name_dispaly直接从user取
			 */
			ActionContext.getContext().getSession().put(ACT_USER, user);
			
			return SUCCESS;
		} else {
			this.addFieldError("loginPasswordError", "您的密码错误！");
			return INPUT;
		}
	}

	
}


//
//@SuppressWarnings({ "unchecked", "unused" })
//public String login() {
//	
//	final String nameOrEmail = getName().trim();
//	final String loginPsw = getPassword().trim();
//	
//	AvgUser role = nameOrEmail.contains("@") ? 
//				userDao.getRoleByEmail(nameOrEmail)
//			:	userDao.getRoleByLoginName(nameOrEmail);
//	
//	if (null == role) {
//		this.addFieldError("loginUserError", "Account does not exesit.");
//		return INPUT;
//	}
//
//	ActionContext actCtx = ActionContext.getContext();
//	Map<String, String> userLoggedin
//				= (Map<String, String>) actCtx.get(USR_LOGGEDIN);
//	
//	if (userLoggedin == null) {
//System.err.println("new ConcurrentHashMap");
//		userLoggedin = new ConcurrentHashMap<String, String>();
//	}
//	
//System.err.println("userLoggedin.get(role.name_login):" 
//	+ userLoggedin.get(role.name_login)
//);
//	
//	if (userLoggedin.get(role.name_login) != null) {			
//		
//		this.addFieldError("user-already-logged-in", 
//							"You have already logged in");
//			return INPUT;
//	}
////	HttpServletRequest
////	if (EncryptionService
////			.validatePasswords(loginPsw,
////					role.hashedPswWithSalt))
//	if (true) {
////		HttpServletRequest htttpReq = ServletActionContext.getRequest();
////		HttpSession httpSession = htttpReq.getSession();
//		
////		String userIp = htttpReq.getRemoteAddr();
////		Long ipAsLong = StringUtil.ipToLong(userIp);
//	
//		this.user = userDao.getById(role.id);
//
////		httpSession.setAttribute("user", user);
//		userLoggedin.put(user.name_login, "test");
//System.err.println("userLoggedin :"+ userLoggedin + "\n" + "actCtx.put(user-loggedin");
//		actCtx.put(USR_LOGGEDIN, userLoggedin);
//		
//		
//		/**
//		 * for the legacy system
//		 */
//		actCtx.getSession().put("user", user);
////		ActionContext.getContext().getSession().put("user", user);
////		if(role.getRoleType().equals(RoleType.SuperUser)) {
////			/**
////			 * 超级用户登录了！
////			 */
////			httpSession.setAttribute("isSuperUser", true);
////			ActionContext.getContext().getSession().put("isSuperUser", true);
////		} else {
////			httpSession.setAttribute("isSuperUser", false);
////			ActionContext.getContext().getSession().put("isSuperUser", false);
////		}
//
////		HttpSessionMonitor.currentSession.put(httpSession.getId(), ipAsLong);
////		HttpSessionMonitor.currentSession.put(httpSession.getId(), userIp);
//		return SUCCESS;	
//	}
//	else {
//		this.addFieldError("loginPasswordError", "Wrong password");
//		return INPUT;
//	}
//}

//HttpServletRequest request = ServletActionContext.getRequest();
//
//System.err.println("getQueryString: " + request.getQueryString() + "\n\n");
//System.err.println("getRemoteAddr: " + request.getRemoteAddr());
//System.err.println("getRemoteHost: " + request.getRemoteHost());
//System.err.println("getRemotePort: " + request.getRemotePort());
//System.err.println("getProtocol: " + request.getRemoteUser());