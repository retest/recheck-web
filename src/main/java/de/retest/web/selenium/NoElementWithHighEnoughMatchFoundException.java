package de.retest.web.selenium;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.web.RecheckWebProperties;

public class NoElementWithHighEnoughMatchFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoElementWithHighEnoughMatchFoundException( final Element resultFromExpected ) {
		super( "The element " + resultFromExpected + " from the Golden Master could not be matched with confidence > "
				+ RecheckWebProperties.getInstance().elementMatchThreshold()
				+ ". To change the required minimal confidence for a match, please set property 'de.retest.recheck.elementMatchThreshold'. " );
	}
}
