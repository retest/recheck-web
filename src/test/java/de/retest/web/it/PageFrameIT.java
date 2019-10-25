package de.retest.web.it;

import static de.retest.web.testutils.PageFactory.page;
import static de.retest.web.testutils.PageFactory.Page.PAGE_FRAME;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.jupiter.RecheckExtension;
import de.retest.web.selenium.By;

@ExtendWith( RecheckExtension.class )
class PageFrameIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = "page-frame-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void page_frame_html_should_be_checked( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		this.driver.get( page( PAGE_FRAME ) );

		driver.findElement( By.id( "email" ) ).sendKeys( "Max" );

		Thread.sleep( 1000 );
		re.check( driver, "open" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

}
