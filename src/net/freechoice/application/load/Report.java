package net.freechoice.application.load;


/**
 * @author BowenCai
 *
 */
public class Report {


	public double 		cpuLoad;
	public double		heapUsage;
	public double		nonHeapUsage;
	public int 			sessionCount;
	
	public Report() {
	}
	
	public Report(Report report) {
		this(report.cpuLoad,
				report.heapUsage,
				report.nonHeapUsage,
				report.sessionCount);
	}
	
	public Report(double cpuLoad, double heapUsage, double nonHeapUsage,
			int sessionCount) {
		
		this.cpuLoad = cpuLoad;
		this.heapUsage = heapUsage;
		this.nonHeapUsage = nonHeapUsage;
		this.sessionCount = sessionCount;
	}
}
