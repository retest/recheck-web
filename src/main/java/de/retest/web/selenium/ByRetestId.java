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
import de.retest.ui.diff.Alignment;

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
		if ( lastExpectedState == null ) {
			throw new IllegalArgumentException( "Cannot find element in null state." );
		}
		final Element result = findElement( lastExpectedState.getContainedElements() );
		if ( result == null ) {
			throw new RuntimeException( "No element with retest id '" + retestId + "' found!" );
		}
		// TODO Use unobfuscated methods
		final Alignment alignment = Alignment.a( lastExpectedState, lastActualState );
		return alignment.a( result );
	}

	public Element findElement( final List<Element> children ) {
		for ( final Element element : children ) {
			if ( retestId.equals( element.getRetestId() ) ) {
				return element;
			}
			final Element result = findElement( element.getContainedElements() );
			if ( result != null ) {
				return result;
			}
		}
		return null;
	}
}
