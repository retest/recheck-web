package de.retest.web;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class IntegrationTest {

	private WebDriver driver;
	private Recheck re;

	@Before
	public void setup() {
		System.setProperty( "webdriver.chrome.driver", "src/test/resources/chromedriver" );

		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments( "--headless" );
		chromeOptions.addArguments( "--disable-gpu" );
		chromeOptions.addArguments( "--window-size=1200,800" );

		final DesiredCapabilities dc = new DesiredCapabilities();
		dc.setJavascriptEnabled( true );
		dc.setCapability( ChromeOptions.CAPABILITY, chromeOptions );

		driver = new ChromeDriver( dc );

		// Use the default implementation.
		re = new RecheckImpl();
	}

	@Test
	public void index() throws Exception {
		// Set the file name of the Golden Master.
		re.startTest( "index" );

		// Do your Selenium stuff.
		driver.get( "file:///"
				+ new File( "src/test/resources/de/retest/web/IntegrationTest/retest.html" ).getAbsolutePath() );

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
