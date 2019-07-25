package de.retest.web;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.ui.DefaultValueFinder;
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
		final JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		final String frameName = frame.getIdentifyingAttributes().get( "name" );
		final String frameId = frame.getIdentifyingAttributes().get( "id" );
		final String frameNameOrID = frameName != null ? frameName : frameId;
		if ( frameNameOrID == null ) {
			// TODO Implement handling e.g. XPath.
			log.error( "Cannot retrieve frame without name and ID from {}.", frame );
			return;
		}
		try {
			log.debug( "Switching to frame with name/ID '{}'.", frameNameOrID );
			driver.switchTo().frame( frameNameOrID );
			final String framePath = frame.getIdentifyingAttributes().getPath();
			@SuppressWarnings( "unchecked" )
			final PathsToWebDataMapping mapping = new PathsToWebDataMapping( framePath,
					(Map<String, Map<String, Object>>) jsExecutor.executeScript( queryJs, cssAttributes ) );
			final RootElement frameContent = convert( mapping, frameNameOrID, framePath );
			frame.addChildren( frameContent.getContainedElements() );
		} catch ( final Exception e ) {
			log.error( "Exception retrieving data content of frame with name/ID '{}'.", frameNameOrID, e );
		}
	}

	private RootElement convert( final PathsToWebDataMapping mapping, final String frameNameOrID,
			final String framePath ) {
		final PeerConverter peerConverter = new PeerConverter( retestIdProvider, attributesProvider, mapping,
				"frame-" + frameNameOrID, null, defaultValueFinder ) {
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
