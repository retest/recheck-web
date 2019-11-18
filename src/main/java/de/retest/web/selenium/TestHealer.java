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

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class TestHealer {

	private static final String PATH = IdentifyingAttributes.PATH_ATTRIBUTE_KEY;
	private static final String TYPE = IdentifyingAttributes.TYPE_ATTRIBUTE_KEY;

	private static final Pattern CSS_CLASS = Pattern.compile( "^\\.([a-zA-Z0-9\\-]+)" );
	private static final Pattern CSS_ID = Pattern.compile( "^\\#([a-zA-Z0-9\\-]+)" );
	private static final Pattern CSS_TAG = Pattern.compile( "^([a-zA-Z0-9\\-]+)" );
	private static final Pattern CSS_ATTRIBUTE = Pattern.compile( "^\\[([a-zA-Z0-9\\-=\"]+)\\]" );

	private static final Logger logger = LoggerFactory.getLogger( TestHealer.class );
	private static final String ELEMENT_NOT_FOUND_MESSAGE = "It appears that even the Golden Master has no element";

	private final UnbreakableDriver wrapped;
	private final RootElement lastExpectedState;
	private final RootElement lastActualState;

	private TestHealer( final UnbreakableDriver wrapped ) {
		this.wrapped = wrapped;
		lastExpectedState = wrapped.getLastExpectedState();
		if ( lastExpectedState == null ) {
			throw new IllegalStateException( "No last expected state to find old element in!" );
		}
		lastActualState = wrapped.getLastActualState();
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
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, hasID( id ) );

		if ( actualElement == null ) {
			logger.warn( "{} with id '{}'.", ELEMENT_NOT_FOUND_MESSAGE, id );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML id attribute", id,
					actualElement.getIdentifyingAttributes().get( ID ), ID, actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByClassName( final ByClassName by ) {
		final String className = retrieveCssClassName( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, hasClass( className ) );

		if ( actualElement == null ) {
			logger.warn( "{} with CSS class '{}'.", ELEMENT_NOT_FOUND_MESSAGE, className );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML class attribute", className,
					actualElement.getIdentifyingAttributes().get( CLASS ), "className", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByName( final ByName by ) {
		final String name = retrieveName( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, hasName( name ) );

		if ( actualElement == null ) {
			logger.warn( "{} with name '{}'.", ELEMENT_NOT_FOUND_MESSAGE, name );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML name attribute", name,
					actualElement.getIdentifyingAttributes().get( NAME ), NAME, actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByLinkText( final ByLinkText by ) {
		final String linkText = retrieveLinkText( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, hasLinkText( linkText ) );

		if ( actualElement == null ) {
			logger.warn( "{} with link text '{}'.", ELEMENT_NOT_FOUND_MESSAGE, linkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "link text", linkText,
					actualElement.getIdentifyingAttributes().get( TEXT ), "linkText", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByPartialLinkText( final ByPartialLinkText by ) {
		final String partialLinkText = retrievePartialLinkText( by );
		final Element actualElement = de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
				hasPartialLinkText( partialLinkText ) );

		if ( actualElement == null ) {
			logger.warn( "{} with link text '{}'.", ELEMENT_NOT_FOUND_MESSAGE, partialLinkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "partial link text", partialLinkText,
					actualElement.getIdentifyingAttributes().get( TEXT ), "partialLinkText",
					actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByCssSelector( final ByCssSelector by ) {
		final String origSelector = ByWhisperer.retrieveCssSelector( by );
		String selector = origSelector;
		Predicate<Element> predicate = element -> true;
		boolean matched = true;
		while ( !selector.isEmpty() && matched ) {
			matched = false;
			final Matcher tagMatcher = CSS_TAG.matcher( selector );
			if ( tagMatcher.find() ) {
				final String tag = tagMatcher.group( 1 );
				predicate = predicate.and( hasTag( tag ) );
				selector = selector.substring( tag.length() ).trim();
				matched = true;
			}
			final Matcher idMatcher = CSS_ID.matcher( selector );
			if ( idMatcher.find() ) {
				final String id = idMatcher.group( 1 );
				predicate = predicate.and( hasID( id ) );
				selector = selector.substring( id.length() + 1 ).trim();
				matched = true;
			}
			final Matcher classMatcher = CSS_CLASS.matcher( selector );
			if ( classMatcher.find() ) {
				final String cssClass = classMatcher.group( 1 );
				predicate = predicate.and( hasClass( cssClass ) );
				selector = selector.substring( cssClass.length() + 1 ).trim();
				matched = true;
			}
			final Matcher attributeMatcher = CSS_ATTRIBUTE.matcher( selector );
			if ( attributeMatcher.find() ) {
				final String withoutBrackets = attributeMatcher.group( 1 );
				final String attribute = getAttribute( withoutBrackets );
				final String attributeValue = getAttributeValue( withoutBrackets );
				predicate = predicate.and( hasAttribute( attribute, attributeValue ) );
				selector = selector.substring( withoutBrackets.length() + 2 ).trim();
				matched = true;
			}
		}

		if ( !selector.isEmpty() ) {
			logger.warn(
					"Unbreakable tests are not implemented for all CSS selectors. Please report your chosen selector ('{}') at https://github.com/retest/recheck-web/issues.",
					selector );
			return null;
		}

		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, predicate );
		if ( actualElement == null ) {
			logger.warn( "{} with CSS selector '{}'.", ELEMENT_NOT_FOUND_MESSAGE, origSelector );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML class attribute", origSelector,
					actualElement.getIdentifyingAttributes().get( CLASS ), "cssSelector", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private static Predicate<Element> hasAttribute( final String attribute, final String attributeValue ) {
		return element -> element.getAttributeValue( attribute ).toString().equals( attributeValue );
	}

	private static Predicate<Element> hasLinkText( final String linkText ) {
		return element -> "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )
				&& linkText.equals( element.getAttributes().get( TEXT ) )
				|| linkText.equals( element.getIdentifyingAttributes().get( TEXT ) );
	}

	private static Predicate<Element> hasPartialLinkText( final String linkText ) {
		return element -> "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )
				&& element.getAttributeValue( TEXT ).toString().contains( linkText );
	}

	private static Predicate<Element> hasClass( final String cssClass ) {
		return element -> element.getIdentifyingAttributes().get( CLASS ) != null
				? ((String) element.getIdentifyingAttributes().get( CLASS )).contains( cssClass ) : false;
	}

	private static Predicate<Element> hasName( final String name ) {
		return element -> name.equals( element.getIdentifyingAttributes().get( NAME ) );
	}

	private static Predicate<Element> hasTag( final String tag ) {
		return element -> element.getIdentifyingAttributes().get( TYPE ).equals( tag );
	}

	private static Predicate<Element> hasID( final String id ) {
		return element -> id.equals( element.getIdentifyingAttributes().get( ID ) );
	}

	private String getAttribute( final String withoutBrackets ) {
		if ( withoutBrackets.contains( "=" ) ) {
			return withoutBrackets.substring( 0, withoutBrackets.lastIndexOf( "=" ) );
		}
		return withoutBrackets;
	}

	private String getAttributeValue( final String withoutBrackets ) {
		if ( !withoutBrackets.contains( "=" ) ) {
			return "true";
		}
		String result = withoutBrackets.substring( withoutBrackets.lastIndexOf( "=" ) + 1 );
		if ( result.contains( "\"" ) || result.contains( "'" ) ) {
			result = result.substring( 1, result.length() - 1 );
		}
		return result;
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
			writeWarnLogForChangedIdentifier( "xpath", xpathExpression,
					actualElement.getIdentifyingAttributes().get( PATH ), "xpath", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByTagName( final ByTagName by ) {
		final String tag = ByWhisperer.retrieveTag( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState, hasTag( tag ) );

		if ( actualElement == null ) {
			logger.warn( "{} with tag '{}'.", ELEMENT_NOT_FOUND_MESSAGE, tag );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML tag attribute", tag,
					actualElement.getIdentifyingAttributes().get( TYPE ), TYPE, actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private void writeWarnLogForChangedIdentifier( final String elementIdentifier, final Object oldValue,
			final Object newValue, final String byMethodName, final String retestId ) {
		logger.warn( "*************** recheck warning ***************" );
		logger.warn( "The {} used for element identification changed from '{}' to '{}'.", elementIdentifier, oldValue,
				newValue );
		logger.warn( "retest identified the element based on the persisted Golden Master." );

		String test = "";
		String callLocation = "";
		try {
			final StackTraceElement callSite = TestCaseFinder.getInstance() //
					.findTestCaseMethodInStack() //
					.getStackTraceElement();
			test = callSite.getClassName();
			callLocation = callSite.getFileName() + ":" + callSite.getLineNumber();
		} catch ( final Exception e ) {
			logger.warn( "Exception retrieving call site of findBy call." );
		}

		// TODO Get filename of state
		logger.warn( "If you apply these changes to the Golden Master {}, your test {} will break.", "", test );

		if ( newValue != null ) {
			logger.warn( "Use `By.{}(\"{}\")` or `By.retestId(\"{}\")` to update your test {}.", byMethodName, newValue,
					retestId, callLocation );
		} else {
			logger.warn( "Use `By.retestId(\"{}\")` to update your test {}.", retestId, callLocation );
		}
	}

}
