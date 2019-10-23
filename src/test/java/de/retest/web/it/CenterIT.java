package de.retest.web.it;

import static de.retest.web.testutils.PageFactory.page;
import static de.retest.web.testutils.PageFactory.Page.CENTER;

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
class CenterIT {
	WebDriver driver;
	Recheck re;

	@BeforeEach
	void before() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = "center-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void testCenter( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		this.driver.get( page( CENTER ) );

		Thread.sleep( 1000 );

		re.check( this.driver, "open" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}
}
