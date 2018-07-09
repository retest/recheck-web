package de.retest.web.testutils;

import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeOptionsFactory {

	private ChromeOptionsFactory() {}

	public static ChromeOptions createNewInstance( final String... args ) {
		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use chrome in container-based Travis CI environment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		opts.addArguments( args );
		return opts;
	}

}
