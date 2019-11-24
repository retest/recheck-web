package de.retest.web.selenium.css;

import static de.retest.web.AttributesUtil.CLASS;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

public class Has {

	private static final String TYPE = IdentifyingAttributes.TYPE_ATTRIBUTE_KEY;

	public static Predicate<Element> cssAttribute( final String withoutBrackets ) {
		final String attribute = getAttribute( withoutBrackets );
		final String attributeValue = getAttributeValue( withoutBrackets );
		return hasAttribute( attribute, attributeValue );
	}

	private static Predicate<Element> hasAttribute( final String attribute, final String attributeValue ) {
		return element -> element.getAttributeValue( attribute ).toString().equals( attributeValue );
	}

	private static String getAttribute( final String withoutBrackets ) {
		if ( withoutBrackets.contains( "=" ) ) {
			return withoutBrackets.substring( 0, withoutBrackets.lastIndexOf( "=" ) );
		}
		return withoutBrackets;
	}

	private static String getAttributeValue( final String withoutBrackets ) {
		if ( !withoutBrackets.contains( "=" ) ) {
			return "true";
		}
		String result = withoutBrackets.substring( withoutBrackets.lastIndexOf( "=" ) + 1 );
		if ( result.contains( "\"" ) || result.contains( "'" ) ) {
			result = result.substring( 1, result.length() - 1 );
		}
		return result;
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
				? ((String) element.getIdentifyingAttributes().get( CLASS )).contains( cssClass ) : false;
	}

	public static Predicate<Element> cssName( final String name ) {
		return element -> name.equals( element.getIdentifyingAttributes().get( NAME ) );
	}

	public static Predicate<Element> cssTag( final String tag ) {
		return element -> element.getIdentifyingAttributes().get( TYPE ).equals( tag );
	}

	public static Predicate<Element> cssId( final String id ) {
		return element -> id.equals( element.getIdentifyingAttributes().get( ID ) );
	}
}
