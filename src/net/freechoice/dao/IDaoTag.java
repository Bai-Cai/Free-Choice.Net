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

import java.util.List;

import net.freechoice.dao.annotation.Trigger;
import net.freechoice.model.FC_Tag;
/**
 * 
 * @author BowenCai
 *
 */
public interface IDaoTag extends IDaoBase<FC_Tag> {
	
//	public static final String ORDERBY_TA
	
	/**
	 *  order by num_tagged
	 * @param offset
	 * @param tagNumber
	 * @return
	 */
	List<FC_Tag> 	getTags( int offset, int tagNumber);
	
	List<FC_Tag>	getTagsByName(final String name);
	
	List<String> 	getAllTagNames();

	List<FC_Tag> 	getTagsOfPost(int postId, int offset, int tagNumber);
	
	List<String> 	getTagNamesOfPost(int postId, int offset, int limit);
	
	
	@Trigger
	@Override
	public void	discard(int id);
	
	@Trigger
	@Override
	public void	recover(int id);
}
