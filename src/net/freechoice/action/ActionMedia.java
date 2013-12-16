package net.freechoice.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.freechoice.model.FC_User;
import net.freechoice.service.PageService;
import net.freechoice.util.PageHelper;

import org.apache.commons.io.FileUtils;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 白强
 * @version 2013-11-14
 */
public class ActionMedia extends ActionSupport {

	String PATH = ServletActionContext.getServletContext().getRealPath(
			"/upload/");

	private static final long serialVersionUID = 1L;
	private PageService pageService;

	private String name;
	private PageHelper pager;
	private int currentPage;
	private int pagerMethod;
	private int totalRows;
	private List<String> fileList;
	private FC_User user;
	/**
	 * 上传的文件
	 */
	private File image;
	
	/**
	 * 文件名称
	 */
	private String imageFileName; // 
	
	/**
	 * 文件类型
	 */
	private String imageContentType; // 

	public PageService getPageService() {
		return pageService;
	}

	public void setPageService(PageService pageService) {
		this.pageService = pageService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}

	public FC_User getUser() {
		return user;
	}

	public void setUser(FC_User user) {
		this.user = user;
	}

	
	public String save() throws IOException {
		
		FC_User user=(FC_User)ActionContext
					.getContext().getSession().get(ActionTemplate.ACT_USER);
		
		if(user==null){
			return ActionTemplate.ACT_LOGIN;
		}
		
		if (image != null) {
			
			File savefile = new File(new File(PATH+File.separator
											 + user.getName_login().trim()),
											 imageFileName);
			
			if (!savefile.getParentFile().exists()) {
				savefile.getParentFile().mkdirs();
			}
			
			FileUtils.copyFile(image, savefile);
System.err.println("文件上传成功");
			return SUCCESS;
		} else {
System.err.println("文件上传失败");
		return INPUT;
		}
	}

	public String query() {
		
		FC_User user=(FC_User)ActionContext.getContext().getSession().get("user");
		if(user==null){
			return ActionTemplate.ACT_LOGIN;
		}
		
		File file = new File(PATH+File.separator+user.getName_login().trim());
		if(!file.exists()){
			file.mkdirs();
		}
		int totalRow = file.list().length;
		pager = pageService.getPager(this.getCurrentPage(),
				this.getPagerMethod(), totalRow);
		this.setCurrentPage(pager.getCurrentPage());

		if (totalRow == 0) {
			this.setTotalRows(1);
			return SUCCESS;
		} else {
			this.setTotalRows(totalRow);
		}
		
        fileList=new ArrayList<String>();
        
		for (int i = 0; i < totalRow; i++) {
			fileList.add(file.list()[i]);
		}
		if (pager.getCurrentPage() == pager.getTotalPages()) {
			fileList = fileList.subList(
					(pager.getCurrentPage() - 1) * pager.getPageSize(),
					pager.getTotalRows());
		} else {
			fileList = fileList.subList(
					(pager.getCurrentPage() - 1) * pager.getPageSize(),
					pager.getCurrentPage() * pager.getPageSize());
		}
		return SUCCESS;
	}

	public String delete() {
		FC_User user=(FC_User)ActionContext.getContext().getSession().get("user");
		if(user==null){
			return ActionTemplate.ACT_LOGIN;
		}
		File file = new File(PATH +File.separator+ user.getName_login().trim() + getName());
System.err.println("delete" + file);
		file.delete();
		return SUCCESS;
	}

}
