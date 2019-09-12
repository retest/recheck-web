package de.retest.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class FrameConverter {

	private final String queryJs;
	private final RetestIdProvider retestIdProvider;
	private final AttributesProvider attributesProvider;
	private final DefaultValueFinder defaultValueFinder;

	public void addChildrenFromFrames( final WebDriver driver, final Set<String> cssAttributes,
			final RootElement lastChecked ) {
		final List<Element> frames =
				de.retest.web.selenium.By.findElements( lastChecked.getContainedElements(), isFrame() );
		log.debug( "Found {} frame(s), getting data per frame.", frames.size() );
		for ( final Element frame : frames ) {
			addChildrenFromFrame( driver, cssAttributes, frame );
			driver.switchTo().defaultContent();
		}
	}

	private void addChildrenFromFrame( final WebDriver driver, final Set<String> cssAttributes, final Element frame ) {
		try {
			final String framePath = frame.getIdentifyingAttributes().getPath();
			final String frameXPath = "/" + framePath;
			final WebElement frameWebElement = driver.findElement( By.xpath( frameXPath ) );

			log.debug( "Switching to frame '{}'.", frame );
			driver.switchTo().frame( frameWebElement );

			log.debug( "Retrieving data content of frame '{}'.", frame );
			final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			@SuppressWarnings( "unchecked" )
			final PathsToWebDataMapping mapping = new PathsToWebDataMapping( framePath,
					(Map<String, Map<String, Object>>) jsExecutor.executeScript( queryJs, cssAttributes ) );
			final RootElement frameContent = convert( mapping, getFrameTitle( frame ), framePath );
			frame.addChildren( frameContent.getContainedElements() );
		} catch ( final Exception e ) {
			log.error( "Exception retrieving data content of frame '{}'.", frame, e );
		}
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
		final PeerConverter peerConverter = new PeerConverter( retestIdProvider, attributesProvider, mapping,
				frameTitle, null, defaultValueFinder, framePath ) {
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
