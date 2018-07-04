package de.retest.web;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class IntegrationTest {

	private WebDriver driver;
	private Recheck re;

	@Before
	public void setup() {
		System.setProperty( "webdriver.chrome.driver", "src/test/resources/chromedriver" );
		driver = new ChromeDriver();
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
