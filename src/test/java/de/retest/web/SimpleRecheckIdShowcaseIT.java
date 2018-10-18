package de.retest.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.web.selenium.By;
import de.retest.web.selenium.RecheckDriver;

/*
 * Simple recheck-web showcase for a Chrome-based integration test. See other *IT classes for more examples.
 */
public class SimpleRecheckIdShowcaseIT {

	private RecheckDriver driver;
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
		driver = new RecheckDriver( new ChromeDriver( opts ) );

		// Use the default implementation.
		re = new UnbreakableRecheckImpl( driver );
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "simple-showcase" );

		// Do your Selenium stuff.
		final Path showcasePath = Paths.get( "src/test/resources/pages/showcase/retest-changed.html" );
		driver.get( showcasePath.toUri().toURL().toString() );

		// Single call instead of multiple assertions (doesn't fail on differences).
		re.check( driver, "index" );

		driver.findElement( By.retestId( "contact" ) ).click();

		// Usually, we conclude the test case, but since we expect differences, this will fail.
		// re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		//		re.cap();
	}

}
