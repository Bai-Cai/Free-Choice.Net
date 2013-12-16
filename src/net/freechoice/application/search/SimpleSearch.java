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
package net.freechoice.application.search;




/**
 * 
 * @author BowenCai
 *
 */
public class SimpleSearch extends AbstractSearch {


	@Override
	public final String createSQL() {

		String rawSentence  = args.getProperty(RAW_OPTIONAL_WORDS);
		
		StringBuilder searchStr = new StringBuilder(64);
		
		searchStr.append('\'');
		final String[] words = simpleFilterAndSplite(rawSentence, 48);
		for (final String s: words) {
			searchStr.append(s).append('|');
		}
		int lastChar = searchStr.lastIndexOf("|");
		if (lastChar != -1) {
			searchStr.setCharAt(lastChar, '\'');
		}
		
//		String string = "'!NASA|(code&computer)|(const)'"; // test

		StringBuilder query = new StringBuilder(
				"with Q as (select to_tsquery(" + searchStr.toString() + ")as query "// searchStr.toString()
						+"	) "
						+ SELECT
						+"	ts_headline(content, Q.query,"
						+" 		'MaxWords=64, MinWords=12, ShortWord=4,"
						+"		MaxFragments=2,"
						+" 		FragmentDelimiter=\"<br><br>...&nbsp&nbsp\" ' "
						+"  ),"
						+" ts_rank_cd(search_vector, Q.query) AS rank"
						+" from FC_Post as P, Q "
						+" where P.is_valid = true and Q.query @@ P.search_vector "
						+" order by rank desc "
				);

		String offset = args.getProperty(OFFSET);
		String limit = args.getProperty(LIMIT);
		if (null != offset) {
			query.append(" offset " + offset);
		}
		if (null != limit) {
			query.append(" limit " + limit);
		}
//System.out.println(query.toString());
		return query.toString();
	}


}
//		
//		StringBuilder builder = new StringBuilder(36);
//		
//		builder.append('\'');
//		for (final String s : keywords) {
//			builder.append(s).append('|');
//		}
//		builder.setCharAt(builder.lastIndexOf("&"), '\'');
//		
//		return getJdbcTemplate().query(
//				"with Q as ("
//				+"		select to_tsquery(" + builder.toString() + ") AS query"
//				+"	) select	id,"
//				+"				status,"
//				+"				id_author,"
//				+"				name_author,"
//				+"				month_posted,"
//				+"				time_posted,"
//				+"				num_read,"
//				+"				num_comment,"
//				+"				title,"
//				+"				ts_headline(content, Q.query),"
//				+"				ts_rank_cd(search_vector, Q.query) AS rank"
//				+"		from FC_Post, Q"
//				+"		where Q.query @@ search_vector"
//				+"		order by rank desc"
//				+"		offset" + offset
//				+"		limit " + postNumber,
//				rowMapper);
