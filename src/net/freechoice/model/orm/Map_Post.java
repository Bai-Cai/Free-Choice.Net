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
import net.freechoice.model.FC_Post;
import org.springframework.jdbc.core.PreparedStatementCreator;
/**
 * 
 * @author BowenCai
 *
 */
public class Map_Post implements IMapper<FC_Post> {


	@Override
	public FC_Post mapRow(final ResultSet rs, int rowNum) 
			throws SQLException {
		
		FC_Post post = new FC_Post();
		
		post.id 			= rs.getInt(1);
		post.status 		= rs.getShort(2);
		post.id_author 		= rs.getInt(3);
		post.name_author 	= rs.getString(4);
		post.time_posted 	= rs.getTimestamp(5);
		post.num_read 		= rs.getInt(6);
		post.num_comment 	= rs.getInt(7);
		post.title 			= rs.getString(8);
		post.content	 	= rs.getString(9);
		
		return post;
	}


	/**
	 * for content update see Dao_Post
	 */
	@Override
	public PreparedStatementCreator createUpdate(final FC_Post entity) {

		return new PreparedStatementCreator() {	
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {

				PreparedStatement ps = con.prepareStatement(
				"update  FC_Post "
				+"set status = ?,"
				+"name_author = ?,"
				+"num_read = ?,"
				+"title = ? "
				+" where id = ? "
				);
				ps.setShort(1, entity.status);
				ps.setString(2, entity.name_author);
				ps.setInt(3, entity.num_read);
				ps.setString(4, entity.title);
				ps.setInt(5, entity.id);
				return ps;
			}
		};
	}

	@Override
	public PreparedStatementCreator createInsert(final FC_Post post) {
		
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(
				"insert into FC_Post("
				+"status,"
				+"id_author,"
				+"name_author,"
				+"title,"
				+"content,"
				+"search_vector)"
				+"values(?,?,?,?,?, to_tsvector(? || ?)) "
				, RET_ID);
				
				ps.setShort(1, post.status);
				ps.setInt(2, post.id_author);
				ps.setString(3, post.name_author);
				ps.setString(4, post.title);
				ps.setString(5, post.content);
				ps.setString(6, post.title);
				ps.setString(7, post.content);
				return ps;
			}
		};
	}
}






