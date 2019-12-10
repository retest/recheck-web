package de.retest.web.meta.driver.capabilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Capabilities;

import de.retest.web.meta.SeleniumMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlatformMetadataProviderTest {

	@ParameterizedTest
	@MethodSource( "de.retest.web.meta.driver.capabilities.AllCapabilities#capabilities" )
	void retrieve_should_properly_resolve_platform( final Capabilities capabilities ) throws Exception {
		final PlatformMetadataProvider cut = new PlatformMetadataProvider( capabilities );

		assertThat( cut.retrieve() ) //
				.containsKeys( SeleniumMetadata.OS_NAME );
	}
}
