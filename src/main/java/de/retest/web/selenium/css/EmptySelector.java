package de.retest.web.selenium.css;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;

public class EmptySelector implements Selector {

	private final String selector;

	public EmptySelector( final String selector ) {
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
