package net.freechoice.model.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @author BowenCai
 *
 */
public class IntExtractor implements ResultSetExtractor<Integer> {

	@Override
	public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {

		Integer i = null;
		rs.next();
		i = rs.getInt(1);
		return i;
	}

}
