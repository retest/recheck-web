package de.retest.web;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.Reloadable;

import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ScreenshotProviders.ScreenshotProviderConverter;

/**
 * Interface for additional recheck-web properties, determined via system properties (first) or
 * <code>.retest/retest.properties</code> (second). For more information, please have a look at the
 * <a href="https://docs.retest.de/recheck-web/usage/configuration/#properties">documentation</a>.
 */
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

	/**
	 * @return The {@link ScreenshotProvider} to be used.
	 */
	@Key( SCREENSHOT_PROVIDER_PROPERTY_KEY )
	@DefaultValue( "viewportOnlyMinimal" )
	@ConverterClass( ScreenshotProviderConverter.class )
	ScreenshotProvider screenshotProvider();

}
