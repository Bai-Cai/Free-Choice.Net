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

import org.springframework.jdbc.core.PreparedStatementCreator;

import net.freechoice.dao.IDaoTag;
import net.freechoice.misc.annotation.Warning;
import net.freechoice.model.FC_Tag;
import net.freechoice.model.orm.Map_Tag;

/**
 * 
 * @author BowenCai
 *
 */
public class DaoTag extends DaoTemplate<FC_Tag> implements IDaoTag {

	public DaoTag() {
		super(FC_Tag.class, new Map_Tag());
	}

	private static final String SELECT_FROM_FC_TAG= 
				"SELECT	id,"
				+"	num_tagged,"
				+"	content "
				+"from fc_tag ";
	@Override
	public FC_Tag getById(int id) {
		return getJdbcTemplate().queryForObject(
				SELECT_FROM_FC_TAG
				+ " where is_valid = true and id = " + id,
				mapper);
	}
	@Override
	public List<FC_Tag> getTags(int offset, int tagNumber) {
		
		return getJdbcTemplate().query(
				SELECT_FROM_FC_TAG
				+ " where is_valid = true "
				+ " order by num_tagged "
				+ " offset " + offset
				+ " limit " + tagNumber, mapper);
	}
	
	@Warning(values={"Injection"})
	@Override
	public List<FC_Tag> getTagsByName(final String name) {

		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				SELECT_FROM_FC_TAG
				+ " where is_valid = true and content = ?");
				ps.setString(1, name);
				return ps;
			}
		}, mapper);
	}

	@Override
	public List<String> getAllTagNames() {
		
		return getJdbcTemplate().queryForList(
				"select content from fc_tag " +
				"where is_valid = true order by num_tagged ",
				String.class);
	}

	@Override
	public List<FC_Tag> getTagsOfPost(int postId, int offset, int limit) {
		
		return getJdbcTemplate().query(
				SELECT_FROM_FC_TAG + " as T inner join r_tag_post as R "
				+" on R.id_tag_ = T.id "
				+" where T.is_valid = true and  R.id_post_ = " + postId
				+" offset " + offset
				+" limit " + limit 
				,mapper);
	}

	@Override
	public List<String> getTagNamesOfPost(int postId, int offset, int limit) {
		
		return getJdbcTemplate().queryForList(
				"select content from fc_tag as T inner join r_tag_post as R "
				+"on R.id_tag_ = T.id "
				+"where T.is_valid = true and R.id_post_ = " + postId
				+" offset " + offset
				+" limit " + limit
				, String.class);
	}

}
