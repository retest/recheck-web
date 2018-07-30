package de.retest.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.ChromeOptionsFactory;

class SimplePageIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		driver = new ChromeDriver( ChromeOptionsFactory.createNewInstance() );

		re = new RecheckImpl();
	}

	@Test
	void simple_html_page_should_be_checked() throws Exception {
		re.startTest( "simple-page" );

		final Path simplePagePath = Paths.get( "src/test/resources/pages/simple-page.html" );
		driver.get( simplePagePath.toUri().toURL().toString() );

		re.check( driver, "open" );

		re.capTest();
	}

	@AfterEach
	void tearDown() {
		driver.quit();

		re.cap();
	}

}
