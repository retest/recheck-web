package de.retest.web;

import static de.retest.web.screenshot.ScreenshotProviders.shoot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.meta.MetadataProvider;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.recheck.ui.diff.ElementDifference;
import de.retest.web.mapping.PathsToWebDataMapping;
import de.retest.web.meta.SeleniumMetadataProvider;
import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.ImplicitDriverWrapper;
import de.retest.web.selenium.UnbreakableDriver;
import de.retest.web.selenium.WriteToResultWarningConsumer;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;

public class RecheckSeleniumAdapter implements RecheckAdapter {

	private static final String GET_ALL_ELEMENTS_BY_PATH_JS_PATH = "/javascript/getAllElementsByPath.js";

	private static final Logger logger = LoggerFactory.getLogger( RecheckSeleniumAdapter.class );

	private final DefaultValueFinder defaultValueFinder = new DefaultWebValueFinder();

	private final RetestIdProvider retestIdProvider;
	private final ScreenshotProvider screenshotProvider;

	private ActionReplayResult actionReplayResult;

	public RecheckSeleniumAdapter( final RecheckOptions options ) {
		retestIdProvider = options.getRetestIdProvider();
		screenshotProvider = getScreenshotProvider( options );
	}

	private ScreenshotProvider getScreenshotProvider( final RecheckOptions options ) {
		if ( options instanceof RecheckWebOptions ) {
			final ScreenshotProvider fromOptions = ((RecheckWebOptions) options).getScreenshotProvider();
			if ( fromOptions != null ) {
				return fromOptions;
			}
		}
		return RecheckWebProperties.getInstance().screenshotProvider();
	}

	public RecheckSeleniumAdapter() {
		this( RecheckWebOptions.builder().build() );
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
		final RemoteWebElement webElement = retrieveWebElement( toVerify );
		if ( webElement != null ) {
			return convertWebElement( webElement );
		}
		final WebDriver webDriver = retrieveWebDriver( unwrapImplicitDriver( toVerify ) );
		if ( webDriver != null ) {
			return convertWebDriver( webDriver );
		}
		throw new IllegalArgumentException( "Cannot convert objects of type '" + toVerify.getClass().getName() + "'." );
	}

	private RemoteWebElement retrieveWebElement( final Object toVerify ) {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, toVerify ) ) {
			return retrieveWebElement( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebElement ) {
			return (RemoteWebElement) toVerify;
		}
		return null;
	}

	private Object unwrapImplicitDriver( final Object toVerify ) {
		if ( toVerify instanceof AutocheckingRecheckDriver ) {
			throw new UnsupportedOperationException( String.format(
					"The '%s' does implicit checking after each action, " //
							+ "therefore no explicit check with 'Recheck#check' is needed. " //
							+ "Please remove the explicit check. " //
							+ "For more information, please have a look at https://docs.retest.de/recheck-web/introduction/usage/.",
					toVerify.getClass().getSimpleName() ) );
		}
		if ( toVerify instanceof ImplicitDriverWrapper ) {
			return ((ImplicitDriverWrapper) toVerify).getWrappedDriver();
		}
		return toVerify;
	}

	private WebDriver retrieveWebDriver( final Object toVerify ) {
		if ( toVerify instanceof UnbreakableDriver ) {
			return (UnbreakableDriver) toVerify;
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, toVerify ) ) {
			return retrieveWebDriver( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, toVerify ) );
		}
		if ( toVerify instanceof RemoteWebDriver ) {
			return (RemoteWebDriver) toVerify;
		}
		return null;
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
		final BufferedImage screenshot = shoot( driver, webElement, screenshotProvider );
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		final Object result = jsExecutor.executeScript( getQueryJS(), webElement );
		@SuppressWarnings( "unchecked" )
		// [["//html[1]", {tagName=html, ...}], ["//html[1]/body[1]", {tagName=body, ...}]]
		final List<List<Object>> tagMapping = (List<List<Object>>) result;
		debug( tagMapping );
		final RootElement lastChecked = convert( tagMapping, driver.getCurrentUrl(), driver.getTitle(), screenshot );

		final FrameConverter frameConverter = new FrameConverter( getQueryJS(), retestIdProvider, defaultValueFinder );
		frameConverter.addChildrenFromFrames( driver, lastChecked );

		if ( driver instanceof UnbreakableDriver ) {
			((UnbreakableDriver) driver).setLastActualState( lastChecked );
			((UnbreakableDriver) driver)
					.setWarningConsumer( new WriteToResultWarningConsumer( this::retrieveDifferences ) );
		}

		return Collections.singleton( lastChecked );
	}

	private void debug( final List<List<Object>> tagMapping ) {
		tagMapping.stream().filter( entry -> ((String) entry.get(0)).contains( "pseudo" ) )
				.forEach( entry -> logger.warn( "{} | {}", entry.get(0), entry.get(1) ) );
	}

	public RootElement convert( final List<List<Object>> tagMapping, final String url, final String title,
			final BufferedImage screenshot ) {
		final PathsToWebDataMapping mapping = new PathsToWebDataMapping( tagMapping );

		logger.info( "Checking website {} with {} elements.", url, mapping.size() );
		return new PeerConverter( retestIdProvider, mapping, title, screenshot, defaultValueFinder,
				mapping.getRootPath() ).convertToPeers();
	}

	private String getQueryJS() {
		try ( final InputStream url = getClass().getResourceAsStream( GET_ALL_ELEMENTS_BY_PATH_JS_PATH ) ) {
			return String.join( "\n", IOUtils.readLines( url, StandardCharsets.UTF_8 ) );
		} catch ( final IOException e ) {
			throw new UncheckedIOException( "Exception reading '" + GET_ALL_ELEMENTS_BY_PATH_JS_PATH + "'.", e );
		}
	}

	private Stream<ElementDifference> retrieveDifferences() {
		if ( actionReplayResult == null ) {
			return Stream.empty();
		}
		return actionReplayResult.getAllElementDifferences().stream();
	}

	@Override
	public Map<String, String> retrieveMetadata( final Object toCheck ) {
		final MetadataProvider provider = SeleniumMetadataProvider.of( toCheck );
		return provider.retrieve();
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return defaultValueFinder;
	}

	@Override
	public void notifyAboutDifferences( final ActionReplayResult actionReplayResult ) {
		this.actionReplayResult = actionReplayResult;
	}
}
