package de.retest.web.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.By;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.PageFactory.Page;

/*
 * Simple recheck-web showcase for a Chrome-based integration test. See other *IT classes for more examples.
 */
public class SimpleAutocheckingDriverShowcaseIT {

	private AutocheckingRecheckDriver driver;

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
		driver = new AutocheckingRecheckDriver( new ChromeDriver( opts ) );
	}

	@Test
	public void index() throws Exception {
		driver.navigate().to( PageFactory.page( Page.FORM_PAGE ) );

		driver.findElement( By.id( "old-email-id" ) ).sendKeys( "me@retest.de" );

		driver.findElement( By.tagName( "html" ) ).findElement( By.className( "inputLabel" ) ).sendKeys( "typed" );

		driver.findElement( By.name( "checky" ) ).click();

		driver.findElement( By.linkText( "Contact" ) ).click();

		driver.findElement( By.retestId( "contact" ) ).click();

		driver.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();
		driver.cap();
	}

}
