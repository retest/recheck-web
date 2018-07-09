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

public class SimplePageIntTest {

	WebDriver driver;
	Recheck re;

	@Before
	public void setUp() {
		driver = new ChromeDriver( ChromeOptionsFactory.createNewInstance() );

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
