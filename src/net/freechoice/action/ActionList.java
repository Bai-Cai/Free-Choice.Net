package net.freechoice.action;

import java.util.List;

import net.freechoice.application.search.ControlledSearch;
import net.freechoice.dao.IDaoComment;
import net.freechoice.dao.IDaoPost;
import net.freechoice.dao.IDaoTag;
import net.freechoice.model.FC_Comment;
import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_Tag;
import net.freechoice.service.BlogSearchService;
import net.freechoice.service.PageService;
import net.freechoice.util.PageHelper;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ActionList.java
 * 
 * @author 白强
 * @version 1.0 2013-11-24
 * 
 * @author BowenCai
 * @version 1.1 2013-12-7
 */
public class ActionList extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	

//	public static final String CH_TAG_LIST ="cache-tagList";
//	public static final String CH_POP_LIST ="cache-popList";
	
	
	private IDaoComment 	commentDao;
	private IDaoPost 		postDao;
	private IDaoTag 		tagDao;

	public static final int	LENGTH_ABSTRACT = 448;
	/**
	 * default number of popular posts
	 */
	public static final int POP_POST_DEFAULT = 6;
	/**
	 * default number of  popular tags
	 */
	public static final int POP_TAG_DEFAULT = 10;

	/**
	 * default number of  search result
	 */
	public static final int SEARCH_RESULT_DEFAULT = 20;
	/**
	 * default number of  comments
	 */
	public static final int COMMENT_DEFAULT = 10;
	
	
	private PageService 	pageService;


	public BlogSearchService searchService;

	/**
	 * ? current page?
	 */
	private FC_Post 		post;
	
	private FC_Comment 		comment;

	private List<FC_Post> 	postList;

	private List<FC_Tag> 	tagList;
	
	private List<FC_Comment> commentList;

	private List<FC_Post> 	popList;
	
	
	private int 			id;

	private String 			keyWords;

	private PageHelper 		pager;
	
	/**
	 * currentPage title ? currentPage author name?
	 */
	private int 			currentPage = PageHelper.INIT;
	
	private int 			pagerMethod;
	private int 			totalRows;
	private int 			tagId;
	private int 			userId;
	
	/*处理逻辑*/
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String addComment(){
		commentDao.save(comment);
		return SUCCESS;
	}
	
	public String query() {

		int totalRow = postDao.count();
		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);
		
		this.setCurrentPage(pager.getCurrentPage());
		
		if (totalRow == 0) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}
		
		popList = postDao.getPopularPosts(0, 0, POP_POST_DEFAULT);
		
		postList=postDao.getLatestPosts(LENGTH_ABSTRACT,
										pager.getStartRow(),
										pager.getPageSize());
		
		tagList = tagDao.getTags(0, POP_TAG_DEFAULT);
		
		return SUCCESS;
	}
	/**
	 * 点击博客，返回全文和comments
	 * @return
	 */
	public String getBlog() {
		
		popList = postDao.getPopularPosts(0, 0, POP_POST_DEFAULT);
		tagList = tagDao.getTags(0, POP_TAG_DEFAULT);
		
		commentList = commentDao.getCommentsOfPost(getId(), 0, COMMENT_DEFAULT);

		post = postDao.getById(getId());
		postDao.fetchContentFor(post);
		
		return SUCCESS;
	}

	public String search() {

		pager = pageService.getPager(this.getCurrentPage(),
										this.getPagerMethod(),
										SEARCH_RESULT_DEFAULT);

		this.setCurrentPage(pager.getCurrentPage());

		this.setTotalRows(SEARCH_RESULT_DEFAULT);

		ControlledSearch searchCreator = searchService.getSuitableSearch();
		searchCreator.setSentence(keyWords);
		
		/**
		 * SQL offset and limit
		 */
		searchCreator.setOffset(0);
		searchCreator.setLimit(SEARCH_RESULT_DEFAULT);
		
		postList = postDao.search(searchCreator);
		
		popList = postDao.getPopularPosts(0, 0, POP_POST_DEFAULT);
		tagList = tagDao.getTags(0, POP_TAG_DEFAULT);
		
		return SUCCESS;
	}

	public String queryByUser() {
		
		int totalRow = postDao.getPostCountOfUser(getUserId());
		
		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);

		this.setCurrentPage(pager.getCurrentPage());

		if (0 == totalRow) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}

		postList = postDao.getPostsByUser(getUserId(), 
										LENGTH_ABSTRACT, 
										pager.getStartRow(), 
										pager.getPageSize());

		tagList = tagDao.getTags(0, POP_TAG_DEFAULT);
		
		popList = postDao.getPopularPosts(0, 0, POP_POST_DEFAULT);

		return SUCCESS;
	}
	
	public String queryByTag() {
		
		int totalRow =postDao.getPostCountOfTag(getTagId());

		pager = pageService.getPager(
						this.getCurrentPage(),
						this.getPagerMethod(),
						totalRow);

		this.setCurrentPage(pager.getCurrentPage());

		if (0 == totalRow) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}
		postList = postDao.getPostsOfTag(getTagId(),
											LENGTH_ABSTRACT,
											pager.getStartRow(),
											pager.getPageSize()
				 );

		tagList = tagDao.getTags(0, POP_TAG_DEFAULT);
		
		popList = postDao.getPopularPosts(0, 0, POP_POST_DEFAULT);
		
		return SUCCESS;
	}
	
	public void setSearchService(BlogSearchService searchService) {
		this.searchService = searchService;
	}
	
	public IDaoComment getCommentDao() {
		return commentDao;
	}

	public void setCommentDao(IDaoComment commentDao) {
		this.commentDao = commentDao;
	}

	public IDaoPost getPostDao() {
		return postDao;
	}

	public void setPostDao(IDaoPost postDao) {
		this.postDao = postDao;
	}

	public IDaoTag getTagDao() {
		return tagDao;
	}

	public void setTagDao(IDaoTag tagDao) {
		this.tagDao = tagDao;
	}

	public int getAbstractLength() {
		return LENGTH_ABSTRACT;
	}

	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public FC_Post getPost() {
		return post;
	}

	public void setPost(FC_Post post) {
		this.post = post;
	}

	public List<FC_Post> getPostList() {
		return postList;
	}

	public void setPostList(List<FC_Post> postList) {
		this.postList = postList;
	}

	public List<FC_Post> getPopList() {
		return popList;
	}

	public void setPopList(List<FC_Post> popList) {
		this.popList = popList;
	}

	public List<FC_Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<FC_Tag> tagList) {
		this.tagList = tagList;
	}

	public List<FC_Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<FC_Comment> commentList) {
		this.commentList = commentList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PageHelper getPager() {
		return pager;
	}

	public void setPager(PageHelper pager) {
		this.pager = pager;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPagerMethod() {
		return pagerMethod;
	}

	public void setPagerMethod(int pagerMethod) {
		this.pagerMethod = pagerMethod;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public FC_Comment getComment() {
		return comment;
	}
	public void setComment(FC_Comment comment) {
		this.comment = comment;
	}
	
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
}


