package de.retest.web;

import static java.util.stream.Collectors.joining;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.mapping.PathsToWebDataMapping;
import de.retest.web.mapping.WebData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class FrameConverter {

	private final String queryJs;
	private final RetestIdProvider retestIdProvider;
	private final DefaultValueFinder defaultValueFinder;
	private final String parentFramePrefix;

	public FrameConverter( final String queryJs, final RetestIdProvider retestIdProvider,
			final DefaultValueFinder defaultValueFinder ) {
		this( queryJs, retestIdProvider, defaultValueFinder, "" );
	}

	public void addChildrenFromFrames( final WebDriver driver, final RootElement lastChecked ) {
		final List<Element> frames =
				de.retest.web.selenium.By.findElements( lastChecked.getContainedElements(), isFrame() );
		if ( frames.size() <= 0 ) {
			log.debug( "Found no sub-frames." );
			return;
		}
		log.debug( "Found {} sub-frame(s), getting data per frame.", frames.size() );
		for ( final Element frame : frames ) {
			addChildrenFromFrame( driver, frame );
			driver.switchTo().defaultContent();
		}
	}

	private void addChildrenFromFrame( final WebDriver driver, final Element frame ) {
		try {
			final WebElement frameWebElement = getFrameParent( driver, frame );

			log.debug( "Switching to frame '{}'.", frame );
			driver.switchTo().frame( frameWebElement );

			log.debug( "Retrieving data content of frame '{}'.", frame );
			final String framePath = frame.getIdentifyingAttributes().getPath();
			final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			@SuppressWarnings( "unchecked" )
			final PathsToWebDataMapping mapping = new PathsToWebDataMapping( framePath,
					(List<List<Object>>) jsExecutor.executeScript( queryJs ) );
			debug( mapping );
			final RootElement frameContent = convert( mapping, getFrameTitle( frame ), framePath );

			final FrameConverter frameConverter =
					new FrameConverter( queryJs, retestIdProvider, defaultValueFinder, framePath + "/" );
			frameConverter.addChildrenFromFrames( driver, frameContent );

			frame.addChildren( frameContent.getContainedElements() );
		} catch ( final Exception e ) {
			log.error( "Exception retrieving data content of frame '{}'.", frame, e );
		}
	}

	private void debug( final PathsToWebDataMapping mapping ) {
		final Map<String, WebData> tagMapping = new LinkedHashMap<>();
		mapping.forEach( entry -> tagMapping.put( entry.getKey(), entry.getValue() ) );
		tagMapping.entrySet().stream().filter( entry -> entry.getKey().contains( "pseudo" ) )
				.forEach( entry -> log.warn( "{} | {}", entry.getKey(), entry.getValue().getKeys().stream()
						.map( key -> key + "=" + entry.getValue().getAsString( key ) ).collect( joining( ", " ) ) ) );
	}

	private WebElement getFrameParent( final WebDriver driver, final Element frame ) {
		final String framePath = frame.getIdentifyingAttributes().getPath().substring( parentFramePrefix.length() );
		final String frameXPath = "/" + framePath;
		return driver.findElement( By.xpath( frameXPath ) );
	}

	private String getFrameTitle( final Element frame ) {
		final String prefix = "frame-";
		final Attribute id = frame.getIdentifyingAttributes().getAttribute( AttributesUtil.ID );
		if ( id != null ) {
			return prefix + id.getValue();
		}
		final Attribute name = frame.getIdentifyingAttributes().getAttribute( AttributesUtil.NAME );
		if ( name != null ) {
			return prefix + name.getValue();
		}
		return prefix + frame.getRetestId();
	}

	private RootElement convert( final PathsToWebDataMapping mapping, final String frameTitle,
			final String framePath ) {
		final PeerConverter peerConverter =
				new PeerConverter( retestIdProvider, mapping, frameTitle, null, defaultValueFinder, framePath ) {
					@Override
					protected boolean isRoot( final String parentPath ) {
						// Handle trailing slashes.
						return framePath.equals( parentPath.replaceAll( "/$", "" ) );
					}
				};
		return peerConverter.convertToPeers();
	}

	private static Predicate<Element> isFrame() {
		return element -> {
			final String type = element.getIdentifyingAttributes().getType();
			return Stream.of( "iframe", "frame" ).anyMatch( type::equalsIgnoreCase );
		};
	}
}
