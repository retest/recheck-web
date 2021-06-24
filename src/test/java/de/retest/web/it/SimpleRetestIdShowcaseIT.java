package de.retest.web.it;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.web.RecheckWebImpl;
import de.retest.web.selenium.By;
import de.retest.web.selenium.UnbreakableDriver;

/*
 * Simple recheck-web showcase for a Chrome-based integration test. See other *IT classes for more examples.
 */
public class SimpleRetestIdShowcaseIT {

	private UnbreakableDriver driver;
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
		driver = new UnbreakableDriver( new ChromeDriver( opts ) );

		// Use the default implementation.
		re = new RecheckWebImpl();
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

		// Works even if the GM is not yet created, if retestId is deterministic
		re.check( driver, "new_state" );
		driver.findElement( By.retestId( "for_testers" ) ).click();

		// Usually, we conclude the test case with re.capTest(),
		// but since we expect differences, we check these.
		Assertions.assertThatThrownBy( () -> re.capTest() ).hasMessageContainingAll( //
				"text: expected=\"Contact\", actual=\"Support\"", //
				"No Golden Master found. First time test was run?" );
	}

	@After
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		//		re.cap();

		// Not standard! Ensure we delete the GM that was created
		FileUtils.deleteQuietly( Paths.get( "src/test/resources/retest/recheck",
				"de.retest.web.it.SimpleRetestIdShowcaseIT", "simple-showcase.new_state.recheck" ).toFile() );
	}

}
