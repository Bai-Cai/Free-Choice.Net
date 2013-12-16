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
 * @author BowenCai
 *
 */
@Entity
@Table(name="FC_Research_log")
public class FC_ResearchLog implements IModel {

	private static final long serialVersionUID = 4896428624490308254L;
	public static final int serialID32 = (int)(serialVersionUID ^ (serialVersionUID >>> 32));
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int				id;
	
	public int				id_research_;
	
	@Column(length = 32)
	public String			name_author;
	
	@ReadOnly
	public Timestamp		time_posted;//timestamp
	
	@ReadOnly
	public int				num_read;
	
	@Column(length = 96)
	public String			title;

	@Column()
	public String			content;
	
	public FC_ResearchLog() {
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) {
			return true;
		} else if (o instanceof FC_Research) {
			return ((FC_Research)o).id == this.id;
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

}
