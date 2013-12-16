package net.freechoice.action;


import java.util.List;

import net.freechoice.dao.IDaoTag;
import net.freechoice.model.FC_Tag;
import net.freechoice.model.FC_User;
import net.freechoice.service.PageService;
import net.freechoice.util.PageHelper;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author 白强
 * @author BowenCai
 * @version 2013-11-21
 * @since 2013-11-14
 */
public class ActionTag  extends ActionTemplate<FC_Tag> {
	
	private static final long serialVersionUID = 1L;
	
	
	private IDaoTag 	tagDao;

	private PageService pageService;


	private List<FC_Tag> list;
	private FC_Tag 		tag;
	/**
	 * id of what?
	 */
	private int 		id;
	private PageHelper 	pager;
	private int 		currentPage;
    private int 		pagerMethod;
    private int 		totalRows;
    


	public String save() {
		
		FC_User user=(FC_User)ActionContext
				.getContext().getSession().get(ACT_USER);
		
		if(user==null){
			return ACT_LOGIN;
		}
		tagDao.save(tag);
		return SUCCESS;
	}


	public String update() {
		
		FC_User user=(FC_User)ActionContext
				.getContext().getSession().get(ACT_USER);
		
		if(user==null){
			return ACT_LOGIN;
		}
		tagDao.update(tag);
		return SUCCESS;
	}


	public String query() {
		
		FC_User user=(FC_User)ActionContext
				.getContext().getSession().get(ACT_USER);
		
		if(user==null){
			return ACT_LOGIN;
		}
		int totalRow = tagDao.count();
		
		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);
		
		this.setCurrentPage(pager.getCurrentPage());
		
		if (totalRow == 0) {
			this.setTotalRows(1);
		} else {
			this.setTotalRows(totalRow);
		}
		list = tagDao.getTags(pager.getStartRow(), pager.getPageSize());
		return SUCCESS;
	}


	public String delete() {

		FC_User user=(FC_User)ActionContext
				.getContext().getSession().get(ACT_USER);
		
		if(user==null){
			return ACT_LOGIN;
		}
		if (tagDao.getById(getId()) != null) {
			tagDao.delete(getId());
			return SUCCESS;
		} else {
			return ERROR;
		}

	}


	public String get() {
		tag = tagDao.getById(getId());
		return SUCCESS;
	}
	
	public IDaoTag getTagDao() {
		return tagDao;
	}

	public void setTagDao(IDaoTag tagDao) {
		this.tagDao = tagDao;
	}
	
	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public FC_Tag getTag() {
		return tag;
	}

	public void setTag(FC_Tag tag) {
		this.tag = tag;
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

	public List<FC_Tag> getList() {
		return list;
	}

	public void setList(List<FC_Tag> list) {
		this.list = list;
	}
}
