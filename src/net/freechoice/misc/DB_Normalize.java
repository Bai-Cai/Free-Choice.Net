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
package net.freechoice.misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.freechoice.service.EncryptionService;
import test.TestBase;

/**
 * @author BowenCai
 * 
 */
public class DB_Normalize {

	/**
	 * @param args
	 * @throws SQLException
	 */
	
	static TestBase connPool = new TestBase();
	
	public static void main(String[] args) throws SQLException {
		
		do_FC_User_Psw();
		
	}

	public static void do_FC_User_Psw() throws SQLException {

		connPool.connect();
		connPool.cpds.setAutoCommitOnClose(true);
		
		Connection con1 = connPool.getConnection();
		
		PreparedStatement ps = con1
				.prepareStatement("select password, id from fc_user");
		
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			
			String rawPsw = rs.getString(1);
			int id = rs.getInt(2);
			
			// System.err.println("id:" + id + "   psw:" + rawPsw);
			if (!net.freechoice.util.StrUtil.likeHashedPsw(rawPsw)) {
				
System.err.print(rawPsw + "\t");

				rawPsw = EncryptionService.transformPassword(rawPsw);
				
System.err.println(rawPsw);

				Connection con2 = connPool.getConnection();
				PreparedStatement psUp = con2
						.prepareStatement("update fc_user set password = ? where id = ?");
				psUp.setString(1, rawPsw);
				psUp.setInt(2, id);
				psUp.execute();
				psUp.close();
//System.err.println(psUp.toString());
				con2.close();
			}
		}
		// System.err.println("done.");
		ps.close();
		rs.close();
		con1.close();

		connPool.close();
	}
	

	public static void do_FC_Tag_num_tagged() throws SQLException {


		connPool.connect();
		
		List<Integer> ids = new ArrayList<Integer>(128);
		List<Integer> taggedNum = new ArrayList<Integer>(128);
		
		Connection c = connPool.getConnection();
		Statement stmt = c.createStatement();
		
		ResultSet rs = stmt.executeQuery(
				"select id , num_tagged from fc_tag where is_valid = true");
		
		while (rs.next()) {
			ids.add(rs.getInt(1));
			taggedNum.add(rs.getInt(2));
		}
		rs.close();
		
		for (int i = 0; i != ids.size(); ++i) {
			
			ResultSet rrs = stmt.executeQuery(
			"select count(id_tag_)from r_tag_post as R "
			+"inner join fc_post as P where P.is_valid = true"
			+"	and  R.id_tag_ = Tg.id and Tg.id = " + ids.get(i));
			rrs.next();
			int fromDB = rrs.getInt(1);
			
			if(fromDB != taggedNum.get(i))  {
				System.err.println(taggedNum + "\n correct " + fromDB);
				stmt.executeUpdate(
						"update fc_tag set num_tagged = " + fromDB 
						+ " where id = "  +ids.get(i));
			}
			
			
//			stmt.execute(
//			"update fc_tag as Tg set num_tagged = "
//			+"		 ("
//			+"			 select count(id_tag_)"
//			+"	from r_tag_post as R inner join fc_post as P where P.is_valid = true"
//			+"			 and  R.id_tag_ = Tg.id"
//			+"		 ) where Tg.id = " + i.toString()			
//					);
		}
		c.commit();
		stmt.close();
		c.close();
	}
	
	public static void do_FC_Post_num_comment() {
		
	}

}
