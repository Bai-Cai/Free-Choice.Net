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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.freechoice.application.search.AbstractSearch;
import net.freechoice.application.search.SimpleSearch;
import net.freechoice.dao.IDaoPost;
import net.freechoice.dao.annotation.DBSpec;
import net.freechoice.dao.annotation.Dialect;
import net.freechoice.dao.annotation.Trigger;
import net.freechoice.misc.annotation.Warning;
import net.freechoice.model.FC_Post;
import net.freechoice.model.orm.Map_Post;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * 
 * @author BowenCai
 *
 */
public class DaoPost extends DaoTemplate<FC_Post> implements IDaoPost {
	

	public static final String NUM_READ_DESC = " order by num_read, time_posted desc";
	
	public DaoPost() {
		super(FC_Post.class, new Map_Post());
	}

	public static final String select(final int length) {
		return  " select"
				+"	id,"
				+"	status,"
				+"	id_author,"
				+"	name_author,"
				+"	time_posted,"
				+"	num_read,"
				+"	num_comment,"
				+"	title,"
				+"LEFT(content, " + length + ")"
				+"from fc_post "; 
	}

	public static final String __SELECT = 
			"select id,status,id_author,name_author," +
			"time_posted,num_read,num_comment,title,LEFT(content,";
	
	public static final String selectFromPost(final int length,
											final String condition,
											final String order,
											final int offset,
											final int limit) {
		
		StringBuilder builder = new StringBuilder(224);
		builder.append(__SELECT).append(length).append(")from fc_post ");
		if (condition != null) {
			builder.append(condition);
		}
		if (order != null) {
			builder.append(order);
		}
		builder.append(" offset ").append(offset)
				.append(" limit ").append(limit);
		
		return builder.toString();
	}
	
	private static final String selectFromPost(final int length,
			final String condition, final String order,
			final int limit) {

		StringBuilder builder = new StringBuilder(224);
		builder.append(__SELECT).append(length).append(")from fc_post ");
		if (condition != null) {
			builder.append(condition);
		}
		if (order != null) {
			builder.append(order);
		}
		builder.append(" limit ").append(limit);

		return builder.toString();
	}
	
	@Override
	public FC_Post getById(int id) {
		return getJdbcTemplate().queryForObject(
				select(512) 
				+ "where is_valid = true and id = " + id
				, mapper);
	}
	@Override
	public int countPostsOfUser(int userId) {
		
		return getJdbcTemplate().queryForInt(
				"select count(1) from fc_post where "
				+ "is_valid = true status > 1" // status > 1 and id_author = 
				+"and id_author = " + userId);
	}

	@Override
	public int countDrafsOfAuthor(int userId) {
		
		return getJdbcTemplate().queryForInt(
				"select count(1) from fc_post where "
				+ "is_valid = true "
				+"and id_author = " + userId);
	}

	@Override
	public void fetchContentFor(FC_Post post) {
		
		post.content = getJdbcTemplate().queryForObject(
				"select content from fc_post where id = " + post.id, 
				String.class);
	}
	@Override
	public String getFullText(int id) {
		
		return getJdbcTemplate().queryForObject(
				"select content from fc_post "
				+"where id = " + id,
				String.class);
	}

	/*
	 select * from v_post where 
	 and time_posted < PREVIOUS_TIME
	 order by time_posted
	 limit limit
	 */
	public List<FC_Post> getPostsByUser(int userId,
										final int length,
										final int offset,
										final int limit) {
		
		return getJdbcTemplate().query(
				selectFromPost(length, 
						"where is_valid = true and status > 1 and id_author = " + userId,
						TIME_DESCEND, 
						offset, 
						limit),
				mapper);
	}

	@Override
	public int getPostCountOfUser(int userId) {

		return getJdbcTemplate()
				.queryForInt(
				"select count(1) from fc_post as F "
				+ " where F.is_valid = true and F.status > 1 and F.id_author = " + userId);

	}
	
	@Override
	public List<FC_Post> getDraftsOfAuthor(int userId, int length, 
													int offset,
													int limit) {
		
		return getJdbcTemplate().query(
				selectFromPost(length, 
						" where is_valid = true and id_author = " + userId,
						TIME_DESCEND, 
						offset, limit),
				mapper);
	}
	

	@Override
	public List<FC_Post> getDraftsOfAuthor(final int userId, final int length,
												final Timestamp offset,
												final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						select(length)
						+ " where id_author = ? and time_posted < ? "
						+ TIME_DESCEND
						+ " limit ?");
				ps.setInt(1, userId);
				ps.setTimestamp(1, offset);
				ps.setInt(3, limit);
				return ps;
			}
		}, mapper);
	}
	
	@Override
	public List<FC_Post> getLatestPosts(final int length,
												final int offset,
												final int limit) {
		return getJdbcTemplate().query(
				selectFromPost(length,
						" where is_valid = true and status > 1 ",
						TIME_DESCEND, offset, limit),
						mapper);
	}
	@Override
	public List<FC_Post> getLatestPosts(final int length, 
										final Timestamp offset, 
										final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						selectFromPost(length,
						"where is_valid = true and status > 1 "
						+ " and time_posted < ?", 
						NUM_READ_DESC,
						limit)
						);
				ps.setTimestamp(1, offset);
				return ps;
			}
		}, mapper);
	}
	
	@Override
	public List<FC_Post> getPopularPosts(final int length,
												final int offset,
												final int limit) {
		return getJdbcTemplate().query(
				selectFromPost(length,
					" where is_valid = true and status > 1 ",
					NUM_READ_DESC, offset, limit),
				mapper);
	}
	
	@Override
	public List<FC_Post> getPopularPosts(final int length, final Timestamp offset, final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						selectFromPost(length,
							"where is_valid = true and status > 1 "
							+ " and time_posted < ?", 
							NUM_READ_DESC,
							limit)
						);
				ps.setTimestamp(1, offset);
				return ps;
			}
		}, mapper);
	}
	
	@DBSpec(dialect=Dialect.PostgreSQL)
//	@Deprecated
	@Override
	public List<FC_Post> getPostsBetween(final Date start, final Date end,
										final int length,
										final int offset,
										final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(
					selectFromPost(length,
						" where is_valud = true and status > 1 and (time_posted::date between ? and ?)", 
						TIME_DESCEND, 
						offset, 
						limit)
					);
				ps.setDate(1, start);
				ps.setDate(2, end);
				return ps;
			}
		}, mapper);
	}

	@DBSpec(dialect=Dialect.PostgreSQL)
	@Override
	public List<FC_Post> getPostsOnDate(final Date date,
										final int length,
										final int offset,
										final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(
					selectFromPost(length,
						" where is_valud = true and status > 1 " +
						"and time_posted::date = ?", 
						TIME_DESCEND, 
						offset, 
						limit)
						);
				ps.setDate(1, date);
				return ps;
			}
		}, mapper);
	}

	@Override
	public List<FC_Post> getPostsAfter(final Date time,
										final int length,
										final int offset,
										final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(
						selectFromPost(length,
						" where is_valud = true " +
						"and status > 1 and time_posted > ?", 
						TIME_DESCEND, 
						offset, 
						limit)
					);
				ps.setDate(1, time);
				return ps;
			}
		}, mapper);
	}


	@DBSpec(dialect=Dialect.PostgreSQL)
	@Override
	public List<FC_Post> getPostsBefore(final Date time,
										final int length,
										final int offset,
										final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(
						selectFromPost(length,
						" where is_valud = true and status > 1 and time_posted < ?", 
						TIME_DESCEND, 
						offset, 
						limit)
				);
				ps.setDate(1, time);
				return ps;
			}
		}, mapper);
	}
	
	@Override
	public List<FC_Post> getPostsOfTag(int tagId,
										final int length,
										final int offset,
										final int postNumber) {
		
		return getJdbcTemplate().query(
				 select(length) + " as F "
				+ " inner join R_tag_post as V on V.id_post_ = F.id "
				+" where F.is_valid = true and F.status > 1 and V.id_tag_ = " + tagId
				+ TIME_DESCEND
				+ " offset " + offset
				+ " limit " + postNumber
				,mapper);
	}
	@Override
	public int 	getPostCountOfTag(int tagId) {
		
		return getJdbcTemplate().queryForInt(
				"select count(1) from fc_post as F "
				+" inner join R_tag_post as V on V.id_post_ = F.id "
				+" where F.is_valid = true and F.status > 1 and V.id_tag_ = " + tagId);
	}
	
	@Override
	public List<FC_Post> getPostsOfTagIds(List<Integer> tagIds,
											int length,
											int offset,
											int limit) {
		
		String ids = bracketIds(tagIds);
		
		return getJdbcTemplate().query(
				select(length) + " as F " 
				+" where F.is_valid = true and F.status > 1 and not exists"
				+"(	select id from fc_tag as T "
						+ "where T.is_valid = true and T.id in " + ids
				+"	except "
				+"	(select id_tag_ from r_tag_post as R "
						+"where R.id_post_ = F.id)"
				+ ")"
				+ TIME_DESCEND	
				+ " offset " + offset
				+ " limit " + limit,
				mapper);
	}

	@Override
	public String getValue(final int postId, final String key) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
				"select value from fc_post_meta where id_post_ = ?"
				+ " and key = ?");
				ps.setInt(1, postId);
				ps.setString(2, key);
				return ps;
			}
		}, STR_EXTRACTOR);
	}

	@Override
	public void put(final int postId, final String key, final String value) {
		
		getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {

				PreparedStatement ps = arg0.prepareStatement(
						"insert into fc_post_meta(id_post_, key, value)"
						+"values(?,?,?)"
						);
				ps.setInt(1, postId);
				ps.setString(2, key);
				ps.setString(3, value);
				return ps;
			}
		});
	}

	@Trigger
	@Override
	public void savePostWithTagIds(final FC_Post post, final List<Integer> tags) {
		
		int id  = super.save(post);
		
		if (tags != null && tags.size() > 0) {
			
			String rValue = bracketPostIdnTagIds(id, tags);
			String tagValue = bracketIds(tags);

			getJdbcTemplate().execute(

					 " begin transaction;"
					+"insert into r_tag_post(id_tag_, id_post_)values" 
						+ rValue + ";"
					+"update fc_tag set num_tagged = num_tagged + 1 where id in"
						+ tagValue + ";"
					+"commit;");
		}
	}

	private List<Integer> getTagIdsOfPost(int postId) {
	
		return getJdbcTemplate().queryForList(
				"select id_tag_ from r_tag_post where id_post_ = " + postId
				,Integer.class);
	}
	/*
	getJdbcTemplate().update(new PreparedStatementCreator() {
		
		@Override
		public PreparedStatement createPreparedStatement(Connection arg0)
				throws SQLException {
			PreparedStatement ps = arg0.prepareStatement(
				BEGIN	
				+"update fc_post set content = ? "
				+ "where id = ?;"				
				+"update fc_post set search_vector = to_tsvector(content) "
				+"where id = ?;"
				+ COMMIT);
			ps.setString(1, content);
			ps.setInt(2, id);
			ps.setInt(3, id);
			return ps;
		}
	});
	*/
	@Override
	public void updateWithTagIds(final FC_Post post, List<Integer> newTagIds) {

		getJdbcTemplate().update(mapper.createUpdate(post));
		getJdbcTemplate().update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
					BEGIN	
					+"update fc_post set content = ? "
					+ "where id = ?;"				
					+"update fc_post set search_vector = to_tsvector(title||content) "
					+"where id = ?;"
					+ COMMIT);
				ps.setString(1, post.content);
				ps.setInt(2, post.id);
				ps.setInt(3, post.id);
				return ps;
			}
		});
		
		updateTagsOf(post.id, newTagIds);
	}

	@Warning(values={"Performance"})
	private void updateTagsOf(int postId, List<Integer> newTagIds) {

		if (null == newTagIds || 0 == newTagIds.size()) {
			List<Integer> removedTagIds = getTagIdsOfPost(postId);
			String rmIds = bracketIds(removedTagIds);
			getJdbcTemplate().execute(
				BEGIN
				+"update fc_tag set num_tagged = num_tagged - 1 "
				+" where id in " + rmIds + ";"
				+"delete from r_tag_post "
				+" where id_tag_ in " + rmIds + ";"
				+COMMIT
			);
			return;
		}
		
		List<Integer> oldTagIds = getTagIdsOfPost(postId);
		
		List<Integer> rm = new ArrayList<Integer>(oldTagIds);

		List<Integer> add = new ArrayList<Integer>(newTagIds);
		
		for (Integer id : oldTagIds) {
			add.remove(id);
		}
		for (Integer id : newTagIds) {
			rm.remove(id);
		}
		
		if (add.size() != 0) {
			
			String addIds = bracketIds(add);
			
			String addIdsId = bracketPostIdnTagIds(postId, add);
			
			getJdbcTemplate().execute(
				BEGIN
				+" update fc_tag set num_tagged = num_tagged + 1 "
				+" where id in " + addIds + ";"
				+"insert into r_tag_post(id_tag_, id_post_) "
				+ " values" + addIdsId + ';'
				+ COMMIT
			);
		}
		if (rm.size() != 0) {

			String rmIds = bracketIds(rm);
			StringBuilder rmIdsId = new StringBuilder(64);
			rmIdsId.append('(');
			rmIdsId.append(bracketPostIdnTagIds(postId, rm));
			rmIdsId.append(')');
			getJdbcTemplate().execute(
			BEGIN
			+" update fc_tag set num_tagged = num_tagged - 1 "
			+" where id in " + rmIds + ";"
			+"delete from r_tag_post "
			+ " where (id_tag_, id_post_) in " + rmIdsId + ';'
			+ COMMIT
			);
		}
		
	}

	@Trigger
	@Override
	public void	discard(int id) {
		getJdbcTemplate().execute(				
			BEGIN
			+"update FC_Post set is_valid = false where id = " + id + ";"
			+"update FC_Comment set is_valid = false where id_post_ =" + id + ';'
			+ COMMIT
		);
	}
	
	@Trigger
	@Override
	public void	recover(int id) {
		getJdbcTemplate().execute(
			BEGIN
			+"update FC_Post set is_valid = true where id = " + id + ";"
			+"update FC_Comment set is_valid = true where id_account_ =" + id + ';'
			+ COMMIT
			);
	}

	/**
	 * currently only English full-text search is supported,
	 * characters of other language will be omitted.
	 * valid key word length < 48
	 * @param offset can be null
	 * @param limit can be null
	 */
	@Override
	public List<FC_Post> simpleSearch(final String rawSentence, 
										final Integer offset,
										final Integer limit) {

		SimpleSearch creator = new SimpleSearch();
		
		Properties args = new Properties();
		args.setProperty(AbstractSearch.RAW_OPTIONAL_WORDS, rawSentence);
		args.setProperty(AbstractSearch.OFFSET, offset.toString());
		args.setProperty(AbstractSearch.LANGUAGE, limit.toString());
		
		creator.setArgs(args);
		
		List<FC_Post> ls = getJdbcTemplate().query(creator.createSQL(),mapper);
		return ls;
	}

	@Override
	public List<FC_Post> search(AbstractSearch creator) {

		return getJdbcTemplate().query(creator.createSQL(), mapper);
	}

//-----------------------------------------------------------------------------
//				for super user
//-----------------------------------------------------------------------------
	@Override
	public List<FC_Post> suGetPosts(int length, int offset, int limit) {
		
		return getJdbcTemplate().query(
				select(length)
				+ " offset " + offset
				+ " limit "  +limit
				, mapper);
	}

	@Override
	public List<FC_Post> suGetPostsOfAuthor(int authotId, int length,
			int offset, int limit) {
		
		return getJdbcTemplate().query(
				select(length)
				+ " where id_author = " + authotId
				+ " offset " + offset
				+ " limit "  +limit
				, mapper);
	}

	@DBSpec(dialect=Dialect.PostgreSQL)
	@Override
	public List<FC_Post> suGetPostsBefore(final Date date, final int length, final int offset,
			final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
//			 "status > 1 and (time_posted::date between ? and ?)",
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						select(length)
						+ " where time_posted::date < ?"
						+ " offset " + offset
						+ " limit " + limit);
				ps.setDate(1, date);
				return ps;
			}
		}, mapper);
	}

	@DBSpec(dialect=Dialect.PostgreSQL)
	@Override
	public List<FC_Post> suGetPostsAfter(final Date date, 
											final int length,
											final int offset,
											final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						select(length)
						+ " where time_posted::date > ?"
						+ " offset " + offset
						+ " limit " + limit
						);
				ps.setDate(1, date);
				return ps;
			}
		}, mapper);
	}

	@DBSpec(dialect=Dialect.PostgreSQL)
	@Override
	public List<FC_Post> suGetPostsBetween(final Date start,
											final Date end,
											final int length,
											final int offset, 
											final int limit) {
		
		return getJdbcTemplate().query(new PreparedStatementCreator() {
//			 "status > 1 and (time_posted::date between ? and ?)",
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						select(length)
						+ " where time_posted::date between ? and ?"
						+ " offset " + offset
						+ " limit " + limit);
				ps.setDate(1, start);
				ps.setDate(2, end);
				return ps;
			}
		}, mapper);
	}

}













