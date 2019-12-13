package de.retest.web.meta.driver;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.recheck.meta.MultiMetadataProvider;
import de.retest.web.meta.SeleniumMetadata;
import de.retest.web.meta.driver.capabilities.CapabilityMetadataProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( access = AccessLevel.PACKAGE )
public final class WebDriverMetadataProvider implements MetadataProvider {

	private final WebDriver driver;

	public static MetadataProvider of( final WebDriver driver ) {
		return MultiMetadataProvider.of( // 
				CapabilityMetadataProvider.of( driver ), //
				new WebDriverMetadataProvider( driver ) //
		);
	}

	@Override
	public Map<String, String> retrieve() {
		final Map<String, String> map = new HashMap<>();

		map.put( SeleniumMetadata.DRIVER_TYPE, driver.getClass().getSimpleName() );
		map.put( SeleniumMetadata.URL, driver.getCurrentUrl() );

		final Dimension size = driver.manage().window().getSize();
		map.put( SeleniumMetadata.WINDOW_WIDTH, String.valueOf( size.getWidth() ) );
		map.put( SeleniumMetadata.WINDOW_HEIGHT, String.valueOf( size.getHeight() ) );

		return map;
	}
}
