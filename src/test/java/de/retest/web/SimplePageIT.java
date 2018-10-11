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

class SimplePageIT {

	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void simple_html_page_should_be_checked( final WebDriver driver ) throws Exception {
		re.startTest( "simple-page-" + driver.getClass().getSimpleName() );

		final Path simplePagePath = Paths.get( "src/test/resources/pages/simple-page.html" );
		driver.get( simplePagePath.toUri().toURL().toString() );

		re.check( driver, "open" );

		driver.quit();
	}

	@AfterEach
	void tearDown() {
		re.capTest();
		re.cap();
	}

}
