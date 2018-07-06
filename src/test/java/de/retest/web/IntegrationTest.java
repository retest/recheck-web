package de.retest.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
		// $ whereis chromedriver
		System.setProperty( "webdriver.chrome.driver", "/usr/local/bin/chromedriver" );

		final ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments( "--headless" );
		chromeOptions.addArguments( "--disable-gpu" );

		final DesiredCapabilities dc = new DesiredCapabilities();
		dc.setJavascriptEnabled( true );
		dc.setCapability( ChromeOptions.CAPABILITY, chromeOptions );

		driver = new ChromeDriver( dc );

		driver.manage().timeouts().pageLoadTimeout( -1, TimeUnit.MINUTES );
		re = new RecheckImpl();
	}

	@Test
	public void index() throws Exception {
		driver.get( "file:///"
				+ new File( "src/test/resources/de/retest/web/IntegrationTest/retest.html" ).getAbsolutePath() );

		re.startTest( "index" );
		re.check( driver, "index" );
		re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();
		re.cap();
	}

}
