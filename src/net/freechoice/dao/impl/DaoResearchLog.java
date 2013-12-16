	/**
	 * @author BowenCai
	 *
	 */
package net.freechoice.dao.impl;

import java.util.List;

import net.freechoice.dao.IDaoResearchLog;
import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_ResearchLog;
import net.freechoice.model.orm.Map_ResearchLog;

/**
 * @author BowenCai
 *
 */
public class DaoResearchLog extends DaoTemplate<FC_ResearchLog> implements IDaoResearchLog {
	
	public DaoResearchLog() {
		super(FC_ResearchLog.class, new Map_ResearchLog());
	}

	private static final String SELECT= 
			"SELECT	id,"
			+"name_author,"
			+"time_posted,"
			+"num_read,"
			+"title,"
			+"content "
			+"FROM FC_Research_log ";
	
	@Override
	public FC_ResearchLog getById(int id) {
		
		return getJdbcTemplate().queryForObject(
				SELECT + "where is_valid = true and id = " + id,
				mapper);
	}

	@Override
	public int countLogsOfResearch(int rechId) {
		
		return getJdbcTemplate().queryForInt(
				"select count(id) from fc_research_log "
				+" where is_valid = true and id_research_ = " + rechId);
	}

	@Override
	public List<FC_ResearchLog> getLogsOfResearch(int rechId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void fetchContentFor(FC_Post post) {
		// TODO Auto-generated method stub
		
	}

	
}
