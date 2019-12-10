package de.retest.web.meta.driver.capabilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.selenium.UnbreakableDriver;

class CapabilityMetadataProviderTest {

	@Test
	void of_should_identify_driver() throws Exception {
		final WebDriver driver = mock( RemoteWebDriver.class, RETURNS_MOCKS );

		final MetadataProvider cut = CapabilityMetadataProvider.of( driver );

		assertThat( cut.retrieve() ).isNotEmpty();
	}

	@Test
	void of_should_identify_wrapped_driver() throws Exception {
		final UnbreakableDriver wrapsDriver = mock( UnbreakableDriver.class, RETURNS_MOCKS );
		when( wrapsDriver.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class, RETURNS_MOCKS ) );

		final MetadataProvider cut = CapabilityMetadataProvider.of( wrapsDriver );

		assertThat( cut.retrieve() ).isNotEmpty();
	}

	@Test
	void of_should_not_use_plain_web_driver_and_return_empty_provider() throws Exception {
		final WebDriver driver = mock( WebDriver.class );

		final MetadataProvider cut = CapabilityMetadataProvider.of( driver );

		assertThat( cut.retrieve() ).isEmpty();
	}
}
