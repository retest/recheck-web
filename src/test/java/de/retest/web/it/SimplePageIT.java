package de.retest.web.it;

import static de.retest.web.testutils.PageFactory.page;
import static de.retest.web.testutils.PageFactory.Page.SIMPLE_PAGE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.jupiter.RecheckExtension;

@ExtendWith( RecheckExtension.class )
class SimplePageIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = "simple-page-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void simple_html_page_should_be_checked( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( page( SIMPLE_PAGE ) );

		Thread.sleep( 1000 );

		re.check( driver, "open" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

}
