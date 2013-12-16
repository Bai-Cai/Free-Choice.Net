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
import net.freechoice.model.FC_Comment;


/**
 * 
 * @author BowenCai
 *
 */
public interface IDaoComment extends IDaoBase<FC_Comment> {
	
	
	
	List<FC_Comment> getCommentsOfPost(int postId, int offset, int limit);

	List<FC_Comment> getCommentsOfName(final String name);
	
	List<FC_Comment> getCommentsOfEmail(final String email);

	@Trigger
	@Override
	public int save(final FC_Comment comment);
	
	@Trigger
	@Override
	public void discard(int id);
	/**
	 * prefer this one to discard(int id)
	 */
	@Trigger
	@Override
	public void discard(final FC_Comment com);
	
	/**
	 * some entity may override this to enable action chain, 
	 * e.g., invalidate cascade. 
	 */
	@Trigger
	@Override
	public void recover(int id);
	
	/**
	 * some entity may override this to enable actions chain, 
	 * e.g., invalidate cascade. 
	 */
	@Trigger
	@Override
	public void delete(int id);
	
	/**
	 * prefer this one to delete(int id)
	 */
	@Trigger
	@Override
	public void delete(final FC_Comment com);
}




