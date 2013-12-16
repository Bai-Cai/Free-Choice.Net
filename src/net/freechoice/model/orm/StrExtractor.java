package net.freechoice.model.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @author BowenCai
 *
 */
public class StrExtractor implements ResultSetExtractor<String> {

	@Override
	public String extractData(ResultSet rs) throws SQLException,
												DataAccessException {

		String str = null;
		rs.next();
		str = rs.getString(1);
		return str;
	}

}
