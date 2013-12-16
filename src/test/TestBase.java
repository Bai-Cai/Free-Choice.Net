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

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestBase {

	public JdbcTemplate thisJdbc;
	public ComboPooledDataSource cpds;
	public void connect() {
		
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.postgresql.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/free-choice-v1");
		cpds.setUser("bcgh2013");
		cpds.setPassword("2013.bcgh.start");
		cpds.setMinPoolSize(1);
		cpds.setAcquireIncrement(3);
		cpds.setMaxPoolSize(10);
		
		thisJdbc = new JdbcTemplate(cpds, false);
		
	}
	
	public Connection getConnection() {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public void close() {
		cpds.close();
	}
}
