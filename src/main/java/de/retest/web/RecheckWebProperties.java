package de.retest.web;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.Reloadable;

import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ScreenshotProviders.ScreenshotProviderConverter;

@LoadPolicy( LoadType.MERGE )
@Sources( { "system:properties", "file:${projectroot}/.retest/retest.properties" } )
public interface RecheckWebProperties extends Reloadable {

	/*
	 * Basic usage.
	 */

	static RecheckWebProperties getInstance() {
		final RecheckWebProperties instance = ConfigCache.getOrCreate( RecheckWebProperties.class );
		instance.reload();
		return instance;
	}

	/*
	 * Properties, their key constants and related functionality.
	 */

	static final String SCREENSHOT_PROVIDER_PROPERTY_KEY = "de.retest.recheck.web.screenshot.provider";

	@Key( SCREENSHOT_PROVIDER_PROPERTY_KEY )
	@DefaultValue( "viewportOnlyMinimal" )
	@ConverterClass( ScreenshotProviderConverter.class )
	ScreenshotProvider screenshotProvider();

}
