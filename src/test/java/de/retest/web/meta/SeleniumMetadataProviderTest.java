package de.retest.web.meta;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.recheck.meta.MetadataProvider;

class SeleniumMetadataProviderTest {

	@Test
	void retrieve_should_identify_driver() throws Exception {
		final WebDriver driver = mock( RemoteWebDriver.class, RETURNS_MOCKS );

		final MetadataProvider cut = SeleniumMetadataProvider.of( driver );

		assertThat( cut.retrieve() ) //
				.contains( Pair.of( SeleniumMetadata.CHECK_TYPE, SeleniumMetadataProvider.TYPE_DRIVER ) );
	}

	@Test
	void retrieve_should_identify_wrapped_driver() throws Exception {
		final WrapsDriver wrapsDriver = mock( WrapsDriver.class );
		when( wrapsDriver.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class, RETURNS_MOCKS ) );

		final MetadataProvider cut = SeleniumMetadataProvider.of( wrapsDriver );

		assertThat( cut.retrieve() ) //
				.contains( Pair.of( SeleniumMetadata.CHECK_TYPE, SeleniumMetadataProvider.TYPE_DRIVER ) );
	}

	@Test
	void retrieve_should_identify_element() throws Exception {
		final WebElement element = mock( WebElement.class, RETURNS_MOCKS );

		final MetadataProvider cut = SeleniumMetadataProvider.of( element );

		assertThat( cut.retrieve() ) //
				.contains( Pair.of( SeleniumMetadata.CHECK_TYPE, SeleniumMetadataProvider.TYPE_ELEMENT ) );
	}

	@Test
	void retrieve_should_identify_wrapped_element() throws Exception {
		final WrapsElement wrapsElement = mock( WrapsElement.class );
		when( wrapsElement.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class, RETURNS_MOCKS ) );

		final MetadataProvider cut = SeleniumMetadataProvider.of( wrapsElement );

		assertThat( cut.retrieve() ) //
				.contains( Pair.of( SeleniumMetadata.CHECK_TYPE, SeleniumMetadataProvider.TYPE_ELEMENT ) );
	}
}
