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

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;

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
		final Element actualElement =
				de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, "id", id );

		if ( actualElement == null ) {
			logger.warn( "It appears that even the old state didn't have an element with id '{}'.", id );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML id attribute", id,
					actualElement.getIdentifyingAttributes().get( "id" ), "id", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByClassName( final ByClassName by ) {
		final String className = retrieveCSSClassName( by );
		final Element actualElement = de.retest.web.selenium.By.findElementByAttribute( lastExpectedState,
				lastActualState, "class", value -> ((String) value).contains( className ) );

		if ( actualElement == null ) {
			logger.warn( "It appears that even the old state didn't have an element with CSS class '{}'.", className );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML class attribute", className,
					actualElement.getIdentifyingAttributes().get( "class" ), "className", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByName( final ByName by ) {
		final String name = retrieveName( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, "name", name );

		if ( actualElement == null ) {
			logger.warn( "It appears that even the old state didn't have an element with name '{}'.", name );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML name attribute", name, actualElement.getAttributes().get( "name" ),
					"name", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByLinkText( final ByLinkText by ) {
		final String linkText = retrieveLinkText( by );
		final String attributeName = "text";
		final Element actualElement = de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
				element -> linkText.equals( element.getAttributes().get( attributeName ) )
						|| linkText.equals( element.getIdentifyingAttributes().get( attributeName ) )
								&& "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() ) );

		if ( actualElement == null ) {
			logger.warn( "It appears that even the old state didn't have an element with link text '{}'.", linkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "link text", linkText,
					actualElement.getIdentifyingAttributes().get( "text" ), "linkText", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByCssSelector( final ByCssSelector by ) {
		// TODO Implement: This happens at the browser (which understands xpath)
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	private WebElement findElementByXPath( final ByXPath byXPath ) {
		// TODO Implement This happens at the browser (which understands xpath)
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	private void writeWarnLogForChangedIdentifier( final String elementIdentifier, final Object oldValue,
			final Object newValue, final String byMethodName, final String retestId ) {
		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The {} used for element identification changed from '{}' to '{}'.", elementIdentifier, oldValue,
				newValue );
		logger.warn( "retest identified the element based on the persisted old state." );
		// TODO Get filename from state
		// TODO Guess test name from state name
		logger.warn( "If you apply these changes to the state {}, your test {} will break.", "", "" );
		// TODO Eventually, we want something like Test.java:123 instead of "your test"
		if ( newValue != null ) {
			logger.warn( "Use `By.{}(\"{}\")` or `By.retestId(\"{}\")` to update your test.", byMethodName, newValue,
					retestId );
		} else {
			logger.warn( "Use `By.retestId(\"{}\")` to update your test.", retestId );
		}
	}

}
