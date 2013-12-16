package net.freechoice.model.role;

/**
 * @author BowenCai
 *
 */
public class Visitor implements IRole {

	@Override
	public RoleType getRoleType() {
		return RoleType.Visitor;
	}

}
