package de.retest.web;

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
		log.debug( "Found {} frame(s), getting data per frame.", frames.size() );
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
					(Map<String, Map<String, Object>>) jsExecutor.executeScript( queryJs ) );
			final RootElement frameContent = convert( mapping, getFrameTitle( frame ), framePath );

			final FrameConverter frameConverter =
					new FrameConverter( queryJs, retestIdProvider, defaultValueFinder, framePath + "/" );
			frameConverter.addChildrenFromFrames( driver, frameContent );

			frame.addChildren( frameContent.getContainedElements() );
		} catch ( final Exception e ) {
			log.error( "Exception retrieving data content of frame '{}'.", frame, e );
		}
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
