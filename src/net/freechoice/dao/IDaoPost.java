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
package net.freechoice.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import net.freechoice.application.search.AbstractSearch;
import net.freechoice.dao.annotation.Trigger;
import net.freechoice.model.FC_Post;

/**
 * There are three kinds of get method
 * 1. get published post, that is, return post with status > 1
 * 2. get post of all status of a specified user(author)
 * 3. get all post for the super user
 * 
 * all get method in Dao_Post returns only limited chars of a post,
 * that is, the very first chars, say 640 chars(150 words)
 * to get full text, call getFullText
 * @author BowenCai
 *
 */

public interface IDaoPost extends IDaoBase<FC_Post> {
	
	/**
	 * 
	 * @return post status > 1
	 */
	public int		countPostsOfUser(int userId);
	/**
	 * 
	 * @return post
	 */
	public int		countDrafsOfAuthor(int userId);
//-----------------------------------------------------------------------------
//				get content of post
//-----------------------------------------------------------------------------
	/**
	 * get full text of this post and assign the content to the post
	 * @param post
	 */
	void			fetchContentFor(FC_Post post);

	/**
	 * 
	 * @param id
	 * @return full text of this post
	 */
	String			getFullText(int id);

//-----------------------------------------------------------------------------
//			get post, for AvgUser and visitor
//-----------------------------------------------------------------------------
	/**
	 * order by time_posted desc
	 * @param userId
	 * @param length
	 * @param offset
	 * @param limit
	 * @return only posts with status > 1 
	 */
	List<FC_Post> 	getPostsByUser(int userId, final int length,
												final int offset,
												final int limit);
	/**
	 * 
	 * @param userId
	 * @return only posts with status > 1 
	 */
	int 			getPostCountOfUser(int userId);
	/**
	 * 
	 * @param userId
	 * @param length
	 * @param offset
	 * @param limit
	 * @return  all posts of this user
	 */
	List<FC_Post> 	getDraftsOfAuthor(int userId, final int length,
												final int offset,
												final int limit);
	/**
	 * 
	 * @param userId
	 * @param length
	 * @param offset
	 * @param limit
	 * @return count of all posts of this user
	 */
	List<FC_Post> 	getDraftsOfAuthor(int userId, final int length,
										final Timestamp offset,
										final int limit);
	/**
	 * 	order by time_posted descend
	 * 
	 * @param length of content
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post> 	getLatestPosts(final int length,
									final int offset,
									final int limit);
	
	List<FC_Post> 	getLatestPosts(final int length,
									final Timestamp offset,
									final int limit);
	/**
	 * order by num_read descend
	 * @param length length of content
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post> 	getPopularPosts(final int length,
											final int offset,
											final int limit);
	
	List<FC_Post> 	getPopularPosts(final int length,
										final Timestamp offset,
										final int limit);

//-----------------------------------------------------------------------------
//			get by time
//-----------------------------------------------------------------------------
	/**
	 * order by time_posted descend
	 * @param time
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post>	getPostsAfter(final Date time,
									final int length,
									final int offset,
									final int limit);

	/**
	 * order by time_posted descend
	 * @param time
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post>	getPostsBefore(final Date time,
									final int length,
									final int offset,
									final int limit);

	/**
	 * order by time_posted descend
	 * @param start : java.sql.Date, not java.util.Date !
	 * @return
	 */
//	@Deprecated
	List<FC_Post>	getPostsBetween(final Date start, final Date end,
									final int length,
									final int offset,
									final int limit);
	/**
	 * order by time_posted descend
	 * @param date
	 * @param offset
	 * @param limit
	 * @return
	 */
//	@Deprecated
	List<FC_Post>	getPostsOnDate(final Date date,
									final int length,
									final int offset,
									final int limit);
//-----------------------------------------------------------------------------
//			get by tag
//-----------------------------------------------------------------------------
	/**
	 * order by time_posted descend
	 * @param teamId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post>	getPostsOfTag(int tagId,
									final int length,
									final int offset,
									final int limit);
	/**
	 * order by time_posted descend
	 * @param teamId
	 * @param offset
	 * @param limit
	 * @return
	 */
	int				getPostCountOfTag(int tagId);
	/**
	 * order by time_posted descend
	 * @param teamId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<FC_Post>	getPostsOfTagIds(final List<Integer> tagIds,
									final int length,
									final int offset,
									final int limit);

//-----------------------------------------------------------------------------
//			RDBMS extension: map function
//-----------------------------------------------------------------------------
	/**
	 * this is a map
	 * @param postId
	 * @param key
	 * @return
	 */
	String			getValue(int postId, final String key);
	
	/**
	 * map: put
	 * @param postId
	 * @param key
	 * @param value
	 */
	void			put(int postId, final String key, final String value);
	
//-----------------------------------------------------------------------------
//					Update	
//-----------------------------------------------------------------------------
	
	/**
	 * update everything about this post
	 * @param post
	 * @param tags
	 */
	void			updateWithTagIds(final FC_Post post,
												final List<Integer> tags);
	
	/**
	 * 
	 * @param post
	 * @param tags
	 */
	void 			savePostWithTagIds(final FC_Post post, final List<Integer> tags);

//-----------------------------------------------------------------------------
//				delete
//-----------------------------------------------------------------------------
	/*
	 update FC_Post set is_valid = false where id = ;
	 update FC_Comment set is_valid = false where id_account_ = 
	 */
	@Trigger
	@Override
	public void		discard(int id);
	
	@Trigger
	@Override
	public void		recover(int id);
	
	
	/**
	 * 
	 * @param sentence raw sentence, will be processed(e.g., filtering, replacing)
	 * 		 in this functiton
	 * @param pageNumber Max result needed, different from other paging functions
	 * 		note that you should not call this function for each page, 
	 * 		because each time this function is invoked, a new full text search is proceed,
	 * 		thus, you should call this function once and once only,
	 * 		and hold the result list and page on yourself.
	 * @return
	 */
	@Deprecated() // search now is controlled
	List<FC_Post>	simpleSearch(final String keywords,
									final Integer offset,
									final Integer limit);
	
	List<FC_Post> 	search(AbstractSearch creator);

	//-----------------------------------------------------------------------------
//	get post, for SuperUser in the dashboard
//-----------------------------------------------------------------------------
	
	List<FC_Post>	suGetPosts(final int length,
								final int offset,
								final int limit);
	
	List<FC_Post>	suGetPostsOfAuthor(final int authotId,
										final int length,
										final int offset,
										final int limit);
	
	List<FC_Post>	suGetPostsBefore(final Date date,
										final int length,
										final int offset,
										final int limit);
	List<FC_Post>	suGetPostsAfter(final Date date,
										final int length,
										final int offset,
										final int limit);

	List<FC_Post>	suGetPostsBetween(final Date start, final Date end,
									final int length,
									final int offset,
									final int limit);

}


/**
 * 
 * @param sentence
 * @return a list of post of which the abstraction 
 * 	is the highlight text from the fc_post_content.
 */
//List<V_Post> 	simpleSearch(final String keywords);

//List<V_Post> 	advancedSearch(final String sentence);

///**
// * @param PreparedStatementCreator
// * @example: get 40 ~ 60 pages :
// * 
// * getPosts(new PreparedStatementCreator() {
//		public PreparedStatement createPreparedStatement(Connection con)
//				throws SQLException {
//			
//			int start = 40;
//			int pageNumber = 20;
//			PreparedStatement ps = con.prepareStatement(
//					"select * from v_post "
//					+"where time_posted::date > to_date('2013-11-09, 'YYYY-MM-DD'')"
//					+"order by time_posted desc"
//					+" offset " + start
//					+" limit " + pageNumber
//					);
//			return ps;
//		}
//	}); 
// * 
// */
//@WarnCompatibility(note = "offset and limit pg only; time_posted::date pg only")
//List<V_Post> 	queryForPosts(PreparedStatementCreator psCreator);

