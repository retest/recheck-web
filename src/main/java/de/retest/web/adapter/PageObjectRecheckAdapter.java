package de.retest.web.adapter;

import java.util.Set;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.util.PageObjects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageObjectRecheckAdapter implements RecheckAdapter {

	private final WebElementListRecheckAdapter delegate = new WebElementListRecheckAdapter();

	@Override
	public boolean canCheck( final Object toVerify ) {
		return toVerify.getClass().isAnnotationPresent( PageObject.class );
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		return delegate.convert( PageObjects.convertToList( toVerify ) );
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return delegate.getDefaultValueFinder();
	}
}
