package de.retest.web.selenium;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.ui.descriptors.Element;
import de.retest.ui.descriptors.RootElement;
import de.retest.util.Mapping;

public class ByRetestId extends By implements Serializable {

	private static final long serialVersionUID = -3787115401615364934L;

	private static final Logger logger = LoggerFactory.getLogger( ByRetestId.class );

	private final String retestId;

	public ByRetestId( final String retestId ) {
		this.retestId = retestId;
		if ( StringUtils.isBlank( retestId ) ) {
			throw new IllegalArgumentException( "retestId must not be empty." );
		}
	}

	@Override
	public List<WebElement> findElements( final SearchContext context ) {
		logger.info( "findElements called with {} of {}.", context, context.getClass() );
		throw new UnsupportedOperationException( "This class can only be used in conjunction with a RecheckDriver." );
	}

	public Element findElement( final RootElement lastExpectedState, final RootElement lastActualState ) {
		final Mapping<Element, Element> oldNewMapping = de.retest.web.selenium.By.findElement( lastExpectedState,
				lastActualState, element -> retestId.equals( element.getRetestId() ) );
		final Element result = oldNewMapping.getValue();
		if ( result == null ) {
			throw new NoElementWithReTestIdFoundException( retestId );
		}
		return result;
	}

	@Override
	public int hashCode() {
		return retestId.hashCode();
	}

	@Override
	public boolean equals( final Object other ) {
		if ( other == null ) {
			return false;
		}
		if ( this == other ) {
			return true;
		}
		if ( getClass() != other.getClass() ) {
			return false;
		}
		return retestId.equals( ((ByRetestId) other).retestId );
	}
}
