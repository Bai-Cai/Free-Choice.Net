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
package net.freechoice.model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.freechoice.model.FC_User;
import net.freechoice.service.EncryptionService;

import org.springframework.jdbc.core.PreparedStatementCreator;
/**
 * 
 * @author BowenCai
 *
 */
public class Map_User implements IMapper<FC_User> {
	
	@Override
	public FC_User mapRow(final ResultSet rs, int rowNum) throws SQLException {

		FC_User user = new FC_User();
		
		user.id = rs.getInt(1);
		user.path_avatar = rs.getString(2);
		user.name_login = rs.getString(3);
		user.name_display = rs.getString(4);
		user.email = rs.getString(5);
		user.password = rs.getString(6);
		user.tagline = rs.getString(7);

		return user;
	}


	@Override
	public PreparedStatementCreator createUpdate(final FC_User user) {

		final String psw = EncryptionService.transformPassword(user.password);
		
		return new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				
				PreparedStatement ps = con.prepareStatement(
					"update fc_user set path_avatar = ?,"
					+"name_login = ?,"
					+"name_display = ?, "
					+"email = ?, "
					+"password = ?, "
					+"tagline = ? "
					+ " where id = ?");
				ps.setString(1, user.path_avatar);
				ps.setString(2, user.name_login);
				ps.setString(3, user.name_display);
				ps.setString(4, user.email);
				ps.setString(5, psw);
				ps.setString(6, user.tagline);
				ps.setInt(7, user.id);
//System.err.println("--------  SQL  --------\n" + ps.toString());
				return ps;
			}
		};
	}

	@Override
	public PreparedStatementCreator createInsert(final FC_User user) {

		final String psw = EncryptionService.transformPassword(user.password);
		
		return new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"insert into FC_User(" +
				" name_login," +
				" name_display," +
				" email," +
				" password, " +
				" tagline)" +
				" values(?, ?, ?, ?, ?)"
				,RET_ID
				);
				ps.setString(1, user.name_login);
				ps.setString(2, user.name_display);
				ps.setString(3, user.email);
				ps.setString(4, psw);
				ps.setString(5, user.tagline);
				return ps;
			}
		};
	}
}



