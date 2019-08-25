package de.retest.web.adapter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RootElement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebElementListRecheckAdapter implements RecheckAdapter {

	private final WebElementRecheckAdapter delegate = new WebElementRecheckAdapter();

	@Override
	public boolean canCheck( final Object toVerify ) {
		if ( toVerify instanceof List ) {
			final List<?> list = (List<?>) toVerify;
			return onlyContainsCompatibleWebElements( list );
		}
		return false;
	}

	private boolean onlyContainsCompatibleWebElements( final List<?> list ) {
		if ( list.isEmpty() ) { // If list empty, we cannot determine generic type due to type erasure and wildcard
			return false;
		}
		return list.stream().allMatch( delegate::canCheck );
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		final List<?> elements = (List<?>) toVerify;
		return elements.stream() //
				.filter( delegate::canCheck ) // Just to be safe, although we already should have looked on all elements
				.map( delegate::convert ) //
				.flatMap( Set::stream ) //
				.collect( Collectors.toSet() );
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return delegate.getDefaultValueFinder();
	}
}
