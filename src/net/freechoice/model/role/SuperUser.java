package net.freechoice.model.role;

/**
 * @author BowenCai
 *
 */

public class SuperUser extends AvgUser {

	public static final String TIME_EXPIRE = "'TIME_EXPIRE'";
	
	@Override
	public RoleType getRoleType() {
		
		return RoleType.SuperUser;
	}

}
