package net.freechoice.service;

import java.util.Date;

import net.freechoice.application.SnapShooter;
import net.freechoice.application.load.IMonitor;
import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.load.Report;
import net.freechoice.application.load.calculator.ILoadCalculator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;


/**
 * rate system load level and broadcast load change event if necessary.
 * 
 * @author BowenCai
 * @since 2013-11-30
 */
public class RegulationService implements ApplicationContextAware {

	RegulationService() {
	}

//-----------------------------------------------------------------------------
//				
//-----------------------------------------------------------------------------
	ApplicationContext	appContext;
	
	SnapShooter			snapShooter;
	ILoadCalculator		calculator;
	IMonitor			monitor;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
	}

	
	LoadLevel lastLevel = LoadLevel.I_LIGHT;
	
	public void evaluate() {
		
		Report currentStatus = monitor.reportStatus();
		
		LoadLevel currentLevel = calculator.rate(currentStatus);
		
		if ( !currentLevel.equals(lastLevel)) {
			sendBroadCast(currentLevel);
		}
		/**
		 * cooling down from over loading, send e-mail to admins
		 */
		if (!currentLevel.equals(LoadLevel.IIII_OVERLOAD)
				&& lastLevel.equals(LoadLevel.IIII_OVERLOAD)) {
			
			this.snapShooter.handle(currentStatus);
		}
		lastLevel = currentLevel;
	}

	private void sendBroadCast(LoadLevel newLevel) {
		
		LoadChangedEvent event = new LoadChangedEvent(this);
		event.setNewLoadLevel(newLevel);
		appContext.publishEvent(event);
	}
	
	
	
	@SuppressWarnings("deprecation")
	public void test() {

		Report currentStatus = monitor.reportStatus();
		LoadLevel currentLevel = calculator.rate(currentStatus);

		if (!currentLevel.equals(lastLevel)) {
			sendBroadCast(currentLevel);

			Date date = new Date();
			System.out.println("------------------------------");
			System.out.println(date.getHours() + ":" + date.getMinutes() + ":"
					+ date.getSeconds());

			System.err.println(lastLevel.toString() + "   ->   "
					+ currentLevel.toString());
			System.out.println("------------------------------\n");
			/**
			 * cooled down from over loading, send e-mail to me
			 */
			if (!currentLevel.equals(LoadLevel.IIII_OVERLOAD)
					&& lastLevel.equals(LoadLevel.IIII_OVERLOAD)) {
				this.snapShooter.handle(currentStatus);
			}
		}
		lastLevel = currentLevel;
	}
	
	public LoadLevel getCurrentLevel() {
		return lastLevel;
	}
	
	public void setCalculator(ILoadCalculator calculator) {
		this.calculator = calculator;
	}
	public void setMonitor(IMonitor monitor) {
		this.monitor = monitor;
	}
	public void setSnapShooter(SnapShooter snapShooter) {
		this.snapShooter = snapShooter;
	}

	public static class LoadChangedEvent extends ApplicationEvent {
		 
		private static final long serialVersionUID = 1L;
		
		public LoadLevel newLoadLevel;

		public LoadChangedEvent(Object source) {
			super(source);
		}
		public LoadLevel getNewLoadLevel() {
			return newLoadLevel;
		}
		public void setNewLoadLevel(LoadLevel newLoadLevel) {
			this.newLoadLevel = newLoadLevel;
		}
	}


}
