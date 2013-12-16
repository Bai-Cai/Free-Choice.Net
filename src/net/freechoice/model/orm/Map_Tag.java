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
import net.freechoice.model.FC_Tag;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * 
 * @author BowenCai
 *
 */
public class Map_Tag implements IMapper<FC_Tag> {
	
	@Override
	public FC_Tag mapRow(final ResultSet rs, int rowNum) 
			throws SQLException {
		
		FC_Tag tag = new FC_Tag();
		
		tag.id			= rs.getInt(1);
		tag.num_tagged 	= rs.getInt(2);
		tag.content 	= rs.getString(3);

		return tag;
	}

	

	@Override
	public PreparedStatementCreator createUpdate(final FC_Tag entity) {

		return new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {

				PreparedStatement ps = con.prepareStatement(
				"update FC_Tag "
				+"set content = ? "
				+"where id = ? "
				);
				ps.setString(1, entity.content);
				ps.setInt(2, entity.id);
				return ps;
			}
		};
	}

	@Override
	public PreparedStatementCreator createInsert(final FC_Tag tag) {

		return new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"insert into FC_Tag(content)" +
				" values(?)"
				, RET_ID
				);
				ps.setString(1, tag.content);
				return ps;
			}
		};
	}
}






