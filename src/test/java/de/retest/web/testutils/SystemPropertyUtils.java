package de.retest.web.testutils;

import static de.retest.recheck.Properties.VALUES_SEPARATOR;
import static de.retest.recheck.elementcollection.RecheckIgnore.IGNORED_ATTRIBUTES_PROPERTY;
import static de.retest.recheck.ui.descriptors.OutlineAttribute.ABSOLUTE_OUTLINE;

public class SystemPropertyUtils {

	private SystemPropertyUtils() {
	}

	public static void ignoreAbsoluteOutline() {
		String ignored = System.getProperty( IGNORED_ATTRIBUTES_PROPERTY, "" );
		if ( !ignored.contains( ABSOLUTE_OUTLINE ) ) {
			ignored += VALUES_SEPARATOR + ABSOLUTE_OUTLINE;
			System.setProperty( IGNORED_ATTRIBUTES_PROPERTY, ignored );
		}
	}
}
