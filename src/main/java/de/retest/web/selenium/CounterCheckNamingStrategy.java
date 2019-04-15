package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

public class CounterCheckNamingStrategy implements AutocheckingCheckNamingStrategy {

	private int stepCounter = 0;

	@Override
	public void nextTest() {
		stepCounter = 0;
	}

	@Override
	public String getUniqueCheckName( final String action, final WebElement target, final Object... params ) {
		return String.format( "%02d", stepCounter++ );
	}
}
