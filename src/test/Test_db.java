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

import java.io.ByteArrayInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.Cookie;

import net.freechoice.dao.impl.DaoPost;
import net.freechoice.model.FC_Post;
import net.freechoice.model.orm.Map_Post;
import net.freechoice.model.orm.StrExtractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.PreparedStatementCreator;

public class Test_db  extends TestBase{

	@Before
	public void setUp() throws Exception {
		connect();
	}

	/**
	 * @author BowenCai
	 *
	 */

	@After
	public void tearDown() throws Exception {
		cpds.close();
	}

	@Test
	public void sda() {

		DaoPost postDao = new DaoPost();
		postDao.setDataSource(cpds);
		
		
		String str = postDao.getJdbcTemplate().query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						"select content from fc_post where id = 49");
				return ps;
			}
		}, new StrExtractor());
		System.err.println(str);
	}
	
	
	public void tttt() {
		DaoPost postDao = new DaoPost();
		postDao.setDataSource(cpds);
		
		FC_Post post = postDao.getById(49);
		postDao.fetchContentFor(post);
System.err.println(post.content);
		
//		String postContent = thisJdbc.queryForObject("", String.class);
	}
	
	
	public void awd() {

		OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean(); 
		System.out.println(bean.getSystemLoadAverage());
		
	}
	
	
	public void test() throws SQLException, ClassNotFoundException {
		String string = "圣诞节vnws小";
		ByteArrayInputStream bain = new ByteArrayInputStream(string.getBytes());
		
		System.out.println(string.getBytes().length);
		Class.forName("org.postgresql.Driver");	
		String url = "jdbc:postgresql://localhost:5432/free-choice-v1";
		String url1 = "jdbc:postgresql://localhost:5432/test";
		Connection  conn = DriverManager.getConnection(
			url,
				"bcgh2013",
				"2013.bcgh.start");
//		Connection conn = cpds.getConnection();
		PreparedStatement ps = conn.prepareStatement("insert into psw(bs128)values(?)");
		
		ps.setBinaryStream(1, bain);
//		ps.setBytes(1, string.getBytes());
		ps.execute();
//		ps.setObject(1, string.getBytes(), POst)
		conn.close();
	}
}











