package de.retest.web.selenium.css;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;

import de.retest.recheck.ui.descriptors.Element;

class RegexSelector implements Selector {

	private final Matcher classMatcher;
	private final Function<String, Predicate<Element>> factory;

	RegexSelector( final Matcher classMatcher, final Function<String, Predicate<Element>> factory ) {
		this.classMatcher = classMatcher;
		this.factory = factory;
	}

	@Override
	public Predicate<Element> predicate() {
		final String cssAttribute = classMatcher.group( 1 );
		return factory.apply( cssAttribute );
	}

	@Override
	public String remainingSelector() {
		return classMatcher.group( 2 ).trim();
	}

	@Override
	public boolean matches() {
		return true;
	}

}
