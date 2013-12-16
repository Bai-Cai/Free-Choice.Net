package net.freechoice.dao;

import java.util.List;

import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_ResearchLog;

/**
 * @author BowenCai
 *
 */
public interface IDaoResearchLog extends IDaoBase<FC_ResearchLog> {

	/**
	 * 
	 * @return post status > 1
	 */
	public int		countLogsOfResearch(int rechId);
	/**
	 * 
	 * @return post status > 1
	 */
	public List<FC_ResearchLog>		getLogsOfResearch(int rechId);
	
	/**
	 * get full text of this log and assign the content to the post
	 * @param post
	 */
	void			fetchContentFor(FC_Post post);
	
}
