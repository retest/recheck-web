package de.retest.web.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.RecheckOptions;
import de.retest.web.RecheckWebOptions;

/**
 * Extends both {@link AutocheckingRecheckDriver} and {@link UnbreakableDriver} to combine all recheck-web features. Use
 * this class if you automatically want to incorporate new features without changing your recheck-web driver
 * implementation explicitly.
 */
public class RecheckDriver extends AutocheckingRecheckDriver {

	public RecheckDriver( final RemoteWebDriver wrapped ) {
		super( wrapped );
	}

	public RecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options ) {
		super( wrapped, options );
	}

	public RecheckDriver( final RemoteWebDriver wrapped, final RecheckWebOptions options ) {
		super( wrapped, options );
	}

}
