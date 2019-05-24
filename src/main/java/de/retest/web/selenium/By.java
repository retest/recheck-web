package de.retest.web.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.Alignment;

public abstract class By extends org.openqa.selenium.By {

	public static ByBestMatchToRetestId retestId( final String retestId ) {
		return new ByBestMatchToRetestId( retestId );
	}

	public static Element findElement( final RootElement lastExpectedState, final RootElement lastActualState,
			final Predicate<Element> predicate ) {
		if ( lastExpectedState == null ) {
			throw new IllegalArgumentException( "Cannot find element in null state." );
		}
		final Element resultFromExpected = findElement( lastExpectedState.getContainedElements(), predicate );
		if ( resultFromExpected == null ) {
			return null;
		}
		final Alignment alignment = Alignment.createAlignment( lastExpectedState, lastActualState );
		final Element resultFromActual = alignment.get( resultFromExpected );
		if ( resultFromActual == null ) {
			throw new NoElementWithHighEnoughMatchFoundException( resultFromExpected );
		}
		return resultFromActual;
	}

	private static Element findElement( final List<Element> children, final Predicate<Element> predicate ) {
		for ( final Element element : children ) {
			if ( predicate.test( element ) ) {
				return element;
			}
			final Element result = findElement( element.getContainedElements(), predicate );
			if ( result != null ) {
				return result;
			}
		}
		return null;
	}

	public static List<Element> findElements( final List<Element> children, final Predicate<Element> predicate ) {
		final List<Element> result = new ArrayList<>();
		for ( final Element element : children ) {
			if ( predicate.test( element ) ) {
				result.add( element );
			}
			result.addAll( findElements( element.getContainedElements(), predicate ) );
		}
		return result;
	}

	public static Element findElementByAttribute( final RootElement lastExpectedState,
			final RootElement lastActualState, final String attributeName, final Predicate<Object> condition ) {
		return findElement( lastExpectedState, lastActualState, element -> {
			if ( element.getIdentifyingAttributes().get( attributeName ) != null ) {
				return condition.test( element.getIdentifyingAttributes().getAttribute( attributeName ).getValue() );
			} else if ( element.getAttributes().get( attributeName ) != null ) {
				return condition.test( element.getAttributes().get( attributeName ) );
			} else {
				return false;
			}
		} );
	}

	public static Element findElementByAttribute( final RootElement lastExpectedState,
			final RootElement lastActualState, final String attributeName, final Object attributeValue ) {
		return findElementByAttribute( lastExpectedState, lastActualState, attributeName, attributeValue::equals );
	}

}
