package de.retest.web.util;

import de.retest.ui.descriptors.TextAttribute;
import de.retest.web.AttributesConfig;

public class TextAttributeUtil {

	public static TextAttribute createTextAttribute( final String text ) {
		return new TextAttribute( AttributesConfig.TEXT, cleanString( text ) );
	}

	private static String cleanString( final String text ) {
		return text.replaceAll( "\\s+", " " );
	}

}
