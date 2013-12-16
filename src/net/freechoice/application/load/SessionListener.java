package net.freechoice.application.load;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * count session number
 *  this class is ugly ! it will be integrated into Spring sooner.
 *  
 * @author BowenCai
 *
 */
public class SessionListener implements HttpSessionListener {

	public static Map<String, String> currentSession
								= new ConcurrentHashMap<>();

	public static final String NULL = "ss";
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
//		if (currentSession == null) {
//			currentSession = new ConcurrentHashMap<>();
//		}
		String sessionId = arg0.getSession().getId();
//		currentSession.put(sessionId, NULL);
		currentSession.put(sessionId, NULL);

System.err.println("session created " + currentSession.size() + "  "+ sessionId + "  " + currentSession.get(sessionId));
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {

		String sessionId = arg0.getSession().getId();
		currentSession.remove(sessionId);
System.err.println("session destroyed " + currentSession.size() + "   " + sessionId);
	}

}
