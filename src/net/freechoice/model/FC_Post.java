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
 *  Table(name="V_Post") V_Post is the database view name
 * 	TABLE_NAME from getTableName is the database table name
 * @author BowenCai
 *
 */
@Entity
@Table(name="FC_Post")
public class FC_Post implements IModel{

	public static final long serialVersionUID = -7328287539453747388L;
	public static final int serialID32 = (int)(serialVersionUID ^ (serialVersionUID >>> 32));

	public static final short DRAFT = 0;
	public static final short PENDING= 1;
	public static final short PUBLISHED = 2;
	public static final short FREEZED = 3;	
	public static final short CLOSED = 4;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int				id;
	
	public short			status = DRAFT;
	
	@ReadOnly
	public int				id_author;
	
	@Column(length = 32)
	public String			name_author;
	
	@ReadOnly
	public Timestamp		time_posted;//timestamp
	
	@ReadOnly
	public int				num_read;

	@ReadOnly
	public int				num_comment;
	
	@Column(length = 96)
	public String			title;

	/**
	 * 	In database V_Post : select ... left(content, 384)
	 *  so
	 * 	In function return value List<V_Post>
	 *  'content' contains only the first 384 chars of the real content.
	 *  
	 *  to get the full content, use Dao_Post getFullText()
	 *  to update content use Dao_Post updateContentOf
	 *  
	 *  content is of 'text' type in PostgreSQL,
	 */
	@Column()
	public String			content;
	
	
	
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) {
			return true;
		} else if (o instanceof FC_Post) {
			return ((FC_Post)o).id == this.id;
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
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public int getId_author() {
		return id_author;
	}

	public void setId_author(int id_author) {
		this.id_author = id_author;
	}

	public String getName_author() {
		return name_author;
	}

	public void setName_author(String name_author) {
		this.name_author = name_author;
	}

	public Timestamp getTime_posted() {
		return time_posted;
	}

	public void setTime_posted(Timestamp time_posted) {
		this.time_posted = time_posted;
	}

	public int getNum_read() {
		return num_read;
	}

	public void setNum_read(int num_read) {
		this.num_read = num_read;
	}

	public int getNum_comment() {
		return num_comment;
	}

	public void setNum_comment(int num_comment) {
		this.num_comment = num_comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}



//	@Override
//	public String getViewName() {
//		return VIEW_NAME;
//	}
//	@Override
//	public String getTableName() {
//		return TABLE_NAME;
//	}


//	public class Content {
//		private String content;
//		public String get() {
//			if (null == content) {
//				
//			}
//			return content;
//		}
//	}
}
