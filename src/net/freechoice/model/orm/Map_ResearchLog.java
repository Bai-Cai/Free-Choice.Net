package net.freechoice.model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.freechoice.model.FC_ResearchLog;

import org.springframework.jdbc.core.PreparedStatementCreator;

/**
 * @author BowenCai
 *
 */
public class Map_ResearchLog implements IMapper<FC_ResearchLog> {
	
	@Override
	public FC_ResearchLog mapRow(ResultSet rs, int arg1) throws SQLException {
		
		FC_ResearchLog log = new FC_ResearchLog();
		
		log.id 			= rs.getInt(1);
		log.id_research_ = rs.getInt(2);
		log.name_author = rs.getString(3);
		log.time_posted = rs.getTimestamp(4);
		log.num_read	= rs.getInt(5);
		log.title		= rs.getString(6);
		log.content		= rs.getString(7);
		
		return log;
	}

	@Override
	public PreparedStatementCreator createInsert(final FC_ResearchLog entity) {
		
		return new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				
				PreparedStatement ps = arg0.prepareStatement(
						"insert into fc_research_log("
						+"id_research_,name_author,title,content,search_vector)"
						+"values(?,?,?,?,to_tsvector(? || ?))"
						,RET_ID);

				ps.setInt(1, entity.id_research_);
				ps.setString(2, entity.name_author);
				ps.setString(3, entity.title);
				ps.setString(4, entity.content);
				ps.setString(5, entity.title);
				ps.setString(6, entity.content);
				
				return ps;
			}
		};
	}
//	@Column(length = 32)
//	public String			name_author;
//	
//	@ReadOnly
//	public Timestamp		time_posted;//timestamp
//	
//	@ReadOnly
//	public int				num_read;
//	
//	@Column(length = 96)
//	public String			title;
//
//	@Column()
//	public String			content;
	@Override
	public PreparedStatementCreator createUpdate(final FC_ResearchLog entity) {
		
		return new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0)
					throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(
						"update FC_Research_log "
						+"set name_author = ?,"
						+" num_read = ?,"
						+" title = ?,"
						+"where id = ?;");
				
				ps.setString(1, entity.name_author);
				ps.setInt(2, entity.num_read);
				ps.setString(3, entity.title);
				ps.setInt(4, entity.id);
				
				return ps;
			}
		};
	}

}
