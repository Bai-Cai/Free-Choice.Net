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
package test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.freechoice.model.FC_User;
import net.freechoice.model.orm.Map_User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

public class T_user  {

	Connection conn;
	Statement statement;
	ResultSet rs;
	Map_User mapper;

	@Before
	public void setUp() throws Exception {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/free-choice-v1",
					"bcgh2013",
					"2013.bcgh.start");
		statement = conn.createStatement();
		rs = statement.executeQuery("select * from v_user");
		mapper = new Map_User();
	}

	@After
	public void tearDown() throws Exception {

		rs.close();
		statement.close();
		conn.close();
	}

	@Test
	public void test() throws DataAccessException, SQLException {
		
//		ResultSetMetaData metaData = rs.getMetaData();
//
//		int count = metaData.getColumnCount();
//		for (int i = 1; i <= count; i++)
//		{
//			System.out.println(i + "  " + metaData.getColumnName(i));
//		}
//		Boolean boo = null;
//		boolean b = boo;
//		List<FC_User> list = mapper.extractData(rs);
//		for (FC_User v_User : list) {
//			System.out.println(v_User.name_login);
//		}
	}

}




