package de.retest.web;

import static de.retest.web.AttributesConfig.ABSOLUTE_HEIGHT;
import static de.retest.web.AttributesConfig.ABSOLUTE_WIDTH;
import static de.retest.web.AttributesConfig.ABSOLUTE_X;
import static de.retest.web.AttributesConfig.ABSOLUTE_Y;
import static de.retest.web.AttributesConfig.HEIGHT;
import static de.retest.web.AttributesConfig.TAG_NAME;
import static de.retest.web.AttributesConfig.TEXT;
import static de.retest.web.AttributesConfig.WIDTH;
import static de.retest.web.AttributesConfig.X;
import static de.retest.web.AttributesConfig.Y;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AttributesUtil {

	private static final Set<String> identifyingAttributes = new HashSet<>( Arrays.asList( ABSOLUTE_X, ABSOLUTE_Y,
			ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT, X, Y, WIDTH, HEIGHT, TAG_NAME, TEXT ) );

	private AttributesUtil() {}

	public static boolean isIdentifyingAttribute( final String key ) {
		return identifyingAttributes.contains( key );
	}

	public static boolean isStateAttribute( final String key ) {
		return key != null && !isIdentifyingAttribute( key );
	}

}
