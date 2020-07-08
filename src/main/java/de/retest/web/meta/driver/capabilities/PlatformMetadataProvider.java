package de.retest.web.meta.driver.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.meta.SeleniumMetadata;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( access = AccessLevel.PACKAGE )
public final class PlatformMetadataProvider implements MetadataProvider {

	private final Capabilities capabilities;

	@Override
	public Map<String, String> retrieve() {
		final Map<String, String> map = new HashMap<>();

		final Platform platform = retrievePlatformSilently();
		if ( platform != null ) {
			map.put( SeleniumMetadata.OS_NAME, platform.toString() );
			final Object version = capabilities.getCapability( "platformVersion" );
			map.put( SeleniumMetadata.OS_VERSION, Objects.toString( version, "" ) );
			map.put( SeleniumMetadata.OS_ARCH, "" );
		}

		return map;
	}

	private Platform retrievePlatformSilently() {
		try {
			return capabilities.getPlatform();
		} catch ( final IllegalArgumentException | WebDriverException e ) {
			// failed
			return null;
		}
	}
}
