package de.retest.web.selenium;

import static de.retest.web.selenium.ByWhisperer.retrieveCSSClassName;
import static de.retest.web.selenium.ByWhisperer.retrieveId;
import static de.retest.web.selenium.ByWhisperer.retrieveLinkText;
import static de.retest.web.selenium.ByWhisperer.retrieveName;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.ui.descriptors.Element;
import de.retest.ui.descriptors.RootElement;
import de.retest.util.Mapping;

public class TestHealer {

	private static final Logger logger = LoggerFactory.getLogger( TestHealer.class );

	private final RecheckDriver wrapped;
	private final RootElement lastExpectedState;
	private final RootElement lastActualState;

	public TestHealer( final RecheckDriver wrapped ) {
		this.wrapped = wrapped;
		lastExpectedState = wrapped.getLastExpectedState();
		lastActualState = wrapped.getLastActualState();
	}

	public static WebElement findElement( final By by, final RecheckDriver wrapped ) {
		return new TestHealer( wrapped ).findElement( by );
	}

	private WebElement findElement( final By by ) {
		if ( by instanceof ById ) {
			return findElementById( (ById) by );
		}
		if ( by instanceof ByClassName ) {
			return findElementByClassName( (ByClassName) by );
		}
		if ( by instanceof ByName ) {
			return findElementByName( (ByName) by );
		}
		if ( by instanceof ByLinkText ) {
			return findElementByLinkText( (ByLinkText) by );
		}
		if ( by instanceof ByCssSelector ) {
			return findElementByCssSelector( (ByCssSelector) by );
		}
		if ( by instanceof ByXPath ) {
			return findElementByXPath( (ByXPath) by );
		}
		throw new UnsupportedOperationException(
				"Healing tests with " + by.getClass().getSimpleName() + " not yet implemented" );
	}

	private WebElement findElementById( final ById by ) {
		final String id = retrieveId( by );
		final Mapping<Element, Element> oldNewMapping = de.retest.web.selenium.By.findElement( lastExpectedState,
				lastActualState, element -> id.equals( element.getAttributes().get( "id" ) )
						|| id.equals( element.getIdentifyingAttributes().get( "id" ) ) );
		final Element actualElement = oldNewMapping.getValue();
		if ( actualElement == null ) {
			throw new RuntimeException(
					"It appears that even the old state didn't have an element with id '" + id + "'." );
		}

		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The HTML id attribute used for element identification changed from '{}' to '{}'.", id,
				actualElement.getIdentifyingAttributes().get( "id" ) );
		logger.warn( "retest identified the element based on the persisted old state." );
		// TODO Get filename from state
		// TODO Guess test name from state name
		logger.warn( "If you apply these changes to the state {}, your test {} will break.", "", "" );
		// TODO Eventually, we want something like Test.java:123 instead of "your test"
		final String newId = actualElement.getIdentifyingAttributes().get( "id" );
		if ( newId != null ) {
			logger.warn( "Use `By.id(\"{}\")` or `By.retestId(\"{}\")` to update your test.", newId,
					oldNewMapping.getKey().getRetestId() );
		} else {
			logger.warn( "Use `By.retestId(\"{}\")` to update your test.", oldNewMapping.getKey().getRetestId() );
		}

		return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
	}

	private WebElement findElementByClassName( final ByClassName by ) {
		final String className = retrieveCSSClassName( by );
		final Mapping<Element, Element> oldNewMapping = de.retest.web.selenium.By.findElement( lastExpectedState,
				lastActualState,
				element -> element.getAttributes().get( "class" ) != null
						&& ((String) element.getAttributes().get( "class" )).contains( className )
						|| element.getIdentifyingAttributes().get( "class" ) != null
								&& ((String) element.getIdentifyingAttributes().get( "class" )).contains( className ) );
		final Element actualElement = oldNewMapping.getValue();
		if ( actualElement == null ) {
			throw new RuntimeException(
					"It appears that even the old state didn't have an element with CSS class '" + className + "'." );
		}

		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The HTML class attribute used for element identification changed from '{}' to '{}'.", className,
				actualElement.getIdentifyingAttributes().get( "class" ) );
		logger.warn( "retest identified the element based on the persisted old state." );
		logger.warn( "If you apply these changes to the state {}, your test {} will break.", "", "" );
		final String newClass = actualElement.getIdentifyingAttributes().get( "class" );
		if ( newClass != null ) {
			logger.warn( "Use `By.className(\"{}\")` or `By.retestId(\"{}\")` to update your test.", newClass,
					oldNewMapping.getKey().getRetestId() );
		} else {
			logger.warn( "Use `By.retestId(\"{}\")` to update your test.", oldNewMapping.getKey().getRetestId() );
		}

		return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
	}

	private WebElement findElementByName( final ByName by ) {
		final String name = retrieveName( by );
		final Mapping<Element, Element> oldNewMapping = de.retest.web.selenium.By.findElement( lastExpectedState,
				lastActualState, element -> name.equals( element.getAttributes().get( "name" ) )
						|| name.equals( element.getIdentifyingAttributes().get( "name" ) ) );
		final Element actualElement = oldNewMapping.getValue();
		if ( actualElement == null ) {
			throw new RuntimeException(
					"It appears that even the old state didn't have an element with name '" + name + "'." );
		}

		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The HTML name attribute used for element identification changed from '{}' to '{}'.", name,
				actualElement.getAttributes().get( "name" ) );
		logger.warn( "retest identified the element based on the persisted old state." );
		logger.warn( "If you apply these changes to the state {}, your test {} will break.", "", "" );
		logger.warn( "Use `By.name(\"{}\")` or `By.retestId(\"{}\")` to update your test.",
				actualElement.getAttributes().get( "name" ), oldNewMapping.getKey().getRetestId() );

		return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
	}

	private WebElement findElementByLinkText( final ByLinkText by ) {
		final String linkText = retrieveLinkText( by );
		final Mapping<Element, Element> oldNewMapping =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
						element -> (linkText.equals( element.getAttributes().get( "text" ) )
								|| linkText.equals( element.getIdentifyingAttributes().get( "text" ) )
										&& "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )) );
		final Element actualElement = oldNewMapping.getValue();
		if ( actualElement == null ) {
			throw new RuntimeException(
					"It appears that even the old state didn't have an element with link text '" + linkText + "'." );
		}

		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The link text used for element identification changed from '{}' to '{}'.", linkText,
				actualElement.getIdentifyingAttributes().get( "text" ) );
		logger.warn( "retest identified the element based on the persisted old state." );
		logger.warn( "If you apply these changes to the state {}, your test {} will break.", "", "" );
		logger.warn( "Use `By.linkText(\"{}\")` or `By.retestId(\"{}\")` to update your test.",
				actualElement.getIdentifyingAttributes().get( "text" ), oldNewMapping.getKey().getRetestId() );

		return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
	}

	private WebElement findElementByCssSelector( final ByCssSelector by ) {
		// TODO Implement: This happens at the browser (which understands xpath)
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	private WebElement findElementByXPath( final ByXPath byXPath ) {
		// TODO Implement This happens at the browser (which understands xpath)
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

}
