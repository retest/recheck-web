package de.retest.web.it;

import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

@Disabled( "We only use this to create an example.report file for review." )
class WikipediaIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() throws Exception {
		driver = new ChromeDriver();
		re = new RecheckImpl();
	}

	@Test
	void myWikipediaTest() throws Exception {
		re.startTest();
		// Use commit 292c0a18d3 to restore the original page.
		final String url = Paths.get( "src/test/resources/pages/wikipedia/wikipedia-characterization-test.html" )
				.toUri().toURL().toString();
		driver.get( url );
		re.check( driver, "characterization-testing-page" );
		re.capTest();
	}

	@AfterEach
	void tearDown() throws Exception {
		driver.close();
		re.cap();
	}

}
