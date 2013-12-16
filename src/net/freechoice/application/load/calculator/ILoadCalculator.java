package net.freechoice.application.load.calculator;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.load.Report;


/**
 * @author BowenCai
 *
 */
public interface ILoadCalculator {

	LoadLevel rate(final Report report);

}
