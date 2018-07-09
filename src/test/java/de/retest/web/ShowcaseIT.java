package de.retest.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.ChromeOptionsFactory;

public class ShowcaseIT {

	private WebDriver driver;
	private Recheck re;

	@Before
	public void setup() {
		driver = new ChromeDriver( ChromeOptionsFactory.createNewInstance() );

		// Use the default implementation.
		re = new RecheckImpl();
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "showcase" );

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
