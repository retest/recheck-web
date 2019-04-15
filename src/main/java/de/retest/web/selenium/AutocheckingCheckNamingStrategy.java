package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

/**
 * Interface to create names for checks for the {@link AutocheckingRecheckDriver}. These names must be unique within a
 * test, but can repeat in between tests (i.e. after calling {@link #nextTest()}).
 */
public interface AutocheckingCheckNamingStrategy {

	/**
	 * Return a string that is a unique identifier in the current test.
	 */
	String getUniqueCheckName( String action, WebElement target, Object... params );

	/**
	 * Reset this naming strategy, such that strings can be reused in different tests.
	 */
	void nextTest();
}
