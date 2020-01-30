package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

class UnbreakableDriverTest {

	@Test
	void should_unwrap_wrapped_driver_if_possible() throws Exception {
		final RemoteWebDriver inner = mock( RemoteWebDriver.class );

		final RemoteWebDriver oldOuter =
				mock( RemoteWebDriver.class, withSettings().extraInterfaces( org.openqa.selenium.WrapsDriver.class ) );
		when( ((WrapsDriver) oldOuter).getWrappedDriver() ).thenReturn( inner );

		final RemoteWebDriver newOuter =
				mock( RemoteWebDriver.class, withSettings().extraInterfaces( WrapsDriver.class ) );
		when( ((WrapsDriver) newOuter).getWrappedDriver() ).thenReturn( inner );

		assertThat( new UnbreakableDriver( inner ).getWrappedDriver() ).isSameAs( inner );
		assertThat( new UnbreakableDriver( oldOuter ).getWrappedDriver() ).isSameAs( inner );
		assertThat( new UnbreakableDriver( newOuter ).getWrappedDriver() ).isSameAs( inner );
	}

}
