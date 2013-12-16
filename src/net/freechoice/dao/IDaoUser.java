/*******************************************************************************
 * Copyright (c) 2013 BowenCai.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     BowenCai - initial API and implementation
 ******************************************************************************/
package net.freechoice.dao;

import java.util.List;

import net.freechoice.dao.annotation.Trigger;
import net.freechoice.model.FC_User;
import net.freechoice.model.role.AvgUser;

/**
 * 
 * @author BowenCai
 *
 */
public interface IDaoUser extends IDaoBase<FC_User> {
	
	/**
	 * display name of invalid user,
	 * used in post, team, project
	 */
	public static final String NAME_INVALID_USER = " 'user invalid' ";
	

	/**
	 * true 如果 用户已在数据库中，并且 is_valid = true
	 * @param user id
	 * @return return FC_User.is_valid
	 */
	boolean			isUserValid(int id);
	
	/**
	 * 
	 * @param user name
	 * @return return FC_User.is_valid
	 */
	boolean			isUserValid(final String loginName);
	
//	/**
//	 * check before the entire entity is loaded.
//	 * @param loginNameOrEmail check NOT NULL
//	 * @param password		   check NOT NULL
//	 * @return
//	 */
//	@Deprecated
//	boolean			isPasswordCorrect(final String password,
//										final String loginNameOrEmail);
	
	
	/**
	 * 如果数据库中没有这个用户名 -> true
	 * 
	 * used in register
	 * @param name_login
	 * @return if the user of the login name is still valid
	 */
	boolean			isLoginNameUnique(final String name);
	
	/**
	 * 如果数据库中没有哪个用户名使用这个email -> true
	 * check both valid and invalid user .
	 * used in register
	 * @param name
	 * @return
	 */
	boolean			isEmailUnique(final String email);
	
	/**
	 * 
	 * @author BowenCai
	 *
	 */
	public class Result {
		/**
		 * id of the user 
		 */
		public int 	id;
		/**
		 * password from the DB(hashed password with salt);
		 */
		public String	password;
		
		public boolean	isSuperUser;
	}
	
	/**
	 * 
	 * @param name Or Email should be verified before this function(in actions) 
	 * 		using regex.
	 * @return password of this user.consider cache this password .
	 */
	@Deprecated
	Result			getPasswordOfUser(final String nameOrEmail);
	
	
	AvgUser			getRoleByEmail(final String email);
	
	AvgUser			getRoleByLoginName(final String name);
	
	/**
	 * select popular bloggers
	 * ordered by post count desc
	 * @return list of users 
	 */
	List<FC_User> 	getUsersByPostCount(final int offset, final int limit);
	
	
	/**
	 * 
	 * @param name used to log in, unique
	 * @return FC_user or null
	 */
	FC_User 		getUsersByLoginString(final String nameLoginOrEmail);
	
	FC_User			getAuthorOfPost(int postId);

	/**
	 * 
	 * @param userId
	 * @param key
	 * @return
	 */
	String			getValue(int userId, final String key);
	
	/**
	 * map: put 
	 * @param postId
	 * @param key
	 * @param value
	 */
	void			put(int postId, final String key, final String value);
	
	@Trigger
	@Override
	public void 	discard(int id);

	@Trigger
	@Override
	public void 	delete(int id);

	@Trigger
	@Override
	public void 	recover(int id);

	@Trigger
	@Override
	public void 	update(FC_User u);
	
}





