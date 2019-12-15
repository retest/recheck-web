package de.retest.web.selenium.css;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.retest.recheck.ui.descriptors.Element;

class RegexTransformer implements Transformer {

	private static final String REMAINING_GROUP = "remaining";
	private static final String REMAINING = "(?<" + REMAINING_GROUP + ">.*)$";
	private static final String START_OF_LINE = "^";

	private final Pattern cssPattern;
	private final PredicateFactory factory;

	RegexTransformer( final String pattern, final PredicateFactory factory ) {
		cssPattern = Pattern.compile( START_OF_LINE + pattern + REMAINING );
		this.factory = factory;
	}

	@Override
	public Selector transform( final String selector ) {
		final Matcher matcher = cssPattern.matcher( selector );
		if ( matcher.find() ) {
			return newSelector( matcher );
		}
		return Selector.unsupported( selector );
	}

	private Selector newSelector( final Matcher matcher ) {
		final String cssAttribute = matcher.group( 1 );
		final String remainingSelector = matcher.group( REMAINING_GROUP ).trim();
		final Predicate<Element> predicate = factory.create( cssAttribute );
		return Selector.supported( remainingSelector, predicate );
	}

}
