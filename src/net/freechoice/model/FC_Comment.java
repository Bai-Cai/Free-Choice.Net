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

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.freechoice.misc.annotation.ReadOnly;
/**
 * 
 * @author BowenCai
 *
 */
@Entity
@Table(name = "FC_Comment")
public class FC_Comment implements IModel {

	private static final long serialVersionUID = 5237392813785018862L;
	private static final int serialID32 = (int) (serialVersionUID ^ (serialVersionUID >>> 32));
//	public static final String TABLE_NAME = "FC_Comment";
//	public  static final String VIEW_NAME = "V_Comment";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int 				id;

	public int 				id_post_;

	@ReadOnly
	public Timestamp 		time_posted;
	
	@Column(length = 48)
	public String 			email;
	
	@Column(length = 32)
	public String 			name;
	
	@Column(length = 128)
	public String 			comment;


	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		} else if (o instanceof FC_Comment) {
			return ((FC_Comment) o).id == this.id;
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

	public int getId_post_() {
		return id_post_;
	}

	public void setId_post_(int id_post_) {
		this.id_post_ = id_post_;
	}

	public Timestamp getTime_posted() {
		return time_posted;
	}

	public void setTime_posted(Timestamp time_posted) {
		this.time_posted = time_posted;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setId(int id) {
		this.id = id;
	}

//	@Override
//	public String getViewName() {
//		return VIEW_NAME;
//	}	@Override
//	public String getTableName() {
//		// TODO Auto-generated method stub
//		return TABLE_NAME;
//	}
}
