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
package net.freechoice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import net.freechoice.dao.IDaoComment;
import net.freechoice.dao.annotation.DBSpec;
import net.freechoice.dao.annotation.Dialect;
import net.freechoice.dao.annotation.Trigger;
import net.freechoice.model.FC_Comment;
import net.freechoice.model.orm.Map_Comment;

import org.springframework.jdbc.core.PreparedStatementCreator;
/**
 * 
 * @author BowenCai
 *
 */
public class DaoComment extends DaoTemplate<FC_Comment> implements IDaoComment {

	
	public DaoComment() {
		super(FC_Comment.class, new Map_Comment());
	}
	private static final String SELECT_FROM_FC_COMMENT = 
			"SELECT	id,"
			+"id_post_,"
			+"time_posted,"
			+"email,"
			+"name,"
			+"comment "
			+"FROM	FC_Comment ";


	@Override
	public FC_Comment getById(int id) {

		return getJdbcTemplate().queryForObject(
				SELECT_FROM_FC_COMMENT
				+"where is_valid = true and id = " + id,
				mapper);
	}
	
	@Override
	public List<FC_Comment> getCommentsOfPost(int postId, int offset, int limit) {
		
		return getJdbcTemplate().query(
				SELECT_FROM_FC_COMMENT 
				+ "where is_valid = true and id_post_ = " + postId
				+ TIME_DESCEND
				+" offset " + offset
				+" limit " + limit,
				mapper);
	}

	@Override
	public List<FC_Comment> getCommentsOfName(final String name) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				
				PreparedStatement ps = con.prepareStatement(
					SELECT_FROM_FC_COMMENT 
					+ " where is_valid = true and name = ?");
				ps.setString(1, name);
				return ps;
			}
		}, mapper);
	}

	@Override
	public List<FC_Comment> getCommentsOfEmail(final String email) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				
				PreparedStatement ps = con.prepareStatement(
					SELECT_FROM_FC_COMMENT 
					+ " where is_valid = true and email = ?");
				ps.setString(1, email);
				return ps;
			}
		}, mapper);
	}

	@Trigger
	@Override
	public int save(final FC_Comment comment) {
		
		getJdbcTemplate().execute(

				 " begin transaction;"
				+" update fc_post set num_comment = num_comment + 1 "
				+" where id = " + comment.id_post_ + ";"
				+" commit;");

		return super.save(comment);
	}
	

	@DBSpec(dialect = Dialect.PostgreSQL)
	@Trigger
	@Override
	public void discard(int id) {
		
		getJdbcTemplate().execute(
				 "begin transaction;"
				+" update  fc_comment "
				+" set is_valid = false where id = " + id + ";"
				
				+" update fc_post as P "
				+" set num_comment = num_comment - 1"
				+" from fc_comment as C"
				+" where C.id_post_ = P.id and C.id = " + id+ ";"
				+" commit;"
				);
	}
	
	/**
	 * prefer this one to discard(int id)
	 */
	@Trigger
	@Override
	public void discard(final FC_Comment com) {
		
		getJdbcTemplate().execute(
				  BEGIN
				+ "update  fc_comment" 
				+ " set is_valid = false where id = " + com.id + ";"
				
				+"update fc_post set num_comment = num_comment - 1"
				+"where id = " + com.id_post_+ ";"
				+COMMIT);
	}
	/**
	 * some entity may override this to enable action chain, 
	 * e.g., invalidate cascade. 
	 */
	@Trigger
	@DBSpec(dialect = Dialect.PostgreSQL, note="update claues PG only")
	@Override
	public void recover(int id) {
		
		getJdbcTemplate().execute(
				BEGIN
				+"update fc_comment "
				+ " set is_valid = true where id = " + id + ";"
				
				+ "update fc_post as P"
				+ "set num_comment = num_comment + 1"
				+" from fc_comment as C"
				+ "where C.id_post_ = P.id "
				+" and C.id = " + id + ";"
				+ COMMIT);
	}
	/**
	 * some entity may override this to enable actions chain, 
	 * e.g., invalidate cascade. 
	 */
	@Trigger
	@DBSpec(dialect = Dialect.PostgreSQL, note="update claues PG only")
	@Override
	public void delete(int id) {
		
		getJdbcTemplate().execute(
				BEGIN
				+"delete from  fc_comment"
				+"where id = " + id + ";"
				
				+"update fc_post as P"
				+ " set num_comment = num_comment - 1"
				+"from fc_comment as C"
				+"where C.id_post_ = P.id"
				+" and C.id = " + id + ";"
				+COMMIT);
	}
	/**
	 * prefer this one to delete(int id)
	 */
	@Trigger
	@Override
	public void delete(final FC_Comment com) {
		
		getJdbcTemplate().execute(
				BEGIN
				+"delete from " + tableName
				+"where id = " + com.id + ";"
				
				+"update fc_post set num_comment = num_comment - 1"
				+"where id = " + com.id_post_ + ";"
				+COMMIT);
	}

}



