package de.retest.web;

import static de.retest.web.ScreenshotProvider.shoot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
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
		@SuppressWarnings( "unchecked" )
		final Map<String, Map<String, Object>> rawAttributesMapping =
				(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes );

		logger.info( "Checking website {} with {} elements.", driver.getCurrentUrl(), rawAttributesMapping.size() );
		final RootElement lastChecked = convertToPeers( rawAttributesMapping, driver.getTitle(), shoot( driver ) );

		addChildrenFromFrames( driver, cssAttributes, lastChecked );

		if ( driver instanceof UnbreakableDriver ) {
			((UnbreakableDriver) driver).setLastActualState( lastChecked );
		}

		return Collections.singleton( lastChecked );
	}

	public void addChildrenFromFrames( final WebDriver driver, final Set<String> cssAttributes,
			final RootElement lastChecked ) {
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		final List<Element> frames = de.retest.web.selenium.By.findElements( lastChecked.getContainedElements(),
				element -> "iframe".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )
						|| "frame".equalsIgnoreCase( element.getIdentifyingAttributes().getType() ) );

		logger.debug( "Found {} frames, getting HTML per frame.", frames.size() );
		for ( final Element frame : frames ) {
			final String frameId = frame.getIdentifyingAttributes().get( "id" );
			if ( frameId == null ) {
				// TODO Implement handling e.g. via name, XPaht, etc.
				logger.error( "Cannot retrieve frame with id null from {}.", frame );
				continue;
			}
			try {
				logger.debug( "Switching to frame with ID {}.", frameId );
				driver.switchTo().frame( frameId );
				@SuppressWarnings( "unchecked" )
				final Map<String, Map<String, Object>> frameAttributesMapping =
						(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes );
				final RootElement frameContent = convertToPeers( frameAttributesMapping, "", null );
				frame.addChildren( frameContent.getContainedElements() );
			} catch ( final Exception e ) {
				logger.error( "Exception retrieving HTML content of frame {}.", frameId, e );
			}
			driver.switchTo().defaultContent();
		}
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
		return new PeerConverter( retestIdProvider, attributesProvider, data, title, screenshot, defaultValueFinder )
				.convertToPeers();
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return defaultValueFinder;
	}

}
