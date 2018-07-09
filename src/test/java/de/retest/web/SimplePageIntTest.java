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

public class SimplePageIntTest {

	WebDriver driver;
	Recheck re;

	@Before
	public void setUp() {
		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments( "--headless" );
		opts.addArguments( "--no-sandbox" );
		opts.addArguments( "--window-size=1200,800" );

		driver = new ChromeDriver( opts );

		re = new RecheckImpl();
	}

	@Test
	public void simple_html_page_should_be_checked() throws Exception {
		re.startTest( "simple-page" );

		final Path simplePagePath = Paths.get( "src/test/resources/pages/simple-page.html" );
		driver.get( simplePagePath.toUri().toURL().toString() );

		re.check( driver, "open" );

		re.capTest();
	}

	@After
	public void tearDown() {
		driver.quit();

		re.cap();
	}

}
