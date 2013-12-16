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

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class T_Dao {
	
	Connection conn;
	
	JdbcTemplate jdbcTemplate;
	DataSource dataSource;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setup() {
		
		try {
			Class.forName("org.postgresql.Driver");	
			String url = "jdbc:postgresql://localhost:5432/free-choice-v1";
			String url1 = "jdbc:postgresql://localhost:5432/test";
			conn = DriverManager.getConnection(
				url,
					"bcgh2013",
					"2013.bcgh.start");
			
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			try {
				cpds.setDriverClass("org.postgresql.Driver");
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
			cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/free-choice-v1");
			cpds.setUser("bcgh2013");
			cpds.setPassword("2013.bcgh.start");
			cpds.setMinPoolSize(5);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(80);
			dataSource = cpds;
			
			jdbcTemplate = new JdbcTemplate(dataSource, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void clean() {
		try {
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
