package de.retest.web.meta.element;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.AutocheckingWebElement;

class WebElementMetadataProviderTest {

	@Test
	void of_should_extract_driver_from_element() throws Exception {
		final RemoteWebElement element = mock( RemoteWebElement.class, RETURNS_MOCKS );

		final MetadataProvider cut = WebElementMetadataProvider.of( element );

		assertThat( cut.retrieve() ).isNotEmpty();
	}

	@Test
	void of_should_extract_driver_from_wrapped() throws Exception {
		final AutocheckingWebElement element = mock( AutocheckingWebElement.class );
		when( element.getWrappedDriver() ).thenReturn( mock( AutocheckingRecheckDriver.class, RETURNS_MOCKS ) );

		final MetadataProvider cut = WebElementMetadataProvider.of( element );

		assertThat( cut.retrieve() ).isNotEmpty();
	}

	@Test
	void of_should_not_use_plain_web_element_and_return_empty_provider() throws Exception {
		final WebElement element = mock( WebElement.class );

		final MetadataProvider cut = WebElementMetadataProvider.of( element );

		assertThat( cut.retrieve() ).isEmpty();
	}
}
