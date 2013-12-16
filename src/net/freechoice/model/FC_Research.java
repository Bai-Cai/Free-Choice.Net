package net.freechoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author BowenCai
 *
 */
@Entity
@Table(name="FC_Research")
public class FC_Research implements IModel{

	private static final long serialVersionUID = 8445390791968520242L;
	public static final int serialID32 = (int)(serialVersionUID ^ (serialVersionUID >>> 32));
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int				id;
	
	
	
	
	@Override
	public int getId() {
		return id;
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
}
