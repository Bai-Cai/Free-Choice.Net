package test.other;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.service.RegulationService;
import net.freechoice.service.RegulationService.LoadChangedEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author BowenCai
 *
 */
public class T_applicationEvent {

	ApplicationContext appContext;
	@Before
	public void setUp() throws Exception {
		appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println("up and running!");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		LoadChangedEvent event = new RegulationService.LoadChangedEvent(this);
		event.setNewLoadLevel(LoadLevel.IIII_OVERLOAD);
		appContext.publishEvent(event);
	}
}
