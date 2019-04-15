package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class ActionbasedCheckNamingStrategy implements AutocheckingCheckNamingStrategy {

	private final Multiset<String> checks = HashMultiset.create();

	@Override
	public String getUniqueCheckName( final String action, final WebElement targetElement, final Object... params ) {
		String result = action;
		// "enter_[" + normalizeAndShorten( keysToSend ) + "]_into"
		if ( "enter".equals( action ) ) {
			result = "enter_[" + normalizeAndShorten( (CharSequence[]) params ) + "]_into";
		}
		result = result + "_" + toString( targetElement );
		return makeUnique( result );
	}

	private String makeUnique( final String result ) {
		checks.add( result );
		if ( checks.count( result ) == 1 ) {
			return result;
		}
		return result + "_" + checks.count( result );
	}

	private String toString( final WebElement targetElement ) {
		String result = targetElement.toString();
		if ( result.contains( "->" ) ) {
			// remove driver info
			result = result.substring( result.lastIndexOf( "->" ) + 2 ).trim();
			// remove trailing ]
			result = result.substring( 0, result.length() - 1 );
			// remove identification criterion (e.g. id, class, ...)
			result = result.substring( result.lastIndexOf( ':' ) + 1 ).trim();
		}
		return result;
	}

	private String normalizeAndShorten( final CharSequence[] keysToSend ) {
		if ( keysToSend == null || keysToSend.length == 0 || keysToSend[0] == null || keysToSend[0].length() == 0 ) {
			// How to properly represent empty string?
			return "";
		}
		final String stringToSend = keysToSend[0].toString();
		if ( stringToSend.length() <= 10 ) {
			return stringToSend;
		}
		return stringToSend.substring( 0, 7 ) + "...";
	}

	@Override
	public void nextTest() {
		checks.clear();
	}
}
