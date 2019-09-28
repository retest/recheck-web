package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.RecheckExtension;
import de.retest.web.testutils.PageFactory;

@ExtendWith( RecheckExtension.class )
class SpecialContainerTestIT {
	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = "special-container-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void testCenter( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.toPageUrlString( "special-container.html" ) );

		re.check( driver, "open" );

		// Comment in to create golden master with the expected change
		// driver.findElement( org.openqa.selenium.By.id( "change-for-div" ) ).click();

		// This test should then have exactly one difference
		re.check( driver, "click" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}
}
