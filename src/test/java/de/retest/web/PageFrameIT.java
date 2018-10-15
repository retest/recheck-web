package de.retest.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;

public class PageFrameIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void page_frame_html_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		re.startTest( "page-frame-" + driver.getClass().getSimpleName() );

		final Path simplePagePath = Paths.get( "src/test/resources/pages/page-frame.html" );
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
