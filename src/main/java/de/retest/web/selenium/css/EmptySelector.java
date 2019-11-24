package de.retest.web.selenium.css;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;

class EmptySelector implements Selector {

	private final String selector;

	EmptySelector( final String selector ) {
		this.selector = selector;
	}

	@Override
	public boolean matches() {
		return false;
	}

	@Override
	public Predicate<Element> predicate() {
		return e -> false;
	}

	@Override
	public String remainingSelector() {
		return selector;
	}

}
