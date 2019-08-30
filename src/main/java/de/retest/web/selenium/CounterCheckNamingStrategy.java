package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

/**
 * This naming strategy simply counts the checks and returns a two-digit string of the current check, starting with 0.
 * This is robust to changes of the target and params, but fails miserably if steps are inserted, as this will affect
 * all subsequent checks. Also, this is somewhat humanly unreadable.
 */
public class CounterCheckNamingStrategy implements AutocheckingCheckNamingStrategy {

	private int stepCounter = 0;

	@Override
	public String getUniqueCheckName( final String action, final WebElement target, final Object... params ) {
		return getUniqueCheckName( action );
	}

	@Override
	public String getUniqueCheckName( final String action ) {
		return String.format( "%02d", stepCounter++ );
	}

	@Override
	public void nextTest() {
		stepCounter = 0;
	}
}
