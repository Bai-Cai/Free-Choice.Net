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
package test.other;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.freechoice.model.FC_User;
import net.freechoice.model.orm.Map_User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

public class T_connection  {

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
		
//		"insert into FC_User(" +
//		"id_photo, name_login, name_display, password, tagline, appendix)" +
//		" values( " + user.id_photo +  ", ?, ?, ?, ?, ?)"
//		);
//		
		PreparedStatement ps = conn.prepareStatement(
				"insert into FC_User(" +
				"id_photo, name_login, name_display, password, tagline, appendix)" +
				" values(?, ?, ?, ?, ?, ?)"
				);
		ps.setInt(1, 2);
		ps.setString(2, "login");
		ps.setString(3, null);
		ps.setString(4, "psw");
		ps.setString(5, null);
		ps.setString(6, null);
		
		ps.executeUpdate();
		
//		@SuppressWarnings("unchecked")
//		List<FC_User> list = mapper.extractData(rs);
//		for (FC_User v_User : list) {
//			System.out.println(v_User.name_login);
//		}
//		' or '1'='1
//		PreparedStatement ps1 = conn.prepareStatement(
//				"select * from v_user where name_login = ?");
//		ps1.setString(1, "' or '1' = '1");
//System.out.println(ps1.toString());
//		rs = ps1.executeQuery();
//		list = mapper.extractData(rs);
//System.out.println("====after:");
//		for (FC_User v_User : list) {
//			System.out.println(v_User.name_login);
//		}
	}

}




