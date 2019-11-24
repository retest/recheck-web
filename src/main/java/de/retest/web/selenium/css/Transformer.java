package de.retest.web.selenium.css;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retest.recheck.ui.descriptors.Element;

class Transformer {

	private final Pattern cssPattern;
	private final Function<String, Predicate<Element>> factory;

	public Transformer( final Pattern cssPattern, final Function<String, Predicate<Element>> factory ) {
		this.cssPattern = cssPattern;
		this.factory = factory;
	}

	public Selector transform( final String selector ) {
		final Matcher attributeMatcher = cssPattern.matcher( selector );
		if ( attributeMatcher.find() ) {
			return new RegexSelector( attributeMatcher, factory );
		}
		return new EmptySelector( selector );
	}

}