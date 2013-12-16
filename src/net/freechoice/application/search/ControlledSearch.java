package net.freechoice.application.search;

/**
 * @author BowenCai
 *
 */
public class ControlledSearch extends AbstractSearch {
	
	public String 	sentence;
	public String 	headLineCfg;
	public int		sentenceLimit;

	public int		offset;
	public int		limit;

	
	public void setHeadLineCfg(String headLineCfg) {
		this.headLineCfg = headLineCfg;
	}

	public void setSentenceLimit(int sentenceLimit) {
		this.sentenceLimit = sentenceLimit;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public String createSQL() {
		
		StringBuilder searchStr = new StringBuilder(64);
		
		searchStr.append('\'');
		final String[] words = simpleFilterAndSplite(sentence, sentenceLimit);
		for (final String s: words) {
			searchStr.append(s).append('|');
		}
		int lastChar = searchStr.lastIndexOf("|");
		if (lastChar != -1) {
			searchStr.setCharAt(lastChar, '\'');
		}
		
		StringBuilder query = new StringBuilder(384);
		query.append("with Q as (select to_tsquery(")
										.append(searchStr)
										.append( ")as query)");
//		+"	ts_headline(content, Q.query,"
//		+" 		'MaxWords=64, MinWords=12, ShortWord=4,"
//		+"		MaxFragments=2,"
//		+" 		FragmentDelimiter=\"<br><br>...&nbsp&nbsp\" ' "
//		+"  ),"
//		HighlightAll=FALSE

		query.append(SELECT)
				.append(" ts_headline(content, Q.query, ")
				.append(headLineCfg).append("), ")
			.append(" ts_rank_cd(search_vector, Q.query) AS rank ")
			.append(" from FC_Post as P, Q ")
			.append(" where P.is_valid = true and Q.query @@ P.search_vector ")
			.append(" order by rank desc ");

			query.append(" offset " + offset);
			query.append(" limit " + limit);
//System.err.println(query.toString());
		return query.toString();
	}

}
