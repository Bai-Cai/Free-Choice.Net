package net.freechoice.application.load;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;

/**
 * @author BowenCai
 *
 */
public class ServerMonitor implements IMonitor {

	MemoryMXBean 			memBean;
	OperatingSystemMXBean 	osBean;
	
	ServerMonitor() {
		memBean = ManagementFactory.getMemoryMXBean();
		osBean = ManagementFactory.getOperatingSystemMXBean();
	}
	
	@Override
	public Report reportStatus() {
		
		MemoryUsage heapUsage = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeapUsage = memBean.getNonHeapMemoryUsage();
		
		long heapInit = heapUsage.getInit();
		long heapUsed = heapUsage.getUsed();
		long heapMax  = heapUsage.getCommitted();
		
		double heap = ((double)(heapUsed - heapInit)) 
								/ (heapMax - heapInit);

		long nonHeapInit = nonHeapUsage.getInit();
		long nonHeapUsed = nonHeapUsage.getUsed();
		long nonHeapMax  = nonHeapUsage.getCommitted();
		
		double nonHeap = ((double)(nonHeapUsed - nonHeapInit)) 
								/ (nonHeapMax - nonHeapInit);
		
		double cpu = osBean.getSystemLoadAverage();
		
		Report report = new Report();
		report.cpuLoad = cpu;
		report.heapUsage = heap;
		report.nonHeapUsage = nonHeap;

		report.sessionCount = SessionListener.currentSession.size();
	
		return report;
	}
	
	

}


/*
 
  String ipAddr = ((ServletRequestAttributes)RequestContextHolder
  			.currentRequestAttributes())
           .getRequest().getRemoteAddr();
           .......................
As far as I know you can't using the HttpSessionListener interface.

You can get and log the IP Address 
	from "ServletRequest.getRemoteAddr()"
	but you don't have access to the servlet request 
	from HttpSessionListener or from HttpSessionEvent.

Best idea would to have a javax.servlet.Filter 
which gets the IP address and sets it as a session attribute 
if not already present. 
(You could also do the logging if not already present).


		  
//	    // Total number of processors or cores available to the JVM 
//	    System.out.println("Available processors (cores): " + 
//	        Runtime.getRuntime().availableProcessors());

		    long maxMemory = Runtime.getRuntime().maxMemory();
		    // Maximum amount of memory the JVM will attempt to use 
		    System.out.println("Maximum amount of memory the JVM will attempt to use (MB): " + 
		        (maxMemory) / (1<<20) 
		        );
		    // Total memory currently in use by the JVM 
		    System.out.println("Total memory currently in use (MB): " + 
		        Runtime.getRuntime().totalMemory() / (1<<20));
		    
	    // Total amount of free memory available to the JVM 
	    System.out.println("Total amount of free memory available to the JVM (MB): " + 
	        Runtime.getRuntime().freeMemory() / (1<<20));

	    // This will return Long.MAX_VALUE if there is no preset limit 

*/

////Vector<HttpSession> sessions = new Vector<>(64);
//int sessionCount = 0;
//
//@Override
//public synchronized void sessionCreated(HttpSessionEvent se) {
//// TODO Auto-generated method stub
////sessions.add(se.getSession());
////se.getSession().
//
////HttpServletRequest request;
////request.
//sessionCount++;
//}
//
//@Override
//public synchronized void sessionDestroyed(HttpSessionEvent se) {
//// TODO Auto-generated method stub
//sessionCount++;
//}