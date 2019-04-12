package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.remote.RemoteWebDriver;

class AutocheckingRecheckDriverTest {

	@Test
	void test_makeUnique() {
		final AutocheckingRecheckDriver cut = new AutocheckingRecheckDriver( Mockito.mock( RemoteWebDriver.class ) );
		assertThat( cut.makeUnique( "someString" ) ).isEqualTo( "00_someString" );
		assertThat( cut.makeUnique( "someOtherString" ) ).isEqualTo( "01_someOtherString" );
		assertThat( cut.makeUnique( "someString" ) ).isEqualTo( "02_someString" );
	}

}
