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

import net.freechoice.dao.impl.DaoTemplate;
import net.freechoice.model.IModel;

/**
 *  interface for all entities, 
 *	9 method common declared.
 * @see net.freechoice.dao.DaoAux for implementaion
 * @param <T> T extends IModel
 * @since version-1
 * @author BowenCai
 */
public interface IDaoBase<T extends IModel> {
	
	/**
	 * @return number of valid entities
	 */
	int 			count();
	
	/**
	 * 
	 * @return number of all entities, including invalid
	 */
	int 			countAll();

//	/**
//	 * be careful with this method, do not use it unless you really need the data.
//	 * @return valid only
//	 */
//	@Deprecated
//	List<T> 		getAll();
	
	/**
	 * 
	 * @param id
	 * @return valid or invalid entity
	 */
	T				getById(int id);
	
	/**
	 * save entity
	 * @param entity
	 * @return
	 */
	int				save(final T entity);
	
	/**
	 * @see DaoTemplate
	 * invalidation is cascaded, 
	 * implemented by database triggers
	 * 	i.e. invalidate user -> invalidate account and profile of this user 
	 * @see note.DatabaseLogic.txt for detail
	 * @param entity
	 */
	void			discard(int id);

	/**
	 * wrapper
	 * @param entity
	 */
	void			discard(final T entity);
	/**
	 * 	validate object so that the object can be seen in the view
	 * @param id
	 */
	void			recover(int id);

	/**
	 * delete entity, 
	 * for rows affected, see DatabaseLogic
	 * @param id
	 */
	void 			delete(int id);
	/**
	 * 
	 * @param entity
	 */
	void 			delete(final T entity);
	
	/**
	 * update all attributes of a entity
	 * use with caution
	 * @see net.freechoice.model.orm
	 * @param entity
	 */
	void			update(final T entity);
	
}





