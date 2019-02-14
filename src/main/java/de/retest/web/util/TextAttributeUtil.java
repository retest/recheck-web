package de.retest.web.util;

import static de.retest.web.AttributesConfig.TEXT;

import de.retest.recheck.ui.descriptors.TextAttribute;

public class TextAttributeUtil {

	private TextAttributeUtil() {}

	public static final String PRE_ELEMENT = "/pre[";

	public static TextAttribute createTextAttribute( final String path, final String text ) {
		return isPreContained( path ) ? new TextAttribute( TEXT, text )
				: new TextAttribute( TEXT, removeFormatting( text ) );
	}

	private static boolean isPreContained( final String path ) {
		return path.toLowerCase().contains( PRE_ELEMENT );
	}

	private static String removeFormatting( final String text ) {
		return text.replaceAll( "\\s+", " " ).trim();
	}

}
