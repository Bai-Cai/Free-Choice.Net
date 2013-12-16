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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import net.freechoice.dao.impl.DaoUser;
import net.freechoice.model.FC_User;
import net.freechoice.model.orm.Map_User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TUser extends T_Dao {

	@Before
	public void setUp() throws Exception {
		setup();
	}

	@After
	public void tearDown() throws Exception {
		clean();
	}
//	'2013-11-20 +08:00:00'
//	'2013-11-20 12:00:48.756000 +08:00:00'


	
	
	public void testt() throws SQLException {
		PreparedStatement pss = conn.prepareStatement
				("?, ?, ?, ?, ?");
		
		pss.setString(1, "'fwe'sd");
		pss.setDate(2, new Date(new java.util.Date().getTime()));
		pss.setBigDecimal(3, new BigDecimal("123456"));
		pss.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
		pss.setDouble(5, 123456789.87654321);
		
		System.err.println(pss.toString());
//		Dao_User userDao = new Dao_User();
//		userDao.setJdbcTemplate(jdbcTemplate);
//		
//		
//		V_User user = userDao.getById(7);
//		System.out.println(user.name_display);
	}
	
	public void test() throws SQLException {
//		List<FC_User> users;
//		PreparedStatement ps = conn.prepareStatement("select * from v_user");
//		ResultSet rs = ps.executeQuery();
//		users = new Map_User().extractData(rs);
//		System.out.println(users.size());
//		for (FC_User v_User : users) {
//			System.out.println(v_User.id + "  " + v_User.name_login + "   " + v_User.password);
//		}
//		FC_User v_User = users.get(8);
//		System.out.println(v_User.id + "  " + v_User.name_login + "   " + v_User.password);
//		ps.close();
//		
//		
//		PreparedStatement ps1 = conn.prepareStatement("select * from v_user where id = 8");
//		ResultSet rs1 =  ps1.executeQuery();
//		
//		System.out.println(rs1.getInt("id"));
//		
//		
//		FC_User uu = new Map_User().mapRow(rs1, 99);
//		System.out.println(uu.name_login);
//		System.out.println(rs.getInt(1));
//		ps1.close();
		
//		user = new V_User();
//		user.id 		= rs.getInt(1);
//		user.id_photo 	= rs.getInt(2);
//		user.name_login = rs.getString(3);
//		user.name_display = rs.getString(4);
//		user.password 	= rs.getString(5);
//		user.tagline 	= rs.getString(6);
//		user.appendix 	= rs.getString(7);
		

	
	}

}






