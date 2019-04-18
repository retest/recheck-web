package de.retest.web.it;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.web.RecheckWebImpl;
import de.retest.web.selenium.By;
import de.retest.web.selenium.UnbreakableDriver;

/*
 * Simple recheck-web showcase for a Chrome-based integration test. See other *IT classes for more examples.
 */
public class SimpleUnbreakableSeleniumShowcaseIT {

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

		// Use the RecheckDriver as a wrapper for your usual driver
		driver = new UnbreakableDriver( new ChromeDriver( opts ) );

		// Use the unbreakable recheck implementation.
		re = new RecheckWebImpl();
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "simple-showcase" );

		// Do your Selenium stuff.
		// final Path showcasePath = Paths.get( "src/test/resources/pages/showcase/retest.html" );
		final Path showcasePath = Paths.get( "src/test/resources/pages/showcase/retest-changed.html" );
		driver.get( showcasePath.toUri().toURL().toString() );

		// Single call instead of multiple assertions (doesn't fail on differences).
		re.check( driver, "index" );

		// Will issue a warning, as the id has changed
		driver.findElement( By.id( "intro-slider" ) );

		// You could use never-changing retest-id instead
		driver.findElement( By.retestId( "intro-slider" ) );

		// Will issue a warning, as the XPath has changed
		driver.findElement( By.className( "slides" ) );

		// Will issue a warning, as the XPath has changed
		driver.findElement( By.name( "logo" ) );

		// Will issue a warning, as the XPath has changed
		driver.findElement( By.linkText( "Contact" ) );

		// Will issue a warning, as the XPath has changed
		// TODO Implement searching for complex xpath expressions
		// driver.findElement( By.xpath( "//*[@id=\"intro-slider\"]/ul/li[2]/div/div[1]/h2/span[@text=\"SomeText\"]" ) );

		// Will issue a warning, as the class has changed
		// TODO Implement searching for complex css expressions
		// driver.findElement( By.cssSelector( "ul div.slider-text" ) );

		// TODO Ignore the differences in order to be able to re.cap the test...
		// re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		// re.cap();
	}

}
