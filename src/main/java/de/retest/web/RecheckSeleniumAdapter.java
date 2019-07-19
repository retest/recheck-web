package de.retest.web;

import static de.retest.web.ScreenshotProvider.shoot;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.recheck.util.RetestIdProviderUtil;
import de.retest.web.mapping.PathsToWebDataMapping;
import de.retest.web.selenium.UnbreakableDriver;

public class RecheckSeleniumAdapter implements RecheckAdapter {

	private static final String GET_ALL_ELEMENTS_BY_PATH_JS_PATH = "/javascript/getAllElementsByPath.js";

	private static final Logger logger = LoggerFactory.getLogger( RecheckSeleniumAdapter.class );

	private final DefaultValueFinder defaultValueFinder = new DefaultWebValueFinder();
	private final Predicate<Element> isFrame = element -> {
		final String type = element.getIdentifyingAttributes().getType();
		return Stream.of( "iframe", "frame" ).anyMatch( type::equalsIgnoreCase );
	};

	private final RetestIdProvider retestIdProvider;
	private final AttributesProvider attributesProvider;

	private WebDriver driver;

	public RecheckSeleniumAdapter( final RetestIdProvider retestIdProvider,
			final AttributesProvider attributesProvider ) {
		this.retestIdProvider = retestIdProvider;
		this.attributesProvider = attributesProvider;
		logger.debug( "New RecheckSeleniumAdapter created: {}.", System.identityHashCode( this ) );
	}

	public RecheckSeleniumAdapter() {
		this( RetestIdProviderUtil.getConfiguredRetestIdProvider(), YamlAttributesProvider.getInstance() );
	}

	@Override
	public boolean canCheck( final Object toVerify ) {
		return toVerify instanceof WebDriver;
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		driver = (WebDriver) toVerify;

		logger.info( "Retrieving attributes for each element." );
		final Set<String> cssAttributes = attributesProvider.getCssAttributes();
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		@SuppressWarnings( "unchecked" )
		final PathsToWebDataMapping mapping = new PathsToWebDataMapping(
				(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes ) );

		logger.info( "Checking website {} with {} elements.", driver.getCurrentUrl(), mapping.size() );
		final RootElement lastChecked = new PeerConverter( retestIdProvider, attributesProvider, mapping,
				driver.getTitle(), shoot( driver ), defaultValueFinder ).convertToPeers();

		addChildrenFromFrames( driver, cssAttributes, lastChecked );

		if ( driver instanceof UnbreakableDriver ) {
			((UnbreakableDriver) driver).setLastActualState( lastChecked );
		}

		return Collections.singleton( lastChecked );
	}

	private void addChildrenFromFrames( final WebDriver driver, final Set<String> cssAttributes,
			final RootElement lastChecked ) {
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		final List<Element> frames =
				de.retest.web.selenium.By.findElements( lastChecked.getContainedElements(), isFrame );

		logger.debug( "Found {} frames, getting data per frame.", frames.size() );
		for ( final Element frame : frames ) {
			final String frameId = frame.getIdentifyingAttributes().get( "id" );
			if ( frameId == null ) {
				// TODO Implement handling e.g. via name, XPaht, etc.
				logger.error( "Cannot retrieve frame with ID null from {}.", frame );
				continue;
			}
			try {
				logger.debug( "Switching to frame with ID {}.", frameId );
				driver.switchTo().frame( frameId );
				final String framePath = frame.getIdentifyingAttributes().getPath();
				@SuppressWarnings( "unchecked" )
				final PathsToWebDataMapping mapping = new PathsToWebDataMapping( framePath,
						(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes ) );
				final RootElement frameContent = new PeerConverter( retestIdProvider, attributesProvider, mapping,
						"frame-" + frameId, null, defaultValueFinder ) {
					@Override
					protected boolean isRoot( final String parentPath ) {
						// handle trailing slashes...
						return framePath.equals( parentPath.replaceAll( "/$", "" ) );
					}
				}.convertToPeers();
				frame.addChildren( frameContent.getContainedElements() );
			} catch ( final Exception e ) {
				logger.error( "Exception retrieving data content of frame with ID {}.", frameId, e );
			}
			driver.switchTo().defaultContent();
		}
	}

	private String getQueryJS() {
		try ( final InputStream url = getClass().getResourceAsStream( GET_ALL_ELEMENTS_BY_PATH_JS_PATH ) ) {
			return String.join( "\n", IOUtils.readLines( url, StandardCharsets.UTF_8 ) );
		} catch ( final IOException e ) {
			throw new UncheckedIOException( "Exception reading '" + GET_ALL_ELEMENTS_BY_PATH_JS_PATH + "'.", e );
		}
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return defaultValueFinder;
	}

	@Override
	public void notifyAboutDifferences( final ActionReplayResult lastActionReplayResult ) {
		if ( driver instanceof UnbreakableDriver ) {
			logger.debug( "Notifying about differences for last action replay result: {}.", lastActionReplayResult );
			((UnbreakableDriver) driver).setLastActionReplayResult( lastActionReplayResult );
		}
		logger.debug( "Not notifying about differences because WebDriver {} is not an instance of UnbreakableDriver.",
				driver );

	}

}
