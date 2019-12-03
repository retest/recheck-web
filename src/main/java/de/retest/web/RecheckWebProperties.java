package de.retest.web;

import org.aeonbits.owner.ConfigCache;

import de.retest.recheck.RecheckProperties;
import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ScreenshotProviders.ScreenshotProviderConverter;

public interface RecheckWebProperties extends RecheckProperties {

	static void init() {
		RecheckProperties.init();
	}

	static RecheckWebProperties getInstance() {
		return ConfigCache.getOrCreate( RecheckWebProperties.class, System.getProperties() );
	}

	@Key( "de.retest.recheck.web.screenshotProvider" )
	@DefaultValue( "viewportOnly" )
	@ConverterClass( ScreenshotProviderConverter.class )
	ScreenshotProvider screenshotProvider();

}
