package net.freechoice.application.search;

/**
 * @author BowenCai
 *
 */
public class DummySearch extends ControlledSearch {

	@Override
	public String createSQL() {
		return 	"select id from fc_post limit 0";
	}

}
