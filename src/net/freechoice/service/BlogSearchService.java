package net.freechoice.service;

import net.freechoice.application.load.LoadLevel;
import net.freechoice.application.search.ControlledSearch;
import net.freechoice.application.search.DummySearch;
import net.freechoice.service.RegulationService.LoadChangedEvent;

import org.springframework.context.ApplicationListener;

/**
 * 
 * provide proper full-text search basic on current load level
 * @author BowenCai
 *
 */
public class BlogSearchService implements ApplicationListener<LoadChangedEvent> {

	ControlledSearch niceSearch;
	ControlledSearch avgSearch;
	ControlledSearch basicSearch;
	ControlledSearch dummySearch;
	
	BlogSearchService() {
		
		niceSearch = new ControlledSearch();
		niceSearch.setSentenceLimit(64);
		niceSearch.setHeadLineCfg(niceHeadLine);
		
		avgSearch = new ControlledSearch();
		avgSearch.setSentenceLimit(48);
		avgSearch.setHeadLineCfg(avgHeadLine);
		
		basicSearch = new ControlledSearch();
		basicSearch.setSentenceLimit(32);
		basicSearch.setHeadLineCfg(basicHeadLine);
		
		dummySearch = new DummySearch();
	}
	
	LoadLevel currentLevel = LoadLevel.I_LIGHT;
	
	@Override
	public void onApplicationEvent(LoadChangedEvent event) {
		this.currentLevel = event.newLoadLevel;
	}
	
	public ControlledSearch getSuitableSearch() {

		switch (currentLevel) {
		case I_LIGHT:
			return niceSearch;
		case II_MEDIUM:
			return avgSearch;
		case III_HEAVY:
			return basicSearch;
		default:
			return dummySearch;
		}
	}
	
	public String niceHeadLine = 
			"'MaxWords=80,MinWords=24,ShortWord=4," 
			+"MaxFragments=4,HighlightAll=FALSE," 
			+"FragmentDelimiter=\"<br><br>...&nbsp&nbsp\"'";
	
	public String avgHeadLine = 			
			"'MaxWords=48,MinWords=16,ShortWord=3," 
			+"MaxFragments=2,HighlightAll=FALSE," 
			+"FragmentDelimiter=\"<br><br>...&nbsp&nbsp\"'";
	
	public String basicHeadLine =
			"'MaxWords=32,MinWords=12,ShortWord=3," 
			+"MaxFragments=0,HighlightAll=FALSE," 
			+"FragmentDelimiter=\"<br><br>...&nbsp&nbsp\"'";
	
	public void setNiceHeadLine(String niceHeadLine) {
		this.niceHeadLine = niceHeadLine;
	}

	public void setAvgHeadLine(String avgHeadLine) {
		this.avgHeadLine = avgHeadLine;
	}

	public void setBasicHeadLine(String basicHeadLine) {
		this.basicHeadLine = basicHeadLine;
	}

}






