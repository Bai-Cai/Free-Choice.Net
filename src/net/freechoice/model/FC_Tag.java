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

import net.freechoice.misc.annotation.ReadOnly;
/**
 * 
 * @author BowenCai
 *
 */
@Entity
@Table(name="FC_Tag")
public class FC_Tag implements IModel {
	
	public static final long serialVersionUID = 1L;
	public static final int serialID32 = (int)(serialVersionUID ^ (serialVersionUID >>> 32));
//	public static final String TABLE_NAME = "FC_Tag";
//	private static final String VIEW_NAME = "V_Tag";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int				id;
	
	@ReadOnly
	public int				num_tagged;
	
	@Column(length = 32)
	public String			content;
	
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) {
			return true;
		} else if (o instanceof FC_Tag) {
			return ((FC_Tag)o).id == this.id;
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

	public int getNum_tagged() {
		return num_tagged;
	}

	public void setNum_tagged(int num_tagged) {
		this.num_tagged = num_tagged;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(int id) {
		this.id = id;
	}

//	@Override
//	public String getViewName() {
//		return VIEW_NAME;
//	}
//	@Override
//	public String getTableName() {
//		return TABLE_NAME;
//	}
}
