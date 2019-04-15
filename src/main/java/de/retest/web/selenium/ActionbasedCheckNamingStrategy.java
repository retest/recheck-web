package de.retest.web.selenium;

import org.openqa.selenium.WebElement;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * This class aims to create semantic names for checks based on the previously executed action that led to that state.
 * This looks like "enter_[my text]_into_user" or "click_contact". If a name occurs multiple times, the second and
 * following occasions are labeled with "_2", "_3" etc. The drawback of this naming strategy is, that if e.g. the target
 * changes, the state will not be recognized anymore. So it is recommended to use this naming strategy _only_ in
 * combination with the <code>retestId</code>.
 */
public class ActionbasedCheckNamingStrategy implements AutocheckingCheckNamingStrategy {

	private final Multiset<String> checks = HashMultiset.create();

	@Override
	public String getUniqueCheckName( final String action, final WebElement targetElement, final Object... params ) {
		String result = action;
		// "enter_[" + normalizeAndShorten( keysToSend ) + "]_into"
		if ( "enter".equals( action ) ) {
			// TODO Call FileUtils.normalize
			result = "enter_[" + shortenTextInput( (CharSequence[]) params ) + "]_into";
		}
		if ( "get".equals( action ) ) {
			// TODO Call FileUtils.normalize
			result = "get_[" + shortenUrl( params[0] ) + "]";
		}
		if ( targetElement != null ) {
			result = result + "_" + toString( targetElement );
		}
		return makeUnique( result );
	}

	protected String makeUnique( final String result ) {
		checks.add( result );
		if ( checks.count( result ) == 1 ) {
			return result;
		}
		return result + "_" + checks.count( result );
	}

	protected String toString( final WebElement targetElement ) {
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

	protected String shortenTextInput( final CharSequence[] keysToSend ) {
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

	protected String shortenUrl( final Object url ) {
		if ( url == null ) {
			return "";
		}
		return url.toString().replace( "http://", "" ).replace( "https://", "" );
	}

	@Override
	public void nextTest() {
		checks.clear();
	}
}
