package de.retest.web;

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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.mapping.PathsToWebDataMapping;
import de.retest.web.selenium.UnbreakableDriver;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;

public class RecheckSeleniumAdapter implements RecheckAdapter {

	private static final String GET_ALL_ELEMENTS_BY_PATH_JS_PATH = "/javascript/getAllElementsByPath.js";

	private static final Logger logger = LoggerFactory.getLogger( RecheckSeleniumAdapter.class );

	private final DefaultValueFinder defaultValueFinder = new DefaultWebValueFinder();

	private final RetestIdProvider retestIdProvider;
	private final AttributesProvider attributesProvider;
	private final ScreenshotProvider screenshotProvider;

	/**
	 * @deprecated Use {@link #RecheckSeleniumAdapter(RecheckOptions)} instead.
	 */
	@Deprecated
	public RecheckSeleniumAdapter( final RetestIdProvider retestIdProvider,
			final AttributesProvider attributesProvider ) {
		this.retestIdProvider = retestIdProvider;
		this.attributesProvider = attributesProvider;
		screenshotProvider = new ScreenshotProvider();
		logger.debug( "New RecheckSeleniumAdapter created: {}.", System.identityHashCode( this ) );
	}

	public RecheckSeleniumAdapter() {
		this( RecheckWebOptions.builder().build() );
	}

	public RecheckSeleniumAdapter( final RecheckOptions options ) {
		retestIdProvider = options.getRetestIdProvider();
		if ( options instanceof RecheckWebOptions ) {
			final RecheckWebOptions webOptions = (RecheckWebOptions) options;
			attributesProvider = webOptions.getAttributesProvider();
			screenshotProvider = webOptions.getScreenshotProvider();
		} else {
			attributesProvider = YamlAttributesProvider.getInstance();
			screenshotProvider = new ScreenshotProvider();
		}
	}

	@Override
	public RecheckAdapter initialize( final RecheckOptions opts ) {
		return new RecheckSeleniumAdapter( opts );
	}

	@Override
	public boolean canCheck( final Object toVerify ) {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, toVerify ) ) {
			return canCheck( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebElement ) {
			return true;
		}
		if ( toVerify instanceof UnbreakableDriver ) {
			return true;
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, toVerify ) ) {
			return canCheck( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebDriver ) {
			return true;
		}
		return false;
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, toVerify ) ) {
			return convert( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebElement ) {
			return convertWebElement( (RemoteWebElement) toVerify );
		}
		if ( toVerify instanceof UnbreakableDriver ) {
			return convertWebDriver( (UnbreakableDriver) toVerify );
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, toVerify ) ) {
			return convert( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebDriver ) {
			return convertWebDriver( (RemoteWebDriver) toVerify );
		}
		throw new IllegalArgumentException( "Cannot convert objects of type '" + toVerify.getClass().getName() + "'." );
	}

	Set<RootElement> convertWebDriver( final WebDriver driver ) {
		logger.info( "Retrieving attributes for each element." );
		return convert( driver, null );
	}

	Set<RootElement> convertWebElement( final RemoteWebElement webElement ) {
		logger.info( "Retrieving attributes for element '{}'.", webElement );
		return convert( webElement.getWrappedDriver(), webElement );
	}

	private Set<RootElement> convert( final WebDriver driver, final RemoteWebElement webElement ) {
		// Do not inline this, as we want the screenshot created before retrieving elements
		final BufferedImage screenshot = screenshotProvider.shoot( driver, webElement );
		final Set<String> cssAttributes = attributesProvider.getCssAttributes();
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		@SuppressWarnings( "unchecked" )
		final Map<String, Map<String, Object>> tagMapping =
				(Map<String, Map<String, Object>>) jsExecutor.executeScript( getQueryJS(), cssAttributes, webElement );
		final RootElement lastChecked = convert( tagMapping, driver.getCurrentUrl(), driver.getTitle(), screenshot );

		final FrameConverter frameConverter =
				new FrameConverter( getQueryJS(), retestIdProvider, attributesProvider, defaultValueFinder );
		frameConverter.addChildrenFromFrames( driver, cssAttributes, lastChecked );

		if ( driver instanceof UnbreakableDriver ) {
			((UnbreakableDriver) driver).setLastActualState( lastChecked );
		}

		return Collections.singleton( lastChecked );
	}

	public RootElement convert( final Map<String, Map<String, Object>> tagMapping, final String url, final String title,
			final BufferedImage screenshot ) {
		final PathsToWebDataMapping mapping = new PathsToWebDataMapping( tagMapping );

		logger.info( "Checking website {} with {} elements.", url, mapping.size() );
		return new PeerConverter( retestIdProvider, attributesProvider, mapping, title, screenshot, defaultValueFinder,
				mapping.getRootPath() ).convertToPeers();
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

}
