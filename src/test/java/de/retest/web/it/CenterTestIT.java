package de.retest.web.it;

import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class CenterTestIT {
	WebDriver driver;
	Recheck re;

	@Test
	public void testCenter() throws Exception {
		re = new RecheckImpl();

		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI enviroment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		driver = new ChromeDriver( opts );

		final String url = Paths.get( "src/test/resources/pages/centered.html" ).toUri().toURL().toString();
		driver.get( url );

		re.check( driver, "open" );
		re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		re.cap();
	}
}
