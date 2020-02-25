package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
			assertThat( e ).hasMessageContaining( "Test 'testSimpleChange' has 4 difference(s) in 1 state(s):" ) //
					.hasMessageEndingWith( "\tdiv at 'html[1]/body[1]/div[3]':\n" + //
							"\t\tid: expected=\"twoblocks\", actual=\"changedblock\"\n" + //
							"\tp [Some text] at 'html[1]/body[1]/div[3]/p[1]':\n" + //
							"\t\ttext: expected=\"Some text\", actual=\"Some changed text\"\n" + //
							"\tp [Some more text] at 'html[1]/body[1]/div[3]/p[2]':\n" + //
							"\t\twas deleted\n" + //
							"\th2 [Subheading] at 'html[1]/body[1]/h2[1]':\n" + //
							"\t\twas inserted" );
		}
	}

	@After
	public void tearDown() {
		driver.quit();
		re.cap();
	}
}
