package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

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
