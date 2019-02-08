package de.retest.web.util;

import static de.retest.web.AttributesConfig.TEXT;

import de.retest.ui.descriptors.TextAttribute;

public class TextAttributeUtil {

	public static final String PRE_ELEMENT = "pre";

	public static TextAttribute createTextAttribute( final String path, final String text ) {
		return path.toLowerCase().contains( PRE_ELEMENT ) ? new TextAttribute( TEXT, text )
				: new TextAttribute( TEXT, cleanString( text ) );
	}

	private static String cleanString( final String text ) {
		return text.replaceAll( "\\s+", " " ).trim();
	}

}
