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
package net.freechoice.util;

import java.util.Formatter;
import java.util.regex.Pattern;

/**
 * 
 * @author BowenCai
 *
 */
public class StrUtil {
	
	private StrUtil(){}

	public static void main(String[] args) {
//		InetAddress
		String ipv4 = "255.255.255.255";
		long ipv4L = ipV4ToLong(ipv4);
		System.out.println(ipv4);
		System.out.println(longToIpV4(ipv4L));
System.err.println(ipv4.length());

	}
	
		  
	public static final Pattern PTN_EMAIL;
	public static final Pattern PTN_LOGIN_NAME;
	public static final Pattern PTN_PASSWORD;
	public static final Pattern PTN_HASHED_PASSWORD;

	public static final Pattern PTN_IPV4;
	
	public static final Pattern PTN_IPV6_HEX4DECCOMPRESSED_REGEX;
	public static final Pattern PTN_IPV6_6HEX4DEC_REGEX;
	public static final Pattern PTN_IPV6_HEXCOMPRESSED_REGEX;
	public static final Pattern PTN_IPV6_REGEX;
	

	public static final Formatter FORMATTER;

//	private sta
	static {
		PTN_EMAIL = Pattern.compile(
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		
		/**
		 *  only: 'a' to 'z' or 'A' to 'Z' 
		 *  	5 < string.length < 31
		 */
		PTN_LOGIN_NAME = Pattern.compile("^[a-zA-Z0-9._-]{5,31}$");

		/**
		 * must contain: digital numbers 0 to 9, char 'a' to 'z'
		 * must not contain: black space ' '
		 */
		PTN_PASSWORD = Pattern.compile(
				"^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,31}$");
		
		PTN_IPV4 = Pattern.compile(
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		
		PTN_HASHED_PASSWORD = Pattern.compile(
					"^[a-zA-Z0-9+/]{48}$"
					);
	
		PTN_IPV6_HEX4DECCOMPRESSED_REGEX = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?) ::((?:[0-9A-Fa-f]{1,4}:)*)(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");
		PTN_IPV6_6HEX4DEC_REGEX = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}:){6,6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");;
		PTN_IPV6_HEXCOMPRESSED_REGEX = Pattern.compile("\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)\\z");
		PTN_IPV6_REGEX = Pattern.compile("\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z");
	
		FORMATTER = 
				new Formatter(WebContext.LOCATION);
	}
	
	public static final boolean isEmail(final String email) {
		return PTN_EMAIL.matcher(email).matches();
	}
	/**
	 *@param loginName only: 'a' to 'z' or 'A' to 'Z' or '0' to '9'
	 *  	5 < string.length < 31
	 */
	public static final boolean isloginName(final String loginName) {
		return PTN_LOGIN_NAME.matcher(loginName).matches();
	}
	/**
	 * 
	 * @param psw must contain: digital numbers 0 to 9, char 'a' to 'z'
	 * 			must not contain: black space ' '
	 * @return
	 */
	public static final boolean isPassword(final String psw) {
		return PTN_PASSWORD.matcher(psw).matches();
	}
	
	public static final boolean isIpV4(final String psw) {
		return PTN_IPV4.matcher(psw).matches();
	}
	
	public static final boolean likeIpV6(final String password) {
		
		return PTN_IPV6_HEX4DECCOMPRESSED_REGEX.matcher(password).matches()
				|| PTN_IPV6_6HEX4DEC_REGEX.matcher(password).matches()
				|| PTN_IPV6_HEXCOMPRESSED_REGEX.matcher(password).matches()
				|| PTN_IPV6_REGEX.matcher(password).matches();
	}

	public static boolean likeHashedPsw(String password) {

		return PTN_HASHED_PASSWORD.matcher(password).matches();
	}
	
	public static final long ipV4ToLong(final String ipAddress) {
		long result = 0;
		String[] atoms = ipAddress.split("\\.");

		for (int i = 3; i >= 0; i--) {
			result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
		}
		return result & 0xFFFFFFFF;
	}

	public static final String longToIpV4(long ip) {
		StringBuilder sb = new StringBuilder(15);

		for (int i = 0; i < 4; i++) {
			sb.insert(0, Long.toString(ip & 0xff));

			if (i < 3) {
				sb.insert(0, '.');
			}
			ip >>= 8;
		}

		return sb.toString();
	}
	
	
	
	public static final String format(final String str, final Object...objs) {
		return FORMATTER.format(str, objs).toString();
	}
	
	public static final int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE };

	// Requires positive x
	public static final int strLength(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}
	
    // Requires positive x
	public static final int strLength(long x) {
		
        long p = 10;
        for (int i=1; i<19; i++) {
            if (x < p)
                return i;
            p = 10*p;
        }
        return 19;
    }
    
//    public AbstractStringBuilder append(boolean b) {
//        if (b) {
//            ensureCapacityInternal(count + 4);
//            value[count++] = 't';
//            value[count++] = 'r';
//            value[count++] = 'u';
//            value[count++] = 'e';
//        } else {
//            ensureCapacityInternal(count + 5);
//            value[count++] = 'f';
//            value[count++] = 'a';
//            value[count++] = 'l';
//            value[count++] = 's';
//            value[count++] = 'e';
//        }
//        return this;
//    }
}
