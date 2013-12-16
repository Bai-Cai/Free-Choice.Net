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


/**
 * 
 * ts_headline uses the original document, not a tsvector summary, 
 * so it can be slow and should be used with care. 
 * A typical mistake is to call ts_headline for every matching document 
 * when only ten documents are to be shown.
 * 
 * @author BowenCai
 *
 */

public abstract class AbstractSearch {

/*
 	language
 	max size
 	min size
 	StartSel
 */

	public static final String LEGAL_CHARS = "[^\\dA-Za-z.=_+\\s-]";
	/**
	 * RAW_KEY_WORDS key words that cannot be omitted
	 * word1 & word2 & word3
	 */
	public static final String RAW_KEY_WORDS = "RAW_KEY_WORDS";
	/**
	 * RAW_OPTIONAL_WORDS that can be omitted, 
	 * word1 | word2 | word3
	 */
	public static final String RAW_OPTIONAL_WORDS = "RAW_OPTIONAL_WORDS";
	

	/**
	 * words to be avoided
	 */
	public static final String RAW_IGNORE_WORDS = "RAW_IGNORE_WORDS";
	/**
	 * search sentence language
	 */
	public static final String LANGUAGE = "LANGUAGE";
	
	/**
	 * max length of the searched text, default 64
	 */
	public static final String MAX_LENGTH = "MAX_LENGTH";
	
	/**
	 * min length of the searched text, default 15
	 */
	public static final String MIX_LENGTH = "MIX_LENGTH";
	
	/**
	 * max fragment of search results
	 */
	public static final String MAX_FRAGMENT = "MAX_FRAGMENT";
	
	/**
	 * words of this length or less will be dropped at the start and end of a headline
	 */
	public static final String SHORT_WARD = "SHORT_WARD";

	
	/**
	 * left markup, default '<b>'
	 */
	public static final String MARKUP_LEFT = "MARKUP_LEFT";
	
	/**
	 * right markup, default '</b>'
	 */
	public static final String MARKUP_RIGHT = "MARKUP_RIGHT";
	
	/**
	 * offset, it is nullable
	 */
	public static final String OFFSET = "OFFSET";
	
	/**
	 * result limit, can be null
	 */
	public static final String LIMIT = "LIMIT";
	
	public static final String SELECT = 
			" select id,"
			+"status,"
			+"id_author,"
			+"name_author,"
			+"time_posted,"
			+"num_read,"
			+"num_comment,"
			+"title,";
	
	protected Properties	args;

	public void setArgs(final Properties p) {
		this.args = p;
	}


	public abstract String createSQL();
	
	/**
	 * @param keywords
	 * @return
	 */
	public static final String[] simpleFilterAndSplite(String keywords, int limit) {

		keywords = keywords.replaceAll(LEGAL_CHARS, " ");
		keywords = keywords.replaceAll("\\s+", " ");
		if (keywords.length() > limit) {
			return keywords.substring(0, limit).split(" ");
		} else {
			return keywords.split(" ");
		}
//		return keywords.substring(0, 10);//.split(" ");
	}
}






//<p>
//  To present search results it is ideal to show a part of each document and
//  how it is related to the query. Usually, search engines show fragments of
//  the document with marked search terms.  <span
//      class="PRODUCTNAME">PostgreSQL</span>
//  provides a function <code
//      class="FUNCTION">ts_headline</code> that
//  implements this functionality.
//</p>
//
//<p>
//  <code
//      class="FUNCTION">ts_headline</code> accepts a document along
//  with a query, and returns an excerpt from
//  the document in which terms from the query are highlighted.  The
//  configuration to be used to parse the document can be specified by
// config if config
//  is omitted, the
//default_text_search_config configuration is used.
//</p>
//<p>
//  If an string is specified it must
//  consist of a comma-separated list of one or more
//pairs.
//  The available options are:
//
//
//      <p>
//          the strings with
//     which to delimit query words appearing in the document, to distinguish
//     them from other excerpted words.  You must double-quote these strings
//     if they contain spaces or commas.
//      </p>
//
//<p>
//  determine the longest and shortest headlines to output.
//</p>
//
//<p>
//  <tt
//      class="LITERAL">ShortWord</tt>: words of this length or less will be
//     dropped at the start and end of a headline. The default
//     value of three eliminates common English articles.
//</p>
//
//<p>
//  the whole document will be used as the
//     headline, ignoring the preceding three parameters.
//</p>
//
//<p>
//  maximum number of text excerpts
//     or fragments to display.  The default value of zero selects a
//     non-fragment-oriented headline generation method.  A value greater than
//     zero selects fragment-based headline generation.  This method
//     finds text fragments with as many query words as possible and
//     stretches those fragments around the query words.  As a result
//     query words are close to the middle of each fragment and have words on
//     each side. Each fragment will be of at most
//          and
//     words of lengthor less are dropped at the start
//     and end of each fragment. If not all query words are found in the
//     document, then a single fragment of the first 
//          in the document will be displayed.
//</p>
//
//<p>
//  When more than one fragment is
//     displayed, the fragments will be separated by this string.
//</p>
//
//<p>
//  Any unspecified options receive these defaults:
//
//</p>
//
//<p>
//</p>
//<p>
//  For example:
//
//SELECT ts_headline('english',
//'The most common type of search
//is to find all documents containing given query terms
//and return them in order of their similarity to the
//query.',
//to_tsquery('query &amp; similarity'));
//                      ts_headline                         
//------------------------------------------------------------
//containing given &lt;b&gt;query&lt;/b&gt; terms
//and return them in order of their &lt;b&gt;similarity&lt;/b&gt; to the
//&lt;b&gt;query&lt;/b&gt;.
//
//SELECT ts_headline('english',
//'The most common type of search
//is to find all documents containing given query terms
//and return them in order of their similarity to the
//query.',
//to_tsquery('query &amp; similarity'),
//'StartSel = &lt;, StopSel = &gt;');
//                    ts_headline                      
//-------------------------------------------------------
//containing given query  terms
//and return them in order of their &lt;similarity&gt; to the
//uery
//</p>







