package de.retest.web;

import static de.retest.web.ScreenshotProvider.shoot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RetestIdProviderUtil;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.selenium.UnbreakableDriver;

public class RecheckSeleniumAdapter implements RecheckAdapter {

	public static final RetestIdProvider idProvider = RetestIdProviderUtil.getConfiguredRetestIdProvider();

	private static final String GET_ALL_ELEMENTS_BY_PATH_JS_PATH = "/javascript/getAllElementsByPath.js";

	private static final Logger logger = LoggerFactory.getLogger( RecheckSeleniumAdapter.class );

	private final RetestIdProvider retestIdProvider;

	private final AttributesProvider attributesProvider;

	private final DefaultValueFinder defaultValueFinder = new DefaultWebValueFinder();

	public RecheckSeleniumAdapter( final RetestIdProvider retestIdProvider,
			final AttributesProvider attributesProvider ) {
		this.retestIdProvider = retestIdProvider;
		this.attributesProvider = attributesProvider;
		logger.debug( "New RecheckSeleniumAdapter created: {}.", System.identityHashCode( this ) );
	}

	public RecheckSeleniumAdapter() {
		this( idProvider, YamlAttributesProvider.getInstance() );
	}

	@Override
	public boolean canCheck( final Object toVerify ) {
		return toVerify instanceof WebDriver;
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		final WebDriver driver = (WebDriver) toVerify;

		logger.info( "Retrieving attributes for each element." );
		final Set<String> cssAttributes = attributesProvider.getCssAttributes();
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		@SuppressWarnings( "unchecked" ) final Map<String, Map<String, Object>> result =
				(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes );

		logger.info( "Checking website {} with {} elements.", driver.getCurrentUrl(), result.size() );
		final RootElement lastChecked = convertToPeers( result, driver.getTitle(), shoot( driver ) );
		if ( driver instanceof UnbreakableDriver ) {
			((UnbreakableDriver) driver).setLastActualState( lastChecked );
		}

		return Collections.singleton( lastChecked );
	}

	public String getQueryJS() {
		try ( final InputStream url = getClass().getResourceAsStream( GET_ALL_ELEMENTS_BY_PATH_JS_PATH ) ) {
			return String.join( "\n", IOUtils.readLines( url, StandardCharsets.UTF_8 ) );
		} catch ( final IOException e ) {
			throw new UncheckedIOException( "Exception reading '" + GET_ALL_ELEMENTS_BY_PATH_JS_PATH + "'.", e );
		}
	}

	public RootElement convertToPeers( final Map<String, Map<String, Object>> data, final String title,
			final BufferedImage screenshot ) {
		return new PeerConverter( retestIdProvider, data, title, screenshot, defaultValueFinder ).convertToPeers();
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return defaultValueFinder;
	}

}
