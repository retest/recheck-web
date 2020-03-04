package de.retest.web.selenium;

import static de.retest.web.AttributesUtil.CLASS;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;
import static de.retest.web.selenium.ByWhisperer.retrieveCssClassName;
import static de.retest.web.selenium.ByWhisperer.retrieveId;
import static de.retest.web.selenium.ByWhisperer.retrieveLinkText;
import static de.retest.web.selenium.ByWhisperer.retrieveName;
import static de.retest.web.selenium.ByWhisperer.retrievePartialLinkText;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.TestCaseFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.web.selenium.css.DefaultSelectors;
import de.retest.web.selenium.css.Has;
import de.retest.web.selenium.css.PredicateBuilder;

public class TestHealer {

	private static final String PATH = IdentifyingAttributes.PATH_ATTRIBUTE_KEY;
	private static final String TYPE = IdentifyingAttributes.TYPE_ATTRIBUTE_KEY;

	private static final Logger logger = LoggerFactory.getLogger( TestHealer.class );
	private static final String ELEMENT_NOT_FOUND_MESSAGE = "It appears that even the Golden Master has no element";

	private final UnbreakableDriver wrapped;
	private final RootElement lastExpectedState;
	private final RootElement lastActualState;
	private final Consumer<QualifiedElementWarning> warningConsumer;

	private TestHealer( final UnbreakableDriver wrapped ) {
		this.wrapped = wrapped;
		lastExpectedState = wrapped.getLastExpectedState();
		if ( lastExpectedState == null ) {
			throw new IllegalStateException( "No last expected state to find old element in!" );
		}
		lastActualState = wrapped.getLastActualState();
		warningConsumer = wrapped.getWarningConsumer();
	}

	public static WebElement findElement( final By by, final UnbreakableDriver wrapped ) {
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
		if ( by instanceof ByTagName ) {
			return findElementByTagName( (ByTagName) by );
		}
		if ( by instanceof ByPartialLinkText ) {
			return findElementByPartialLinkText( (ByPartialLinkText) by );
		}
		throw new UnsupportedOperationException(
				"Healing tests with " + by.getClass().getSimpleName() + " not yet implemented" );
	}

	private WebElement findElementById( final ById by ) {
		final String id = retrieveId( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, Has.cssId( id ) );

		if ( actualElement == null ) {
			logger.warn( "{} with id '{}'.", ELEMENT_NOT_FOUND_MESSAGE, id );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( ID, id, actualElement.getIdentifyingAttributes().get( ID ), ID,
					actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByClassName( final ByClassName by ) {
		final String className = retrieveCssClassName( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, Has.cssClass( className ) );

		if ( actualElement == null ) {
			logger.warn( "{} with CSS class '{}'.", ELEMENT_NOT_FOUND_MESSAGE, className );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( CLASS, className, actualElement.getIdentifyingAttributes().get( CLASS ),
					"className", actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByName( final ByName by ) {
		final String name = retrieveName( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, Has.cssName( name ) );

		if ( actualElement == null ) {
			logger.warn( "{} with name '{}'.", ELEMENT_NOT_FOUND_MESSAGE, name );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( NAME, name, actualElement.getIdentifyingAttributes().get( NAME ), NAME,
					actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByLinkText( final ByLinkText by ) {
		final String linkText = retrieveLinkText( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, Has.linkText( linkText ) );

		if ( actualElement == null ) {
			logger.warn( "{} with link text '{}'.", ELEMENT_NOT_FOUND_MESSAGE, linkText );
			return null;
		} else if ( "true".equals( actualElement.getAttributeValue( "pseudo" ) ) ) {
			logger.warn( "{} with link text '{}' is a pseudo element", ELEMENT_NOT_FOUND_MESSAGE, linkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( TEXT, linkText, actualElement.getIdentifyingAttributes().get( TEXT ),
					"linkText", actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByPartialLinkText( final ByPartialLinkText by ) {
		final String partialLinkText = retrievePartialLinkText( by );
		final Element actualElement = de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
				Has.partialLinkText( partialLinkText ) );

		if ( actualElement == null ) {
			logger.warn( "{} with link text '{}'.", ELEMENT_NOT_FOUND_MESSAGE, partialLinkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "partial link text", partialLinkText,
					actualElement.getIdentifyingAttributes().get( TEXT ), "partialLinkText", actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByCssSelector( final ByCssSelector by ) {
		final String origSelector = ByWhisperer.retrieveCssSelector( by );
		final Optional<Predicate<Element>> predicate =
				new PredicateBuilder( DefaultSelectors.all(), origSelector ).build();

		return predicate.map( p -> toWebElement( origSelector, p ) ).orElse( null );
	}

	private WebElement toWebElement( final String origSelector, final Predicate<Element> predicate ) {
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, predicate );
		if ( actualElement == null ) {
			logger.warn( "{} with CSS selector '{}'.", ELEMENT_NOT_FOUND_MESSAGE, origSelector );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( CLASS, origSelector,
					actualElement.getIdentifyingAttributes().get( CLASS ), "cssSelector", actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	protected static boolean isNotYetSupportedXPathExpression( final String xpathExpression ) {
		return xpathExpression.matches( ".*[<>:+\\s\"|'@\\*].*" );
	}

	private WebElement findElementByXPath( final ByXPath byXPath ) {
		final String xpathExpression = ByWhisperer.retrieveXPath( byXPath );
		if ( isNotYetSupportedXPathExpression( xpathExpression ) ) {
			logger.warn(
					"Unbreakable tests are not implemented for all XPath selectors. Please report your chosen selector ('{}') at https://github.com/retest/recheck-web/issues.",
					xpathExpression );
			return null;
		}

		Predicate<Element> predicate = element -> true;
		if ( xpathExpression.startsWith( "//" ) ) {
			predicate = predicate.and( element -> element.getIdentifyingAttributes().getPath().toLowerCase()
					.contains( xpathExpression.substring( 1 ).toLowerCase() ) );
		} else if ( xpathExpression.startsWith( "/" ) ) {
			predicate = predicate.and( element -> element.getIdentifyingAttributes().getPath().toLowerCase()
					.startsWith( xpathExpression.substring( 1 ).toLowerCase() ) );
		}

		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, predicate );
		if ( actualElement == null ) {
			logger.warn( "{} with XPath '{}'.", ELEMENT_NOT_FOUND_MESSAGE, xpathExpression );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( PATH, xpathExpression,
					actualElement.getIdentifyingAttributes().get( PATH ), "xpath", actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByTagName( final ByTagName by ) {
		final String tag = ByWhisperer.retrieveTag( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, Has.cssTag( tag ) );

		if ( actualElement == null ) {
			logger.warn( "{} with tag '{}'.", ELEMENT_NOT_FOUND_MESSAGE, tag );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( TYPE, tag, actualElement.getIdentifyingAttributes().get( TYPE ), TYPE,
					actualElement );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private void writeWarnLogForChangedIdentifier( final String elementIdentifier, final Object oldValue,
			final Object newValue, final String byMethodName, final Element actualElement ) {
		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The {} used for element identification changed from '{}' to '{}'.",
				makeHumanReadable( elementIdentifier ), oldValue, newValue );
		logger.warn( "retest identified the element based on the persisted Golden Master." );

		String test = "";
		String callSiteFileName = "";
		Integer callSiteLineNumber = -1;
		try {
			final StackTraceElement callSite = TestCaseFinder.getInstance() //
					.findTestCaseMethodInStack() //
					.getStackTraceElement();
			test = callSite.getClassName();
			callSiteFileName = callSite.getFileName();
			callSiteLineNumber = callSite.getLineNumber();
		} catch ( final Exception e ) {
			logger.warn( "Exception retrieving call site of findBy call." );
		}

		// TODO Get filename of state
		logger.warn( "If you apply these changes to the Golden Master {}, your test {} will break.", "", test );

		if ( newValue != null ) {
			logger.warn( "Use `By.{}(\"{}\")` or `By.retestId(\"{}\")` to update your test {}:{}.", byMethodName,
					newValue, actualElement.getRetestId(), callSiteFileName, callSiteLineNumber );
		} else {
			logger.warn( "Use `By.retestId(\"{}\")` to update your test {}:{}.", actualElement.getRetestId(),
					callSiteFileName, callSiteLineNumber );
		}
		if ( warningConsumer != null ) {
			warningConsumer.accept( new QualifiedElementWarning( actualElement.getRetestId(), elementIdentifier,
					new ElementIdentificationWarning( callSiteFileName, callSiteLineNumber, byMethodName, test ) ) );
		}
	}

	private String makeHumanReadable( final String elementIdentifier ) {
		if ( elementIdentifier.equals( TYPE ) ) {
			return "HTML tag attribute";
		}
		if ( elementIdentifier.equals( TEXT ) ) {
			return "link text";
		}
		return "HTML " + elementIdentifier + " attribute";
	}

}
