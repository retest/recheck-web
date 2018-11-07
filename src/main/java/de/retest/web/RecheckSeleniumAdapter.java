package de.retest.web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.ui.DefaultValueFinder;
import de.retest.ui.descriptors.IdentifyingAttributes;
import de.retest.ui.descriptors.RetestIdProvider;
import de.retest.ui.descriptors.RootElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;

public class RecheckSeleniumAdapter implements RecheckAdapter {

	// TODO Remove again after retest 3.1.0 release...
	public static final RetestIdProvider idProvider = new RetestIdProvider() {
		@Override
		public String getRetestId( final IdentifyingAttributes identifyingAttributes ) {
			return UUID.randomUUID().toString();
		}

		@Override
		public void reset() {
			// not needed because UUID is globally unique
		}
	};

	private static final String GET_ALL_ELEMENTS_BY_PATH_JS_PATH = "/javascript/getAllElementsByPath.js";

	private static final Logger logger = LoggerFactory.getLogger( RecheckSeleniumAdapter.class );

	private final DefaultValuesProvider defaultValuesProvider;

	public RecheckSeleniumAdapter() {
		defaultValuesProvider = new DefaultValuesProvider();
	}

	@Override
	public boolean canCheck( final Object toVerify ) {
		return toVerify instanceof WebDriver;
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		final WebDriver driver = (WebDriver) toVerify;

		final List<String> attributes = AttributesProvider.getInstance().getJoinedAttributes();
		logger.info( "Retrieving {} attributes for each element.", attributes.size() );
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		@SuppressWarnings( "unchecked" )
		final Map<String, Map<String, Object>> result =
				(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), attributes );

		logger.info( "Checking website {} with {} elements.", driver.getCurrentUrl(), result.size() );

		return Collections.singleton( convertToPeers( result, driver.getTitle(), createScreenshot( driver ) ) );
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
		idProvider.reset();
		final Map<String, WebElementPeer> converted = new HashMap<>();
		RootElementPeer root = null;
		for ( final Map.Entry<String, Map<String, Object>> entry : sort( data ) ) {
			final String path = entry.getKey();
			logger.debug( "Found element with path '{}'.", path );
			final String parentPath = getParentPath( path );
			final WebData webData = new WebData( entry.getValue() );
			if ( shouldIgnore( webData ) ) {
				continue;
			}
			WebElementPeer peer = converted.get( path );

			assert peer == null : "List is sorted, we should not have path twice.";

			if ( parentPath == null ) {
				root = new RootElementPeer( defaultValuesProvider, webData, path, title, screenshot );
				peer = root;
			} else {
				peer = new WebElementPeer( defaultValuesProvider, webData, path );
				final WebElementPeer parent = converted.get( parentPath );
				assert parent != null : "We sorted the map, parent should already be there.";
				parent.addChild( peer );
			}

			converted.put( path, peer );
		}

		if ( root == null ) {
			throw new NullPointerException( "RootElementPeer is null." );
		}

		return root.toElement();
	}

	private boolean shouldIgnore( final WebData webData ) {
		return !webData.isShown() //
				&& !webData.getTag().equalsIgnoreCase( "title" ) //
				&& !webData.getTag().equalsIgnoreCase( "meta" ) //
				&& !webData.getTag().equalsIgnoreCase( "head" ) //
				&& !webData.getTag().equalsIgnoreCase( "option" );
	}

	private List<Map.Entry<String, Map<String, Object>>> sort( final Map<String, Map<String, Object>> data ) {
		// Sorting ensures that parents are already created.
		return data.entrySet().stream() //
				.sorted( Comparator.comparing( Entry::getKey ) ) //
				.collect( Collectors.toList() );
	}

	private BufferedImage createScreenshot( final WebDriver driver ) {
		final ShootingStrategy shootingStrategy =
				new ViewportPastingDecorator( new CustomShootingStrategy() ).withScrollTimeout( 100 );
		final AShot aShot = new AShot().shootingStrategy( shootingStrategy );
		return aShot.takeScreenshot( driver ).getImage();
	}

	static String getParentPath( final String path ) {
		final String parentPath = path.substring( 0, path.lastIndexOf( '/' ) );
		if ( parentPath.length() == 1 ) {
			return null;
		}
		return parentPath;
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		// TODO DefaultValueFinder is just a stub.
		return ( identifyingAttributes, attributesKey ) -> null;
	}

}
