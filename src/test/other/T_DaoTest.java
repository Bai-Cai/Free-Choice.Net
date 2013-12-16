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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.freechoice.dao.impl.DaoTemplate;
import net.freechoice.dao.impl.DaoUser;
import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_Tag;
import net.freechoice.model.FC_User;
import net.freechoice.model.orm.Map_Post;
import net.freechoice.model.orm.Map_Tag;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class T_DaoTest extends T_Dao{

	JdbcTemplate tmp;
	@Before
	public void setUp() throws Exception {
		super.setup();
//		tmp = new JdbcTemplate(dataSource);
//		DataSource dSource = new Dat
	}

	@After
	public void tearDown() throws Exception {
		super.clean();
	}

	Map_Tag mapper = new Map_Tag();
	

	int ddd(String xx) {
		xx = xx.toLowerCase().substring(2);
		return 1;
	}
	@Test
	public void sss() {
		String xxxString = "IQOJDIO";
		System.out.println(ddd(xxxString));
		System.out.println(xxxString);
	}
	
	public void test3() throws SQLException {
		PreparedStatement ps = conn.prepareStatement(
				"select content from fc_post where time_posted::date = ?");
				ps.setDate(1, null);
				System.out.println(ps);
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}

//		Dao_User dao_User = new Dao_User();
////		dao_User.is
//		V_Post post;
//		Map_Post postmap = new Map_Post();
//		ResultSet rs = conn.prepareStatement("select * from v_post where id = 4 ").executeQuery();
//		rs.next();
//		post = postmap.mapRow(rs, 1);
//		System.out.println(post.content);
//		System.out.println(post.title);
	}
	
	
	public void test2() throws SQLException, IOException {
System.out.println(System.nanoTime() / 1000000);
		ResultSet rs = conn.createStatement().executeQuery(
				"select content from fc_Post where id = 15"
				);
		rs.next();
		String text = rs.getString(1);
		System.out.println(text.length());
System.out.println(System.nanoTime() / 1000000);
		@SuppressWarnings("resource")
		FileChannel rwChannel = new RandomAccessFile("textfile.txt", "rw").getChannel();
		ByteBuffer wrBuf = rwChannel.map(
				FileChannel.MapMode.READ_WRITE, 0, text.length() + 10);
		int lg = text.length() / 1000000;
		int pos = 0;
		byte[] buf = new byte[text.length() / 1000000];
System.out.println(System.nanoTime() / 1000000);
		for (int i = 0; i < 1000000; i++) {
			System.arraycopy(text.getBytes(), pos, buf, 0, lg);
			 wrBuf.put(buf);
			 pos += lg;
		}
System.out.println(System.nanoTime() / 1000000);
		rwChannel.close();
	}
	
//	352939488
//	352940066
//	352940312
//	352942040
//	@Test
	public void test1() throws SQLException {
System.out.println("Start!");
		File file = new File("斗破苍穹.txt");
		if (!file.exists() || !file.isFile() || file.isDirectory()) {
			return;
		}
		
		StringBuilder sBuilder = new StringBuilder((int) (file.length() + 1));
System.out.println(System.nanoTime() / 1000000);
		  try (Scanner sc = new Scanner(file, "UTF-8")) {
		        while (sc.hasNextLine()) {
		            sBuilder.append(sc.nextLine());
		        }
		        // note that Scanner suppresses exceptions
		        if (sc.ioException() != null) {
		            throw sc.ioException();
		        }
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		  System.out.println(System.nanoTime() / 1000000);
			String insert = "insert into fc_post(content)values("
					+ DaoTemplate.quote(sBuilder.toString())
					+")";
System.out.println(System.nanoTime() / 1000000);
		java.sql.Statement statement = conn.createStatement();
		statement.execute(insert);
System.out.println(System.nanoTime() / 1000000);
System.out.println("finished");
	}
	

	public void test() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		FC_Tag tag = new FC_Tag();
		tag.content = "content";
//		mapper.mapToTable(get, tag);
		
		String namString = (String) FC_User.class.getField("TABLE_NAME").get(FC_User.class.newInstance());
		
		System.out.println(namString + '\n' + (String) FC_User.class.getField("TABLE_NAME").toString());
	
//		
		List<Integer> taIds = new ArrayList<Integer>();
		
		taIds.add(1);
		taIds.add(34);
		taIds.add(45);
		
		taIds.add(11);
		taIds.add(15);
		taIds.add(1556756867);
		taIds.add(25476991);
		
//		StringBuilder sBuilder = new StringBuilder("(");

//		
//		for (Integer v_Tag : taIds) {
//			sBuilder.append(v_Tag).append(',');
//		}
//		sBuilder.setCharAt(sBuilder.lastIndexOf(","), ')');
//		System.out.println(sBuilder.toString());
		
		StringBuilder sBuilder = new StringBuilder();
		final String postId = String.valueOf(999);
		
		for (Integer v_Tag : taIds) {
			sBuilder.append("( " + postId + ", "+ v_Tag+ "),");
		}
		sBuilder.deleteCharAt(sBuilder.lastIndexOf(","));
		System.out.println(
				"insert into r_tag_post(id_post_, id_tag_)values" + sBuilder.toString());
	}

	
}




