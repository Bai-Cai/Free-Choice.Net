package net.freechoice.application.load.calculator;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.load.Report;


/**
 * @author BowenCai
 *
 */
public class DemoCalculator implements ILoadCalculator {

	@Override
	public LoadLevel rate(Report report) {

		return report.sessionCount < 2 ? LoadLevel.I_LIGHT// 0, 1
				: report.sessionCount < 4 ? 
						LoadLevel.III_HEAVY//3
						: LoadLevel.IIII_OVERLOAD;// 4, 5, 6, 7,...

	}

}
