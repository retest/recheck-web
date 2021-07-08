package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.junit.jupiter.RecheckExtension;
import de.retest.web.RecheckWebOptions;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

@Disabled( "We only use this to create an example.report file for review." )
@ExtendWith( RecheckExtension.class )
class WikipediaIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() throws Exception {
		driver = WebDriverFactory.driver( Driver.CHROME );
		final RecheckOptions opts = RecheckWebOptions.builder() //
				.enableScreenshots() //
				.build();
		re = new RecheckImpl( opts );
	}

	@Test
	void myWikipediaTest() throws Exception {
		// Switch to "expected" subfolder to restore original page.
		driver.get( PageFactory.toPageUrlString( "wikipedia/actual/wikipedia-characterization-test.html" ) );
		re.check( driver, "characterization-testing-page" );
	}

	@AfterEach
	void tearDown() throws Exception {
		driver.quit();
	}

}
