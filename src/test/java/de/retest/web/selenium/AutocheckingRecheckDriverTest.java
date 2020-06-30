package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

class AutocheckingRecheckDriverTest {

	AutocheckingRecheckDriver cut;

	@BeforeEach
	void setUp() {
		cut = new AutocheckingRecheckDriver( mock( RemoteWebDriver.class ) );
	}

	@Test
	void switchTo_should_create_autochecking_locator() {
		assertThat( cut.switchTo() ).isInstanceOf( AutocheckingTargetLocator.class );
	}
}
