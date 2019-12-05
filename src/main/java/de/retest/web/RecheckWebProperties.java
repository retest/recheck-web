package de.retest.web;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigCache;
import org.aeonbits.owner.ConfigFactory;

import de.retest.recheck.configuration.ProjectRootFinderUtil;
import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ScreenshotProviders.ScreenshotProviderConverter;

@Sources( "file:${projectroot}/.retest/retest.properties" )
public interface RecheckWebProperties extends Config {

	/*
	 * Basic usage.
	 */

	static void init() {
		ProjectRootFinderUtil.getProjectRoot().ifPresent(
				projectRoot -> ConfigFactory.setProperty( "projectroot", projectRoot.toAbsolutePath().toString() ) );
	}

	static RecheckWebProperties getInstance() {
		return ConfigCache.getOrCreate( RecheckWebProperties.class, System.getProperties() );
	}

	/*
	 * Properties, their key constants and related functionality.
	 */

	static final String SCREENSHOT_PROVIDER_PROPERTY_KEY = "de.retest.recheck.web.screenshotProvider";

	@Key( SCREENSHOT_PROVIDER_PROPERTY_KEY )
	@DefaultValue( "viewportOnly" )
	@ConverterClass( ScreenshotProviderConverter.class )
	ScreenshotProvider screenshotProvider();

}
