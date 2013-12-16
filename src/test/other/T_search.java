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

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import net.freechoice.application.search.AbstractSearch;
import net.freechoice.application.search.AdvancedSearch;
import net.freechoice.application.search.SimpleSearch;
import net.freechoice.model.FC_Post;
import net.freechoice.model.orm.Map_Post;
import net.freechoice.service.EncryptionService;
import net.freechoice.util.StrUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class T_search  extends T_Dao{

	@Before
	public void setUp() throws Exception {
		setup();
	}

	@After
	public void tearDown() throws Exception {
		clean();
	}

	@Test
	public void tes() {
		String str = "ppssww";
		System.err.println(EncryptionService.transformPassword(str));
	}
	
	public void t() {
		String a = null;
		String b = " aaa " + a;
		System.out.println(b);
	}
	
//	@Test
	public void test() throws SQLException {
		
		String sentence = "const";
		SimpleSearch creator = new SimpleSearch();
		
		
		Properties pro = new Properties();
		pro.setProperty(AbstractSearch.RAW_OPTIONAL_WORDS, sentence);
		pro.setProperty(AbstractSearch.OFFSET, "0");
		pro.setProperty(AbstractSearch.LIMIT, "10");
		creator.setArgs(pro);
		
		
		AdvancedSearch advancedSearch = new AdvancedSearch();
		advancedSearch.setArgs(pro);
		
//		advancedSearch.setAND("functions上的 三code");
//		advancedSearch.setNOT("modify");
//		advancedSearch.setOR("painful啥难三角地unfortunately|const");
		
		System.err.println(creator.createSQL());
		System.err.println(advancedSearch.createSQL());
		
		PreparedStatement ps = conn.prepareStatement(advancedSearch.createSQL());
				ResultSet rs = ps.executeQuery();

		FC_Post post;
		while (rs.next()) {
			System.err.println("\n-----------------------------------");
			post = new Map_Post().mapRow(rs, 0);
			System.out.println(
				post.name_author
				+"\n" + post.title
				+"\n" + post.content.length()
				+"\n" + post.content
					);
		}
		ps.close();
		rs.close();
		ps = conn.prepareStatement("select content from fc_post where id=0");
		rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println("\n-----------------------------------");
//			post = new Map_Post().mapRow(rs, 0);
			System.out.println(rs.getString(1));
//				post.name_author
//				+"\n" + post.title
//				+"\n" + post.content.length()
//				+"\n" + post.content
//					);
		}
		

	}

}
