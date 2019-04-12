package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

class AutocheckingRecheckDriverTest {

	@Test
	void makeUnique_should_append_unique_prefix() {
		final AutocheckingRecheckDriver cut = new AutocheckingRecheckDriver( mock( RemoteWebDriver.class ) );
		assertThat( cut.makeUnique( "someString" ) ).isEqualTo( "00_someString" );
		assertThat( cut.makeUnique( "someOtherString" ) ).isEqualTo( "01_someOtherString" );
		assertThat( cut.makeUnique( "someString" ) ).isEqualTo( "02_someString" );
	}
}
