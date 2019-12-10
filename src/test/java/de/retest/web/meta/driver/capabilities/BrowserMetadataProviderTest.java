package de.retest.web.meta.driver.capabilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Capabilities;

import de.retest.web.meta.SeleniumMetadata;

class BrowserMetadataProviderTest {

	@ParameterizedTest
	@MethodSource( "de.retest.web.meta.driver.capabilities.AllCapabilities#capabilities" )
	void retrieve_should_gather_browser_name_and_version( final Capabilities capabilities ) throws Exception {
		final BrowserMetadataProvider cut = new BrowserMetadataProvider( capabilities );

		assertThat( cut.retrieve() ) //
				.containsKeys( SeleniumMetadata.BROWSER_NAME, SeleniumMetadata.BROWSER_VERSION );
	}
}
