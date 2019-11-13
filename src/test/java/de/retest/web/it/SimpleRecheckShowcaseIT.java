package de.retest.web.it;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.vintage.RecheckRule;

/*
 * Simple recheck-web showcase for a Chrome-based integration test. See other *IT classes for more examples.
 */
public class SimpleRecheckShowcaseIT {
	// Connects recheck to your test's life cycle, taking care of starting and finishing tests, naming them, etc.
	@Rule
	public RecheckRule rule = new RecheckRule();

	private WebDriver driver;
	private Recheck re;

	@Before
	public void setup() {
		// If ChromeDriver (http://chromedriver.chromium.org/downloads/) is not in your PATH, uncomment this and point to your installation.
		//		System.setProperty( "webdriver.chrome.driver", "path/to/chromedriver" );

		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI enviroment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		driver = new ChromeDriver( opts );

		// Use the default implementation.
		re = new RecheckImpl();
		// Let the recheck rule know which implementation it shall use.
		rule.use( re );
	}

	@Test
	public void simpleShowcase() throws Exception {
		// Do your Selenium stuff.
		final Path showcasePath = Paths.get( "src/test/resources/pages/showcase/retest.html" );
		driver.get( showcasePath.toUri().toURL().toString() );

		Thread.sleep( 1000 );

		// Single call instead of multiple assertions (doesn't fail on differences).
		re.check( driver, "index" );
	}

	@After
	public void tearDown() {
		driver.quit();
	}

}
