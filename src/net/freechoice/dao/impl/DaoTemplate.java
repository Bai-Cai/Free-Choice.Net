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

import java.util.List;

import javax.persistence.Table;

import net.freechoice.dao.IDaoBase;
import net.freechoice.misc.annotation.Warning;
import net.freechoice.model.IModel;
import net.freechoice.model.FC_Post;
import net.freechoice.model.orm.IMapper;
import net.freechoice.model.orm.IntExtractor;
import net.freechoice.model.orm.StrExtractor;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * 
 * @author BowenCai
 *
 */
public abstract class DaoTemplate<T extends IModel> 
						extends JdbcDaoSupport implements IDaoBase<T> {

	
	public static final String TIME_DESCEND	= " order by time_posted desc ";

	public static final String TIME_ASCEND	= " order by time_posted asc ";

	public static final String BEGIN 		= " begin transaction;";

	public static final String COMMIT 		= " commit;";
	
	public static final StrExtractor STR_EXTRACTOR = new StrExtractor();
	public static final IntExtractor INT_EXTRACTOR = new IntExtractor();
	
	
	
	public	final	IMapper<T> 		mapper;
	
	protected final	Class<T> 		type;
	protected final	String			tableName;
	
	public DaoTemplate(final Class<T> t, final IMapper<T> m) {
		mapper = m;
		type = t;
		tableName = getTableNameByJPA(type);
//		tableName = getTableName(type);
	}
	
	@Override
	final public int count() {
		
		return getJdbcTemplate().queryForInt(
				"select count(1) from " + tableName + " where is_valid = true"
				);
	}

	@Override
	final public int countAll() {
		return getJdbcTemplate().queryForInt(
				"select count(1) from " + tableName
				);
	}

	@Override
	public int save(final T entity) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(mapper.createInsert(entity), keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * some entity may override this to enable action chain, 
	 * e.g., invalidate cascade. 
	 */
	@Override
	public void discard(int id) {
		getJdbcTemplate().execute(
				"update " + tableName 
				+ " set is_valid = false where id = " + id);
	}
	
	@Override
	public void discard(T entity) {
		discard(entity.getId());
	}
	/**
	 * some entity may override this to enable action chain, 
	 * e.g., invalidate cascade. 
	 */
	@Override
	public void recover(int id) {
		getJdbcTemplate().execute(
				"update " + tableName
				+ " set is_valid = true where id = " + id);
	}
	/**
	 * some entity may override this to enable actions chain, 
	 * e.g., invalidate cascade. 
	 */
	@Override
	public void delete(int id) {
		getJdbcTemplate().execute(
				"delete from " + tableName
				+" where id = " + id);
	}

	@Override
	public void delete(T entity) {
		delete(entity.getId());
	}
	
	@Warning(values="all attribute will be updated")
	@Override
	public void update(final T entity) {
		getJdbcTemplate().update(mapper.createUpdate(entity));
	}


	public final<E extends IModel> String 
	bracketModelIds(final List<E> ls) {
		
		StringBuilder builder = new StringBuilder(32);
		builder.append("(");
		for(IModel v : ls) {
			builder.append(v.getId() +  ",");
		}
		builder.setCharAt(builder.lastIndexOf(","), ')');
		return builder.toString();
	}
	
	public final String 
	bracketIds(final List<Integer> ls) {
		
		StringBuilder builder = new StringBuilder(32);
		builder.append("(");
		for(Integer v : ls) {
			builder.append(v +  ",");
		}
		builder.setCharAt(builder.lastIndexOf(","), ')');
		return builder.toString();
	}
	
	public final<E extends IModel> String 
	bracketPostnTags(final FC_Post post, final List<E> ls) {		
		
		StringBuilder builder = new StringBuilder(64);
		final String id1 = String.valueOf(post.getId());
		for(E v : ls) {
			builder.append("(" + v.getId() + "," + id1 + "),");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		return builder.toString();
	}
	
	public final String
	bracketPostIdnTagIds(final int postId, final List<Integer> ls) {		
		
		StringBuilder builder = new StringBuilder(64);
		final String id1 = String.valueOf(postId);
		for(Integer v : ls) {
			builder.append("(" + v + "," + id1 + "),");
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		return builder.toString();
	}
	
	
	public static final String getTableNameByJPA(final Class<?> type) {
		
		if (type.isAnnotationPresent(Table.class)) {
			return type.getAnnotation(Table.class).name();
		} else {
			throw new RuntimeException(" No field annotated 'Table'");
		}
	}
	public static final String getTableName(final Class<?> type) {
		
		return type.getSimpleName();
	}
	/**
	 * @param parameter
	 *            to be inserted into SQL statement
	 * @return a quoted string
	 */
	@Warning(values={"injection"})
	@Deprecated
	public static final String quote(final String param) {

		int paraLen = param.length();
		StringBuffer buf = new StringBuffer((int) (paraLen * 6 / 5));
		buf.append('\'');
		//
		// Note: buf.append(char) is _faster_ than
		// appending in blocks, because the block
		// append requires a System.arraycopy()....
		// go figure...

		for (int i = 0; i < paraLen; ++i) {
			char c = param.charAt(i);

			switch (c) {
			// case 0: /* Must be escaped for 'mysql' */
			// buf.append('\\');
			// buf.append('0');
			//
			// break;

			case '\n': /* Must be escaped for logs */
				buf.append('\\');
				buf.append('n');

				break;

			case '\r':
				buf.append('\\');
				buf.append('r');

				break;

			case '\\':
				buf.append('\\');
				buf.append('\\');

				break;

			case '\'':
				buf.append('\\');
				buf.append('\'');

				break;

			// this.usingAnsiMode = !this.connection.useAnsiQuotedIdentifiers();

			case '"': /* Better safe than sorry */
				// if (this.usingAnsiMode) {
				// buf.append('\\');
				// }

				buf.append('"');

				break;

			case '\032': /* This gives problems on Win32 */
				buf.append('\\');
				buf.append('Z');

				break;

			default:
				buf.append(c);
			} // switch
		} // for

		buf.append('\'');
		return buf.toString();
	} // func: quote
}
