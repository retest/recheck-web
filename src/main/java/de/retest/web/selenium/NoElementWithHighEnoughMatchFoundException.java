package de.retest.web.selenium;

import de.retest.recheck.Properties;
import de.retest.recheck.ui.descriptors.Element;

public class NoElementWithHighEnoughMatchFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoElementWithHighEnoughMatchFoundException( final Element resultFromExpected ) {
		super( "The element " + resultFromExpected + " from the Golden Master could not be matched with confidence > "
				+ System.getProperty( Properties.ELEMENT_MATCH_THRESHOLD_PROPERTY )
				+ ". To change the required minimal confidence for a match, please set property "
				+ Properties.ELEMENT_MATCH_THRESHOLD_PROPERTY );
	}
}
