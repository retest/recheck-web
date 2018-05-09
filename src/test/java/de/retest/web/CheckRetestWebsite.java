package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.junit.SystemProperty;
import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class CheckRetestWebsite {

	@Rule
	public SystemProperty webdriverChromeDriver = new SystemProperty( "webdriver.chrome.driver", null );

	WebDriver driver;
	Recheck re;

	@Before
	public void setUp() {
		webdriverChromeDriver.setValue( "src/main/resources/chromedriver" );
		driver = new ChromeDriver();
		driver.manage().timeouts().pageLoadTimeout( -1, TimeUnit.MINUTES );
		re = new RecheckImpl();
	}

	@After
	public void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	@Ignore
	public void check_changing_website() {
		driver.get( "https://www.google.de/search?q=current+time" );
		re.startTest();
		re.check( driver, "index" );
		re.capTest();
	}

	@Test
	public void check_retest_website() throws Exception {
		driver.get( "https://retest.de" );
		re.startTest();
		re.check( driver, "index" );

		final WebElement toDownload = driver.findElement( By.name( "submit" ) );
		toDownload.click();
		re.check( driver, "download_1" );

		re.capTest();
	}

	@Test
	public void check_retest_website_classic() throws Exception {
		driver.get( "https://retest.de" );
		assertThat( driver.getTitle() ).isEqualTo( "retest" );

		final WebElement toDownload = driver.findElement( By.name( "submit" ) );
		toDownload.click();

		assertThat( driver.findElement( By.id( "errorMsg" ) ).isDisplayed() ).isTrue();
	}

}
