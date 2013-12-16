package net.freechoice.application.load.calculator;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.load.Report;

/**
 * @author BowenCai
 *
 */
public class Calculator2 implements ILoadCalculator {

	@Override
	public LoadLevel rate(Report report) {
		
//		double sysCpuLoad;
//		double vmMemUsage;
//		int sessionCount;
		int level = 5;
//		int level = 10 * (int)(0.55 * sysCpuLoad 
//							+ 0.3* vmMemUsage 
//							+ 0.15 * (sessionCount / 100));
		
		switch (level) {
		case 0:
		case 1:
		case 2:
		case 3:
			return LoadLevel.I_LIGHT;
		case 4:
		case 5:
		case 6:
			return LoadLevel.II_MEDIUM;
		case 7:
		case 8:
			return LoadLevel.III_HEAVY;
		default:
			return LoadLevel.IIII_OVERLOAD;
		}
	}

}
