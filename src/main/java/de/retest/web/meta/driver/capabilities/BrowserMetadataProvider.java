package de.retest.web.meta.driver.capabilities;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.meta.SeleniumMetadata;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( access = AccessLevel.PACKAGE )
public final class BrowserMetadataProvider implements MetadataProvider {

	private final Capabilities capabilities;

	@Override
	public Map<String, String> retrieve() {
		final Map<String, String> map = new HashMap<>();

		map.put( SeleniumMetadata.BROWSER_NAME, capabilities.getBrowserName() );
		map.put( SeleniumMetadata.BROWSER_VERSION, capabilities.getVersion() );

		return map;
	}
}
