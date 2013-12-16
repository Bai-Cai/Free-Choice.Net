package net.freechoice.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.freechoice.misc.annotation.Warning;

/**
 * @author BowenCai
 *
 */
public class DateUtil {

	private DateUtil(){}
	
//	public static void main(String[] args) {
//		Date toDay = new Date();
//		String s = dateToStr(toDay);
//		System.err.println(s);
//		Date d2 = strToDate(s);
////		System.err.println(d2.equals(toDay) + "\n"  + d2 + "\n"  + toDay);
//		System.err.println(dateToStr(toDay));
//	}
	
	public static final SimpleDateFormat DATE_FORMAT 
			= new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	
	public static final String dateToStr(java.util.Date date) {
		return DATE_FORMAT.format(date);
	}

	public static final Date strToDate(final String str) {
		try {
			return DATE_FORMAT.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage() 
										+ " Caused by " 
										+ e.getCause(), e);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isEqual(final Date d1, final Date d2) {
		return d1.getYear() == d2.getYear()
				&& d1.getMonth() == d2.getMonth()
				&& d1.getDay() == d2.getDay();
	}

	@Warning(values = { "JVM time is not synchronous with the DB" })
	public static boolean hasExpired(final Date d) {
		return new Date().after(d);
	}
	
	@Warning(values = { "JVM time is not synchronous with the DB" })
	public static boolean hasExpired(final String s) {
		return new Date().after(strToDate(s));
	}
	
	@Deprecated
	public static java.sql.Date toSqlDate(Date d) {
		return new java.sql.Date(d.getTime());
	}
	@Deprecated
	public static Date toUtilDate(java.sql.Date d) {
		return new Date(d.getTime());
	}
	
	@SuppressWarnings("unchecked")
	public static<T, E> T dateCast(E d) {
		
		if (d instanceof java.util.Date) {
			return (T) new java.sql.Date(((java.util.Date) d).getTime());
		} else if (d instanceof java.sql.Date) {
			return (T)((java.util.Date)d);
//			return (T) new java.util.Date(((java.sql.Date) d).getTime());
		} else if (d instanceof java.sql.Timestamp) {
			return (T)((java.util.Date)d);
		}
		else {
			throw new IllegalArgumentException(" argument is neither java.util.Data nor java.sql.Data");
		}
	}
	
	public static Timestamp toSqlTimestamp(Date d) {
		return new Timestamp(d.getTime());
	}
}













