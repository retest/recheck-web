package de.retest.web;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AttributesUtil {

	public static final String ABSOLUTE_X = "absolute-x";
	public static final String ABSOLUTE_Y = "absolute-y";
	public static final String ABSOLUTE_WIDTH = "absolute-width";
	public static final String ABSOLUTE_HEIGHT = "absolute-height";

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	public static final String TEXT = "text";

	public static final String CLASS = "class";
	public static final String ID = "id";
	public static final String NAME = "name";

	// Mapped to our "type" attribute in WebElementPeer to avoid conflicts with HTML "type" attribute.
	public static final String TAG_NAME = "tagName";

	// Keys used in getAllElementsByPath.js.
	private static final Set<String> identifyingAttributes = new HashSet<>( Arrays.asList( ABSOLUTE_X, ABSOLUTE_Y,
			ABSOLUTE_WIDTH, ABSOLUTE_HEIGHT, X, Y, WIDTH, HEIGHT, TEXT, CLASS, ID, NAME, TAG_NAME ) );

	private AttributesUtil() {}

	public static boolean isIdentifyingAttribute( final String key ) {
		return identifyingAttributes.contains( key );
	}

	public static boolean isStateAttribute( final String key, final AttributesProvider provider ) {
		final boolean isNotIdentifyingAttribute = !isIdentifyingAttribute( key );
		final boolean isHtmlAttribute = isHtmlAttribute( key, provider );
		final boolean isCssAttribute = isCssAttribute( key, provider );
		return isNotIdentifyingAttribute && (isHtmlAttribute || isCssAttribute);
	}

	private static boolean isHtmlAttribute( final String key, final AttributesProvider provider ) {
		return provider.allHtmlAttributes() //
				? true //
				: provider.getHtmlAttributes().contains( key );
	}

	private static boolean isCssAttribute( final String key, final AttributesProvider provider ) {
		return provider.getCssAttributes().contains( key );
	}

}
