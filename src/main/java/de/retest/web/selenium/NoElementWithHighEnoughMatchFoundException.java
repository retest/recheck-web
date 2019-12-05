package de.retest.web.selenium;

import de.retest.recheck.RecheckProperties;
import de.retest.recheck.ui.descriptors.Element;

public class NoElementWithHighEnoughMatchFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoElementWithHighEnoughMatchFoundException( final Element resultFromExpected ) {
		super( "The element " + resultFromExpected + " from the Golden Master could not be matched with confidence > "
				+ RecheckProperties.getInstance().elementMatchThreshold()
				+ ". To change the required minimal confidence for a match, please set property '"
				+ RecheckProperties.ELEMENT_MATCH_THRESHOLD_PROPERTY_KEY + "'." );
	}
}
