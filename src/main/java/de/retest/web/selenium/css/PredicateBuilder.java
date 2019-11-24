package de.retest.web.selenium.css;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.ui.descriptors.Element;

public class PredicateBuilder {

	private static final Logger logger = LoggerFactory.getLogger( PredicateBuilder.class );

	private final List<Transformer> selectors;
	private final List<Predicate<Element>> predicates;
	private final String origSelector;

	public PredicateBuilder( final List<Transformer> selectors, final String origSelector ) {
		this.selectors = selectors;
		this.origSelector = origSelector;
		predicates = new LinkedList<>();
	}

	public Optional<Predicate<Element>> build() {
		final String remainingSelector = parse( origSelector );

		if ( isPartAvailable( remainingSelector ) ) {
			logUnkownSelector( remainingSelector );
			return Optional.empty();

		}
		return Optional.of( combinePredicates() );
	}

	private String parse( final String selector ) {
		String remainingSelector = selector;
		boolean matched = true;
		while ( isPartAvailable( remainingSelector ) && matched ) {
			matched = false;
			for ( final Transformer function : selectors ) {
				final Selector cssSelector = function.transform( remainingSelector );
				if ( cssSelector.matches() ) {
					predicates.add( cssSelector.predicate() );
					remainingSelector = cssSelector.remainingSelector();
					matched = cssSelector.matches();
				}
			}
		}
		return remainingSelector;
	}

	private boolean isPartAvailable( final String selector ) {
		return !selector.isEmpty();
	}

	private void logUnkownSelector( final String selector ) {
		logger.warn(
				"Unbreakable tests are not implemented for all CSS selectors. Please report your chosen selector ('{}') at https://github.com/retest/recheck-web/issues.",
				selector );
	}

	private Predicate<Element> combinePredicates() {
		return predicates.stream().reduce( Predicate::and ).orElse( e -> true );
	}

}
