package de.retest.web.selenium;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;

/**
 * {@code ByBestMatchToRetestId} will search for elements by their old retest ID (the retest ID they had in the Golden
 * Master state).
 *
 * Note that elements are assigned a retest ID when they are created (i.e. also when the <em>new</em> state is created).
 * This retest ID might differ from the retest ID the element had in the persisted (Golden Master) state. Therefore, all
 * its child elements have their respective <em>new</em> (potentially different) retest ID. Thus, the result of a call
 * to {@code ByBestMatchToRetestId} might return an element with children that have a different retest ID than they have
 * in the Golden Master.
 */
public class ByBestMatchToRetestId extends By implements Serializable {

	private static final long serialVersionUID = -3787115401615364934L;

	private static final Logger logger = LoggerFactory.getLogger( ByBestMatchToRetestId.class );

	private final String retestId;

	public ByBestMatchToRetestId( final String retestId ) {
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

	/**
	 * @param lastExpectedState
	 *            The last expected state (usually the Golden Master).
	 * @param lastActualState
	 *            The last actual state (usually the current state).
	 * @return Maybe an element whose children have a different retest ID than in the Golden Master.
	 */
	public Element findElement( final RootElement lastExpectedState, final RootElement lastActualState ) {
		final Element result = de.retest.web.selenium.By.findElement( lastExpectedState, lastActualState,
				element -> retestId.equals( element.getRetestId() ) );
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
		return retestId.equals( ((ByBestMatchToRetestId) other).retestId );
	}

	public String getRetestId() {
		return retestId;
	}
}
