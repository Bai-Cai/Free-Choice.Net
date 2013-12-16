package test;

import static org.junit.Assert.*;

import net.freechoice.service.EncryptionService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.other.T_Dao;

public class T_psw {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @author BowenCai
	 *
	 */

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		T_Dao dao = new T_Dao();
		
//		dao.getJdbcTemplate()
		
		String psw = "Not yet implemented";
		String hashed = EncryptionService.transformPassword(psw);
		String nnew =  "Not yet implemented";
		
		System.err.println(EncryptionService.validatePasswords(nnew, hashed));
		
	}
}





