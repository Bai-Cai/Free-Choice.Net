package net.freechoice.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * @author BowenCai
 *
 */
public class WebContext {
	
	public static final Locale LOCATION = Locale.UK;
	
	public static final Calendar CALENDAR = Calendar.getInstance(LOCATION);
	
	public static Date now() {
		return CALENDAR.getTime();
	}

}
