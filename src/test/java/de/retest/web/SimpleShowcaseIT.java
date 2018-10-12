package de.retest.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class SimpleShowcaseIT {

	private WebDriver driver;
	private Recheck re;

	@Before
	public void setup() {
		// If ChromeDriver is not in your PATH, uncomment this and point to your installation.
		// e.g. it can be downloaded from http://chromedriver.chromium.org/downloads
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
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "simpleShowcaseIT" );

		// Do your Selenium stuff.
		final Path showcasePath = Paths.get( "src/test/resources/pages/showcase/retest.html" );
		driver.get( showcasePath.toUri().toURL().toString() );

		// Single call instead of multiple assertions (doesn't fail on differences).
		re.check( driver, "index" );

		// Conclude the test case (fails on differences).
		re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();

		// Produce the result file.
		re.cap();
	}

}
