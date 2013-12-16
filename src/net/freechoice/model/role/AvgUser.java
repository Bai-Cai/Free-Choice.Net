package net.freechoice.model.role;


/**
 * @author BowenCai
 *
 */
public class AvgUser implements IRole {
	
	public int 			id;
	public String		name_login;
	public String		email;
	public String		hashedPswWithSalt;
	
	@Override
	public RoleType getRoleType() {

		return RoleType.User;
	}

}
