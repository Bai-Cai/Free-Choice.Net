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

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * @author BowenCai
 *
 */
public class AdvancedSearch extends AbstractSearch {

//	private String language = "english";
	private String AND;
	private String OR;
	private String NOT;
	
	/**
	 * if a certain sentence is longer than maxBlock, 
	 * the rest part of the string will be chopped
	 * @see simpleFilterAndSplite
	 */
	private int maxBlock = 48;
	
	/**
	 * if a certain sentence is longer than maxBlock, 
	 * the rest part of the string will be chopped
	 * @param maxBlock max length of a stripped search sentence.
	 */
	public void setMaxBlock(int maxBlock) {
		this.maxBlock = maxBlock;
	}
	
	/**
	 * headline parameters
	 */
	private int maxWords = 40;
	private int minWords = 15;
	private int maxFragment = 4;
	private int shortWord = 4;
	
	/**
	 * check if StopSel is legal
	 */
	private static final Pattern PLAIN = 
			Pattern.compile("^[a-zA-Z0-9/<>]{2,}$");
	
	private String start = "<b>";
	private String stop = "</b>";
	private String FragmentDelimiter = "<br><br>...&nbsp&nbsp";

	/**
	 * SQL query result
	 */
	private int resultSize = 10;
	private int resultOffset = 0;
	
	
	public void setLanguage(final String l) {
		throw new RuntimeException("Only english is supported");
	}

	public void setAND(String aND) {
		AND = aND;
	}
	public void setOR(String oR) {
		OR = oR;
	}
	public void setNOT(String nOT) {
		NOT = nOT;
	}
	public void setStartAndEnd(final String start, final String end) {
		if (start!= null && PLAIN.matcher(start).matches()
				&& stop != null && PLAIN.matcher(end).matches()) {

			this.start = start;
			this.stop = end;
		} else {
			throw new IllegalArgumentException();
		}
	}
	public String getFragmentDelimiter() {
		return FragmentDelimiter;
	}

	public void setFragmentDelimiter(String fragmentDelimiter) {
		FragmentDelimiter = fragmentDelimiter;
	}
	public void setMaxWords(int maxWords) {
		this.maxWords = maxWords;
	}

	public void setMinWords(int minWords) {
		this.minWords = minWords;
	}

	public void setMaxFragment(int maxFragment) {
		this.maxFragment = maxFragment;
	}
	public void setShortWord(int shortWord) {
		this.shortWord = shortWord;
	}
	
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public void setResultOffset(int resultOffset) {
		this.resultOffset = resultOffset;
	}
	
//	StartSel=<b>, StopSel=</b>,
//			MaxWords=40, MinWords=15, ShortWord=3,
//			MaxFragments=3, FragmentDelimiter=" ... "
	@Override
	public void setArgs(Properties p) {
		
		final String and = p.getProperty(RAW_KEY_WORDS);
		if (null != and) {
			this.AND = and;
		}
		final String or = p.getProperty(RAW_OPTIONAL_WORDS);
		if (null != or) {
			this.OR = or;
		}
		final String not = p.getProperty(RAW_IGNORE_WORDS);
		if (null != not) {
			this.NOT = not;
		}
		
		final String start = p.getProperty(MARKUP_LEFT);
		if (null != start) {
			this.start = start;
		}
		final String stop = p.getProperty(MARKUP_RIGHT);
		if (null != stop) {
			this.stop = stop;
		}
		
		final String maxwd = p.getProperty(MAX_LENGTH);
		if (null != maxwd) {
			this.maxWords = Integer.parseInt(maxwd);
		}
		final String maxfg = p.getProperty(MAX_FRAGMENT);
		if (null != maxfg) {
			this.maxFragment = Integer.parseInt(maxfg);
		}
		final String shortwd = p.getProperty(SHORT_WARD);
		if (null != shortwd) {
			this.shortWord = Integer.parseInt(shortwd);
		}
		final String minwd = p.getProperty(MIX_LENGTH);
		if (null != minwd) {
			this.minWords = Integer.parseInt(minwd);
		}
		
		final String offset = p.getProperty(OFFSET);
		if (null != offset) {
			this.resultOffset = Integer.parseInt(offset);
		}
		final String limit = p.getProperty(LIMIT);
		if (null != limit) {
			this.resultSize = Integer.parseInt(limit);
		}
	}
	
	@Override
	public String createSQL() {
		
		StringBuilder queryBuilder = new StringBuilder(128);
		queryBuilder.append("\'");
		boolean hasLeft = false;
		
		if (null != AND) {
			hasLeft  = true;
			
			StringBuilder blockBuilder = new StringBuilder(32);
			blockBuilder.append('(');
			
			final String[] words = simpleFilterAndSplite(AND, maxBlock);
			for (final String s: words) {
				blockBuilder.append(s).append('&');
			}
			blockBuilder.setCharAt(blockBuilder.lastIndexOf("&"), ')');

			queryBuilder.append(blockBuilder);
		}
		
		if (null != OR) {
			
			if (hasLeft) {
				queryBuilder.append('|');
			}
			
			StringBuilder builder = new StringBuilder(32);
			builder.append('(');
			
			final String[] words = SimpleSearch.simpleFilterAndSplite(OR, maxBlock);
			for (final String s: words) {
				builder.append(s).append('|');
			}
			builder.setCharAt(builder.lastIndexOf("|"), ')');
			
			queryBuilder.append(builder);
		}
		
		if (null != NOT) {
			if (hasLeft) {
				queryBuilder.append('|');
			}
			
			StringBuilder builder = new StringBuilder(32);
			builder.append('(');
			
			final String[] words = SimpleSearch.simpleFilterAndSplite(NOT, maxBlock);
			for (final String s: words) {
				builder.append('!').append(s);
			}
			builder.append(')');
			
			queryBuilder.append(builder);
		}
		
		queryBuilder.append('\'');
		
		StringBuilder headLineBuilder= new StringBuilder(80);
//		ts_headline(content, Q.query, 'MaxWords=96, MinWords=48'),
		headLineBuilder.append("\'MaxWords=" + maxWords 
								+ ",MinWords=" + minWords 
								+ ",MaxFragments=" + maxFragment
								+ ",ShortWord=" + shortWord
								+ ",StartSel=" + start
								+ ",StopSel=" + stop
								+ ",FragmentDelimiter = \"" + FragmentDelimiter + "\""
								);
		headLineBuilder.append("\'");
		
		StringBuilder sqlBuilder = new StringBuilder(
				"with Q as (select to_tsquery(" + queryBuilder.toString() + ")as query "// searchStr.toString()
						+"	) "
						+ SELECT
						+" ts_headline(content,Q.query," + headLineBuilder.toString() + "),"
						+" ts_rank_cd(search_vector, Q.query) AS rank"
						+" from FC_Post, Q "
						+" where FC_Post.is_valid = true and Q.query @@ search_vector "
						+" order by rank desc "
						+" offset " + resultOffset
						+" limit "  +resultSize
				);
		return sqlBuilder.toString();
	}

}

//
//
//String rawSentence  = args.getProperty(RAW_OPTIONAL_WORDS);
//
//StringBuilder searchStr = new StringBuilder(48);
//
//searchStr.append('\'');
//final String[] words = simpleFilterAndSplite(rawSentence);
//for (final String s: words) {
//	searchStr.append(s).append('|');
//}
//searchStr.setCharAt(searchStr.lastIndexOf("|"), '\'');
//
//String string = "'(break&root)|(abc|nasa)'";
//
//StringBuilder query = new StringBuilder(
//		"with Q as (select to_tsquery(" + string + ")as query "// searchStr.toString()
//				+"	) "
//				+ SELECT_COMMON_AND
//				+"	ts_headline(content, Q.query, 'MaxWords=96, MinWords=48'),"
//				+"	ts_rank_cd(search_vector, Q.query) AS rank"
//				+" from FC_Post, Q "
//				+" where FC_Post.is_valid = true and Q.query @@ search_vector "
//				+" order by rank desc "
//		);
//
//String offset = args.getProperty(OFFSET);
//String limit = args.getProperty(LIMIT);
//if (null != offset) {
//	query.append(" offset " + offset);
//}
//if (null != limit) {
//	query.append(" limit " + limit);
//}
////System.out.println(query.toString());
//return query.toString();
