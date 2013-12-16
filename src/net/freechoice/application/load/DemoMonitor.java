package net.freechoice.application.load;

/**
 * @author BowenCai
 *
 */
public class DemoMonitor implements IMonitor {

	@Override
	public Report reportStatus() {
		
		Report report = new Report();
//		report.sessionCount = XXX;
		report.sessionCount = SessionListener.currentSession.size();
		return report;
	}

}
