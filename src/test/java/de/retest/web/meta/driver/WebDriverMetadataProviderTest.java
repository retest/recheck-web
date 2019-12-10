package de.retest.web.meta.driver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.meta.SeleniumMetadata;
import de.retest.web.selenium.UnbreakableDriver;

class WebDriverMetadataProviderTest {

	@Test
	void retrieve_should_directly_take_driver_type() throws Exception {
		final WebDriver driver = mock( WebDriver.class, RETURNS_MOCKS );

		final MetadataProvider cut = WebDriverMetadataProvider.of( driver );

		assertThat( cut.retrieve() )
				.contains( Pair.of( SeleniumMetadata.DRIVER_TYPE, driver.getClass().getSimpleName() ) );
	}

	@Test
	void retrieve_should_directly_take_driver_type_if_wrapped() throws Exception {
		final UnbreakableDriver driver = mock( UnbreakableDriver.class, RETURNS_MOCKS );
		when( driver.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class, RETURNS_MOCKS ) );

		final MetadataProvider cut = WebDriverMetadataProvider.of( driver );

		assertThat( cut.retrieve() )
				.contains( Pair.of( SeleniumMetadata.DRIVER_TYPE, driver.getClass().getSimpleName() ) );
	}
}
