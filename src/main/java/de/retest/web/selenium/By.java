package de.retest.web.selenium;

public abstract class By extends org.openqa.selenium.By {

	public static ByRetestId retestId( final String recheckId ) {
		return new ByRetestId( recheckId );
	}

}
