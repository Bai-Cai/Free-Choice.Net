/*******************************************************************************
 * Copyright (c) 2013 白强.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     白强 - initial API and implementation
 ******************************************************************************/
package net.freechoice.service;

import net.freechoice.util.PageHelper;


/**
 * @author 白强
 */
public class PageService {
	
	public PageService() {
		
	}
	
	int pageSize;

	public PageHelper getPager(int currentPage,
								int pagerMethod,
								int totalRows) {
		
		// 定义pager对象，用于传到页面
		PageHelper pager = new PageHelper(totalRows, pageSize);
		
		// 如果当前页号为空，表示为首次查询该页
		// 如果不为空，则刷新pager对象，输入当前页号等信息
		if (currentPage != PageHelper.INIT) {
			pager.refresh(currentPage);
		}
		
		// 获取当前执行的方法，首页，前一页，后一页，尾页。
		
		switch (pagerMethod) {
		
		case PageHelper.FIRST:
			pager.first();
			break;
		case PageHelper.PREV:
			pager.previous();
			break;
		case PageHelper.NEXT:
			pager.next();
			break;
		case PageHelper.LAST:
			pager.last();
			break;
		default:
			break;
		}
		return pager;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
