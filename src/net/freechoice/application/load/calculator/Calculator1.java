package net.freechoice.application.load.calculator;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.load.Report;

/**
 * @author BowenCai
 *
 */
public class Calculator1 implements ILoadCalculator {

	public static final double w_cpu		= 0.5;
	public static final double w_heap		= 0.15;
	public static final double w_nonHeap	= 0.1;
	public static final double w_session	= 0.25;
	
	@Override
	public LoadLevel rate(Report report) {
		
		double avg = 	w_cpu	*	report.cpuLoad
					+	w_heap	*	report.heapUsage
					+	w_nonHeap*	report.nonHeapUsage
					+	w_session*	report.sessionCount;
		
		return avg < 0.3 ? LoadLevel.I_LIGHT
				:	avg < 0.6 ? LoadLevel.II_MEDIUM
				:	avg < 0.9 ? LoadLevel.III_HEAVY
				:	LoadLevel.IIII_OVERLOAD;
	}

}
