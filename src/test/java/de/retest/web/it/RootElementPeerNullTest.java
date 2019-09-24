package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.testutils.WebDriverFactory;

public class RootElementPeerNullTest {

	Recheck re;
	WebDriver driver;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
		driver = WebDriverFactory.driver( WebDriverFactory.Driver.CHROME );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void rootElementShouldNotBeNull() throws Exception {
		re.startTest();

		driver.get( getClass().getResource( "RootElementPeerNullTest.html" ).toExternalForm() );

		re.check( driver.findElement( By.className( "md-tabs" ) ), "tabs" );

		re.capTest();
	}
}
