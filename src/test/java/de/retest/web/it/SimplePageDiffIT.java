package de.retest.web.it;

import static org.junit.jupiter.api.Assertions.fail;

import org.approvaltests.Approvals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

public class SimplePageDiffIT {

	WebDriver driver;
	Recheck re;

	@Before
	public void setup() {
		re = new RecheckImpl();
		driver = WebDriverFactory.driver( Driver.CHROME );
	}

	@Test
	public void testSimpleChange() throws Exception {
		re.startTest();

		driver.get( PageFactory.toPageUrlString( "simple-page-diff.html" ) );

		re.check( driver, "open" );
		try {
			re.capTest();
			fail( "Assertion Error expected" );
		} catch ( final AssertionError e ) {
			Approvals.verify( e.getMessage() );
		}
	}

	@After
	public void tearDown() {
		driver.quit();
		re.cap();
	}
}
