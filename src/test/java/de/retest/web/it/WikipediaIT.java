package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.PageFactory;

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
		// Switch to "expected" subfolder to restore original page.
		driver.get( PageFactory.toPageUrlString( "wikipedia/actual/wikipedia-characterization-test.html" ) );
		re.check( driver, "characterization-testing-page" );
		re.capTest();
	}

	@AfterEach
	void tearDown() throws Exception {
		driver.close();
		re.cap();
	}

}
