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
		final String frameId = frame.getIdentifyingAttributes().get( "id" );
		if ( frameId == null ) {
			// TODO Implement handling e.g. via name, XPath, etc.
			log.error( "Cannot retrieve frame without ID from {}.", frame );
			return;
		}
		try {
			log.debug( "Switching to frame with ID '{}'.", frameId );
			driver.switchTo().frame( frameId );
			final String framePath = frame.getIdentifyingAttributes().getPath();
			@SuppressWarnings( "unchecked" )
			final PathsToWebDataMapping mapping = new PathsToWebDataMapping( framePath,
					(Map<String, Map<String, Object>>) jsExecutor.executeScript( queryJs, cssAttributes ) );
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
			log.error( "Exception retrieving data content of frame with ID '{}'.", frameId, e );
		}
	}

	private static Predicate<Element> isFrame() {
		return element -> {
			final String type = element.getIdentifyingAttributes().getType();
			return Stream.of( "iframe", "frame" ).anyMatch( type::equalsIgnoreCase );
		};
	}

}
