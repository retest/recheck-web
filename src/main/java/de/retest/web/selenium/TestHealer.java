package de.retest.web.selenium;

import static de.retest.web.selenium.ByWhisperer.retrieveCssClassName;
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

import de.retest.recheck.TestCaseFinder;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;

public class TestHealer {

	private static final String PATH = "path";
	private static final String TEXT = "text";
	private static final String NAME = "name";
	private static final String CLASS = "class";
	private static final String ID = "id";

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
		throw new UnsupportedOperationException(
				"Healing tests with " + by.getClass().getSimpleName() + " not yet implemented" );
	}

	private WebElement findElementById( final ById by ) {
		final String id = retrieveId( by );
		final Element actualElement =
				de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, ID, id );

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
		final Element actualElement = de.retest.web.selenium.By.findElementByAttribute( lastExpectedState,
				lastActualState, CLASS, value -> ((String) value).contains( className ) );

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
				de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, NAME, name );

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
		final String attributeName = TEXT;
		final Element actualElement = de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
				element -> linkText.equals( element.getAttributes().get( attributeName ) )
						|| linkText.equals( element.getIdentifyingAttributes().get( attributeName ) )
								&& "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() ) );

		if ( actualElement == null ) {
			logger.warn( "{} with link text '{}'.", ELEMENT_NOT_FOUND_MESSAGE, linkText );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "link text", linkText,
					actualElement.getIdentifyingAttributes().get( TEXT ), "linkText", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private WebElement findElementByCssSelector( final ByCssSelector by ) {
		final String selector = retrieveUsableCssSelector( by );

		final Element actualElement = de.retest.web.selenium.By.findElementByAttribute( lastExpectedState,
				lastActualState, CLASS, value -> ((String) value).contains( selector ) );

		if ( actualElement == null ) {
			logger.warn( "{} with CSS selector '{}'.", ELEMENT_NOT_FOUND_MESSAGE, selector );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "HTML class attribute", selector,
					actualElement.getIdentifyingAttributes().get( CLASS ), "cssSelector", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private String retrieveUsableCssSelector( final ByCssSelector by ) {
		final String rawSelector = ByWhisperer.retrieveCssSelector( by );
		if ( rawSelector.startsWith( "#" ) ) {
			throw new IllegalArgumentException(
					"To search for element by ID, use `By.id()` instead of `#id` as CSS selector." );
		}
		if ( !rawSelector.startsWith( "." ) ) {
			throw new IllegalArgumentException(
					"To search for element by tag, use `By.tag()` instead of `tag` as CSS selector." );
		}
		// remove leading .
		final String selector = rawSelector.substring( 1 );
		if ( selector.matches( ".*[<>:+\\s\"\\[\\*].*" ) ) {
			throw new IllegalArgumentException( "For now, only simple class selector is implemented." );
		}
		return selector;
	}

	private WebElement findElementByXPath( final ByXPath byXPath ) {
		final String xpathExpression = ByWhisperer.retrieveXPath( byXPath );
		if ( xpathExpression.matches( ".*[<>:+\\s\"|'@\\*].*" ) ) {
			throw new IllegalArgumentException( "For now, only simple class selector is implemented." );
		}

		final Element actualElement = findMatchingElement( xpathExpression );

		if ( actualElement == null ) {
			logger.warn( "{} with XPath '{}'.", ELEMENT_NOT_FOUND_MESSAGE, xpathExpression );
			return null;
		} else {
			writeWarnLogForChangedIdentifier( "xpath", xpathExpression,
					actualElement.getIdentifyingAttributes().get( PATH ), "xpath", actualElement.getRetestId() );
			return wrapped.findElement( By.xpath( actualElement.getIdentifyingAttributes().getPath() ) );
		}
	}

	private Element findMatchingElement( final String xpathExpression ) {
		if ( xpathExpression.startsWith( "//" ) ) {
			return de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, PATH,
					value -> ((Path) value).toString().toLowerCase()
							.contains( xpathExpression.substring( 1 ).toLowerCase() ) );
		}
		return de.retest.web.selenium.By.findElementByAttribute( lastExpectedState, lastActualState, PATH,
				value -> ((Path) value).toString().toLowerCase()
						.startsWith( xpathExpression.substring( 1 ).toLowerCase() ) );
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
		addWarning( elementIdentifier, testClassName, retestId );
	}

	private void addWarning( final String elementIdentifier, final String testClassName, final String retestId ) {
		final ElementIdentificationWarning warning =
				new ElementIdentificationWarning( elementIdentifier, testClassName, retestId );
		final StateDifference stateDifference = lastActionReplayResult.getStateDifference();
		for ( final ElementDifference elementDifference : stateDifference.getElementDifferences() ) {
			if ( elementDifference.getRetestId().equals( retestId ) ) {
				final Collection<ElementDifference> childDifferences = elementDifference.getChildDifferences();
				if ( elementDifference.hasIdentAttributesDifferences() ) {
					final IdentifyingAttributesDifference identAttributesDifference =
							(IdentifyingAttributesDifference) elementDifference.getIdentifyingAttributesDifference();
					setElementIdentificationWarning( identAttributesDifference, elementIdentifier, warning );
					searchInChildDifferences( childDifferences, elementIdentifier, retestId, warning );
				}
			}
		}
	}

	private void searchInChildDifferences( final Collection<ElementDifference> childDifferences,
			final String elementIdentifier, final String retestId, final ElementIdentificationWarning warning ) {
		for ( final ElementDifference childElement : childDifferences ) {
			if ( childElement.getRetestId().equals( retestId ) ) {
				final IdentifyingAttributesDifference identAttributesDifference =
						(IdentifyingAttributesDifference) childElement.getIdentifyingAttributesDifference();
				final boolean empty = childElement.getChildDifferences().isEmpty();
				if ( empty ) {
					setElementIdentificationWarning( identAttributesDifference, elementIdentifier, warning );
				}
				searchInChildDifferences( childElement.getChildDifferences(), elementIdentifier, retestId, warning );
			}
		}
	}

	private void setElementIdentificationWarning( final IdentifyingAttributesDifference attributesDifference,
			final String elementIdentifier, final ElementIdentificationWarning warning ) {
		for ( final AttributeDifference attributeDifference : attributesDifference.getAttributeDifferences() ) {
			if ( attributeDifference.getKey().equals( elementIdentifier ) ) {
				attributeDifference.setElementIdentificationWarning( warning );
			}
		}
	}
}
