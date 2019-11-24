package de.retest.web.selenium.css;

import static de.retest.web.AttributesUtil.CLASS;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

public class Has {

	private static final String TYPE = IdentifyingAttributes.TYPE_ATTRIBUTE_KEY;
	private static final Pattern attribute = attributePattern( "" );
	private static final Pattern attributeContaining = attributePattern( "~" );
	private static final Pattern attributeStarting = attributePattern( "\\|" );
	private static final Pattern attributeBeginning = attributePattern( "\\^" );
	private static final Pattern attributeEnding = attributePattern( "\\$" );
	private static final Pattern attributeContainsSubstring = attributePattern( "\\*" );

	private static Pattern attributePattern( final String selectingChar ) {
		final String allowedCharacters = "[^" + selectingChar + "=]+";
		return Pattern.compile( "(" + allowedCharacters + ")(" + selectingChar + "=(" + allowedCharacters + "))?" );
	}

	public static Predicate<Element> attribute( final String selector ) {
		return hasAttribute( selector, attribute, String::equals );
	}

	private static Predicate<Element> hasAttribute( final String selector, final Pattern pattern,
			final BiPredicate<String, String> predicate ) {
		final Matcher matcher = pattern.matcher( selector );
		if ( matcher.matches() ) {
			final String attribute = matcher.group( 1 );
			final String attributeValue = clearQuotes( matcher.group( 3 ) );
			return hasAttributeValue( attribute, attributeValue, predicate );
		}
		return e -> false;
	}

	private static Predicate<Element> hasAttributeValue( final String attribute, final String attributeValue,
			final BiPredicate<String, String> toPredicate ) {
		return element -> null != element.getAttributeValue( attribute )
				&& toPredicate.test( element.getAttributeValue( attribute ).toString(), attributeValue );
	}

	private static String clearQuotes( final String result ) {
		if ( null == result ) {
			return "true";
		}
		if ( result.contains( "\"" ) || result.contains( "'" ) ) {
			return result.substring( 1, result.length() - 1 );
		}
		return result;
	}

	public static Predicate<Element> attributeContaining( final String selector ) {
		return hasAttribute( selector, attributeContaining, Has::containsWord );
	}

	public static boolean containsWord( final String value, final String selector ) {
		return Arrays.stream( value.split( " " ) ).anyMatch( selector::equals );
	}

	public static Predicate<Element> attributeStarting( final String selector ) {
		return hasAttribute( selector, attributeStarting, Has::startsWithWord );
	}

	public static boolean startsWithWord( final String value, final String selector ) {
		return value.equals( selector ) || value.startsWith( selector + "-" );
	}

	public static Predicate<Element> attributeBeginning( final String selector ) {
		return hasAttribute( selector, attributeBeginning, String::startsWith );
	}

	public static Predicate<Element> attributeEnding( final String selector ) {
		return hasAttribute( selector, attributeEnding, String::endsWith );
	}

	public static Predicate<Element> attributeContainingSubstring( final String selector ) {
		return hasAttribute( selector, attributeContainsSubstring, String::contains );
	}

	public static Predicate<Element> linkText( final String linkText ) {
		return element -> "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )
				&& linkText.equals( element.getAttributes().get( TEXT ) )
				|| linkText.equals( element.getIdentifyingAttributes().get( TEXT ) );
	}

	public static Predicate<Element> partialLinkText( final String linkText ) {
		return element -> "a".equalsIgnoreCase( element.getIdentifyingAttributes().getType() )
				&& element.getAttributeValue( TEXT ).toString().contains( linkText );
	}

	public static Predicate<Element> cssClass( final String cssClass ) {
		return element -> element.getIdentifyingAttributes().get( CLASS ) != null
				&& element.getIdentifyingAttributes().get( CLASS ).toString().contains( cssClass );
	}

	public static Predicate<Element> cssName( final String name ) {
		return element -> name.equals( element.getIdentifyingAttributes().get( NAME ) );
	}

	public static Predicate<Element> cssTag( final String tag ) {
		return element -> tag.equals( element.getIdentifyingAttributes().get( TYPE ) );
	}

	public static Predicate<Element> cssId( final String id ) {
		return element -> id.equals( element.getIdentifyingAttributes().get( ID ) );
	}
}
