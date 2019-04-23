package de.retest.web.selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckOptions;

/**
 * This is a wrapper for different {@link RemoteWebDriver} implementations, such as {@link ChromeDriver} or
 * {@link FirefoxDriver}.
 *
 * The {@code RecheckDriver} extends {@code AutocheckingDriver}, such that it is unnecessary to call
 * {@link Recheck#check(Object, String)} explicitly. It also extends {@link UnbreakableDriver}, such that it will not
 * break on otherwise breaking changes until these changes are also applied to the Golden Master.
 */
public class RecheckDriver extends AutocheckingRecheckDriver {

	public RecheckDriver( final RemoteWebDriver wrapped ) {
		super( wrapped );
	}

	public RecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options,
			final AutocheckingCheckNamingStrategy namingStrategy ) {
		super( wrapped, options, namingStrategy );
	}

}
