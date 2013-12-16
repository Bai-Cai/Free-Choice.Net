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
import net.freechoice.model.FC_Comment;

import org.springframework.jdbc.core.PreparedStatementCreator;
/**
 * 
 * @author BowenCai
 *
 */
public class Map_Comment implements IMapper<FC_Comment> {

	
	@Override
	public FC_Comment mapRow(final ResultSet rs, int rowNum) 
			throws SQLException {

		FC_Comment comment = new FC_Comment();
		
		comment.id  		= rs.getInt(1);
		comment.id_post_ 	= rs.getInt(2);
		comment.time_posted = rs.getTimestamp(3);
		comment.email		= rs.getString(4);
		comment.name		= rs.getString(5);
		comment.comment		= rs.getString(6);
		return comment;
	}
	
	@Override
	public PreparedStatementCreator createInsert(final FC_Comment comment) {
		
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"insert into FC_Comment(" +
				"id_post_, email, name, comment)" +
				" values(?, ?, ?, ?)"
				,RET_ID
				);
				ps.setInt(1, comment.id_post_);
				ps.setString(2, comment.email);
				ps.setString(3, comment.name);
				ps.setString(4, comment.comment);
				return ps;
			}
		};
	}

	@Override
	public PreparedStatementCreator createUpdate(final FC_Comment entity) {

		return new PreparedStatementCreator() {	
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				
				PreparedStatement ps = con.prepareStatement(
					"update FC_Comment set id_post_= ? "
					+",  email = ?"
					+",  name = ?"
					+",  comment = ?"
					+" where id = ?");//
				ps.setInt(1, entity.id_post_);
				ps.setString(2, entity.email);
				ps.setString(3, entity.name);
				ps.setString(4, entity.comment);
				ps.setInt(5, entity.id);
				return ps;
			}
		};
	}

}






