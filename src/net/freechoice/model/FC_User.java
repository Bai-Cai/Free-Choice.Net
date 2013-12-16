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
package net.freechoice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User entity,
 * 
 * @author BowenCai
 *
 */
@Entity
@Table(name="FC_User")
public class FC_User implements IModel {

	public static final long serialVersionUID = -464078696567349754L;
	public static final int serialID32 = (int)(serialVersionUID ^ (serialVersionUID >>> 32));

//	public static final String TABLE_NAME = "FC_User";
//	private static final String VIEW_NAME = "v_user";
	
	public static boolean	MALE = false;
	public static boolean	FEMALE = true;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int				id;

	@Column(length=48)
	public String			path_avatar;
	
	@Column(length = 32)
	public String			name_login;
	
	@Column(length = 32)
	public String			name_display;
	

	@Column(length = 48)
	public String			email;
	
	@Column(length = 32)
	public String			password;

	@Column(length = 64)
	public String			tagline;
	
	public FC_User() {
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) {
			return true;
		} else if (o instanceof FC_User) {
			return ((FC_User)o).id == this.id;
		} else {
			return false;
		}
	}
    
	@Override
	public int hashCode() {
		return this.id * 31 + serialID32;
	}


	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getPath_avatar() {
		return path_avatar;
	}

	public void setPath_avatar(String path_avatar) {
		this.path_avatar = path_avatar;
	}

	public String getName_login() {
		return name_login;
	}

	public void setName_login(String name_login) {
		this.name_login = name_login;
	}

	public String getName_display() {
		return name_display;
	}

	public void setName_display(String name_display) {
		this.name_display = name_display;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public void setId(int id) {
		this.id = id;
	}

//	@Override
//	public String getViewName() {
//		return VIEW_NAME;
//	}
//
//	@Override
//	public String getTableName() {
//		return TABLE_NAME;
//	}
}
