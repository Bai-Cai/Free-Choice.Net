package net.freechoice.action;

import java.util.ArrayList;
import java.util.List;

import net.freechoice.dao.IDaoPost;
import net.freechoice.dao.IDaoTag;
import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_Tag;
import net.freechoice.model.FC_User;
import net.freechoice.service.PageService;
import net.freechoice.service.BlogSearchService;
import net.freechoice.util.PageHelper;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author 白强
 * @version 2013-11-21
 * @since 2013-11-13
 * @author BowenCai
 * @version 2013-12-1
 */
public class ActionBlog extends ActionTemplate<FC_Post> {
	
	private static final long serialVersionUID = 1L;

	private IDaoPost 	postDao;
	private IDaoTag 	tagDao;
	/**
	 * 动态 产生 符合当前 负载的 search
	 */
	public BlogSearchService searchService;

	/**
	 * abstract is the text to be displayed on the list it is the first XX chars
	 * of the post
	 * 
	 * abstractLength of the abstract of the post
	 */

	public static int		LENGTH_ABSTRACT = 448;

	public static int 		SEARCH_RESULT_DEFAULT = 20;
	
	private PageService 	pageService;

	/**
	 * ? current page?
	 */
	private FC_Post post;

	private List<FC_Post> postList;

	private List<FC_Tag> tagList;

	private int id;

//	private String keyWords;

	private PageHelper pager;
	/**
	 * currentPage title ? currentPage author name?
	 */
	private int currentPage = PageHelper.INIT;
	private int pagerMethod;
	private int totalRows;
	private int tagId;
	private List<Integer> selectedTagIDs;


	public String save() {
		FC_User user=(FC_User)ActionContext.getContext().getSession().get("user");
		if (user == null){
			return "login";
		}
		post.setStatus((short) 2);
		post.setId_author(user.id);
		postDao.savePostWithTagIds(post, selectedTagIDs);
		return SUCCESS;
	}

	public String update() {
		
		FC_User user=(FC_User)ActionContext.getContext()
						.getSession().get(ACT_USER);
		
		if (user == null){
			return "login";
		}
		
		post.setStatus((short) 2);
		post.setId_author(user.getId());
		postDao.updateWithTagIds(post,selectedTagIDs);
		return SUCCESS;
	}


	public String query() {
		
		FC_User user=(FC_User)ActionContext.getContext().getSession().get("user");
		
		if(user == null){
			return ACT_LOGIN;
		}

		int totalRow = postDao.countDrafsOfAuthor(user.id);
		
		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);
		
		this.setCurrentPage(pager.getCurrentPage());
		
		if (totalRow == 0) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}
		/**
		 * 生成列表，不需要提取博客信息，所以参数为0
		 */
		postList = postDao.getDraftsOfAuthor(user.getId(), 
												0, 
												pager.getStartRow(),
												pager.getPageSize());
		
		return SUCCESS;
	}

	public String delete() {

		postDao.discard(getId());
		return SUCCESS;
	}


	public String get() {
		
		post = postDao.getById(getId());
		
		List<FC_Tag> selectedTags = tagDao.getTagsOfPost(getId(),0,10);
		
		selectedTagIDs = new ArrayList<Integer>();
		
		for(FC_Tag t : selectedTags){
			selectedTagIDs.add(t.id);
		}
		
		postDao.fetchContentFor(post);
		
		tagList = tagDao.getTags(0,10);
		
		return SUCCESS;
	}

	public String queryByTag() {

		int totalRow = postDao.count();

		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);

		this.setCurrentPage(pager.getCurrentPage());

		if (0 == totalRow) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}

		postList = postDao.getPostsOfTag(getTagId(), 
										pager.getStartRow(),
										pager.getPageSize(),
										LENGTH_ABSTRACT);
		return SUCCESS;
	}
	
	public void setSearchEngine(BlogSearchService searchEngine) {
		this.searchService = searchEngine;
	}
	
	public List<Integer> getSelectedTagIDs() {
		return selectedTagIDs;
	}

	public void setSelectedTagIDs(List<Integer> selectedTagIDs) {
		this.selectedTagIDs = selectedTagIDs;
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

	public List<FC_Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<FC_Tag> tagList) {
		this.tagList = tagList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	
	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

//	public void setKeyWords(String keyWords) {
//		this.keyWords = keyWords;
//	}

}
