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


import java.io.File;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.freechoice.dao.impl.DaoTemplate;
import net.freechoice.dao.impl.DaoPost;
import net.freechoice.dao.impl.DaoUser;
import net.freechoice.model.FC_Post;
import net.freechoice.model.FC_Tag;
import net.freechoice.model.FC_User;
import net.freechoice.util.Base64;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.support.rowset.SqlRowSet;



public class Test_User extends TestBase {
	
	DaoUser userDao;
	
	@Before
	public void setUp() throws Exception {
		connect();
		cpds.setAutoCommitOnClose(true);
		userDao = new DaoUser();
		userDao.setJdbcTemplate(thisJdbc);
	}

	@After
	public void tearDown() throws Exception {
		cpds.close();
	}
	@Test
	public void ddd() throws SQLException {
		Connection connection = cpds.getConnection();
//		PreparedStatement ps = connection.prepareStatement(
//				"update fc_user set name_display = ? where id = 10");
//		ps.setString(1, "123456");
//		ps.execute();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select name_display from fc_user where id = 10");
		rs.next();
		String ss = rs.getString(1);
		System.err.println(ss.length());

		System.err.println(ss.trim().length());
	}

//	public void sas() {
//		DaoPost postDao = new DaoPost();
//		postDao.setDataSource(cpds);
//		List<Integer> ids = new ArrayList<>();
//		ids.add(4);
//		ids.add(2);
////		ids.add(1);
//		postDao.updateTagsOf(5, ids);
//	}
	

	
	
	public void x() {
		System.err.println(FC_User.serialID32 + "\n" + FC_Post.serialID32);
		String string = "蔡博文发送啊我株式/株価 - Yahoo!ファイナンス怕大军阀昆明阿伟武林风今晚i";
		System.out.println(string.length() + "  " + string.getBytes().length + "   "  +string.toCharArray().length);
		String bs = Base64.encodeToString(string.getBytes());
		String bss = Base64.encodeToString(string.getBytes());
		
		System.out.println("\n" + bs+"\n\n" + bss);
		
//		String bsString = Base64.
//		String bssString = FC_Base64.decodeFromString(bss);
		
//		System.out.println(bs.equals(bssString));
	
	}
	
	public void sdaf() {
		FC_Post post1 = new FC_Post();
		post1.id_author = 7;
		post1.name_author = "user7";
		post1.title = "PySonar2 Successfully Integrated with Sourcegraph.com";
		post1.content = "asdvsThe difference between Sourcegraph and other code search sites is: Sourcegraph truly understands code and produces accurate results. Sourcegraph lets you semantically search and browse opensource code on the web in a similar fashion as you do locally with IDEs. It also finds users of your code worldwide, and show exactly how they use your code.";
		
		DaoPost postDao = new DaoPost();
		postDao.setDataSource(cpds);
		int id = postDao.save(post1);
//		System.out.println(id);

	}

	
	
	public void xx1() throws SQLException {
		System.out.println(File.separator);
		
		PreparedStatement ps = cpds.getConnection()
				.prepareStatement("select amount from fc_transaction where id = 12");
		

		BigDecimal db = new BigDecimal("15.465482652446425");
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			BigDecimal b = rs.getBigDecimal(1);
			System.out.println(b.equals(db) + "\n" + b);
		}
	}
	
	
//	@Test
	public void ttx() throws SQLException {
		BigDecimal db = new BigDecimal("15.465482652446425");
		PreparedStatement ps = cpds.getConnection()
				.prepareStatement("insert into fc_transaction(id_account_, amount)" +
						"values(5, ?)");
		ps.setBigDecimal(1, db);
		ps.execute();
		
//		ResultSet rs = 
	}
	
	
	public void ssss() {
		DaoPost postDao = new DaoPost();
		postDao.setJdbcTemplate(thisJdbc);
		
		List<FC_Tag> tags = new ArrayList<>();
		
		FC_Tag tag1 = new FC_Tag();
		tag1.id = 1;
		tags.add(tag1);
		
		FC_Tag tag2 = new FC_Tag();
		tag2.id = 2;
		tags.add(tag2);
		
		FC_Tag tag4 = new FC_Tag();
		tag4.id = 4;
		tags.add(tag4);
		
		
		FC_Post post = new FC_Post();
		post.id = 5;

		
	}
	
	
	public void sss() throws SQLException {
		PreparedStatement ps = cpds.getConnection().prepareStatement(
				"select * from v_tag where id = 1");
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			
		}
	}
	
	public void xx() {
	
		String name = "GoW";
		String sql = "select password, id from fc_user " 
						+" where name_login = "+ DaoTemplate.quote(name);
						
						
		SqlRowSet rowSet = userDao.getJdbcTemplate().queryForRowSet(sql);

		System.out.println((rowSet.isLast()));
		
		if (rowSet.next()) {

			System.out.println((rowSet.isLast()));
			
			System.out.println(sql + "\n");
			String result = rowSet.getString("id");
			System.err.println(result);
		}
	}
	

	//七八九十1234567890abcdefghij
	public void ss() {
		String s1 = "想必世界上所有的计算机学生都知道图灵的大名和事迹，";
		String s2 = "因为美国计算机器学会（ACM）每年都会颁发“图灵奖”，它被誉为计算机科学的最高荣誉。";
		String s3 = "大部分的计算机学生都会在某门课程（比如“计算理论”）学习“图灵机”的原理。然而，有多少人知道丘奇是什么人，";


		String se1 = "<b>const</b> char* in2)? It all depends on";
		String se2 = "whether foo2 actually modifies its argument or not. If it does, then ";
		String se3 = "there is no way you can declare in2 as <b>const</b>, consequently you cannot declare ... taking a ";

		String s = _transform(s1);
		String sw = _transform(s2);
		String sq = _transform(s3);
		String ss = _transform(se1);
		String sss = _transform(se2);
		String sz = _transform(se3);
		System.out.println(s.toCharArray().length);
		
	}

	/**
	 * To enable assert
	 * 
	 * 
	 *  Run 
	 *  	-> Run Configurations
	 *  		-> Arguments
	 *  			-> VM Arguments
	 *  				-> add '-ea'
	 *  					-> click 'apply'
	 */
//	@Test
	public void tt() {

System.out.println("start");
		FC_User user = new FC_User();
		user.path_avatar = "/etc/xxx.png";
		user.name_display = "God of War";
		user.name_login   = "GoW";
		user.tagline = "die!";
		user.password = ("You won't see this again");
		user.email = "fromNoWhereToNoWhere@void.com";
System.out.println("save!");
		int id = userDao.save(user);
		System.out.println(id);
//		System.out.println("start");
	}
	
	public void test() {
		
		/**
		 * 1.we can get user
		 */
		FC_User user = userDao.getById(9);
		
		/**
		 * 2. user is right!
		 */
		assert(user.name_login.equals("user1 name login"));
		/**
		 * if assertion failed, print real value
		 */

		System.out.println(user.path_avatar);
		/**
		 * test discard
		 */
		assert(userDao.count() == 3);
		userDao.discard(user);
		assert(userDao.count() == 2);
		assert(userDao.countAll() == 3);

		/**
		 * test recover
		 */
		userDao.recover(user.id);
		assert(userDao.count() == 3);
		assert(userDao.countAll() == 3);
	}
	
	private static final String HASH_ALGO = "SHA-1";
	private static final String ENCRYPT_ALGO = "AES";
	private static final byte[] FC_SECRET_KEY_1 = new byte[] { 
		'f', 'r', 'e', 'e','-', 'c', 'h', 'o', 'i', 'c', 'e', '-', 'd', 'e', 'v', '-' };

	
	public static final String _transform(final String psw){
		
		MessageDigest messageDigest = null;
		Cipher cipher = null;
		SecretKey key = new SecretKeySpec(FC_SECRET_KEY_1, ENCRYPT_ALGO);
		byte[] encryptedPswHash = null;
		
		try {
			
			cipher = Cipher.getInstance(ENCRYPT_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, key);	
			encryptedPswHash = cipher.doFinal(psw.getBytes());
			
			messageDigest = MessageDigest.getInstance(HASH_ALGO);
	
			messageDigest.update(encryptedPswHash);
			byte[] pswHash = messageDigest.digest();

//			encryptedPswHash = cipher.doFinal(pswHash);
			
			
			
System.out.println("psw: " + psw.getBytes().length);

System.out.println("encryptedPswHash: " + encryptedPswHash.length);
System.out.println("encryptedPswHash Str: " + Base64.encodeToString(encryptedPswHash).length());// + Base64.encodeToString(encryptedPswHash));

System.out.println("pswHash: " + pswHash.length);
System.out.println("pswHash Str: " + Base64.encodeToString(pswHash).length());


		} catch (NoSuchAlgorithmException 
				| NoSuchPaddingException 
				| InvalidKeyException 
				| IllegalBlockSizeException 
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(encryptedPswHash);
	}
}




