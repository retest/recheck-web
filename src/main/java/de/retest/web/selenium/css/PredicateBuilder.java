package de.retest.web.selenium.css;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PredicateBuilder {

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
		String oldSelector = "";
		String remainingSelector = selector;
		while ( isPartAvailable( remainingSelector ) && !oldSelector.equals( remainingSelector ) ) {
			oldSelector = remainingSelector;
			remainingSelector = transform( remainingSelector );
		}
		return remainingSelector;
	}

	private String transform( final String selector ) {
		String remainingSelector = selector;
		for ( final Transformer function : selectors ) {
			final Selector cssSelector = function.transform( remainingSelector );
			remainingSelector = cssSelector.getRemainingSelector();
			if ( !selector.equals( remainingSelector ) ) {
				predicates.add( cssSelector.getPredicate() );
				return remainingSelector;
			}
		}
		return remainingSelector;
	}

	private boolean isPartAvailable( final String selector ) {
		return !selector.isEmpty();
	}

	private void logUnkownSelector( final String selector ) {
		log.warn(
				"Unbreakable tests are not implemented for all CSS selectors. Please report your chosen selector ('{}') at https://github.com/retest/recheck-web/issues.",
				selector );
	}

	private Predicate<Element> combinePredicates() {
		return predicates.stream().reduce( e -> true, Predicate::and );
	}

}
