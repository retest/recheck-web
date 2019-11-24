package de.retest.web.selenium.css;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.web.selenium.TestHealer;

public class PredicateBuilder {

	private static final Logger logger = LoggerFactory.getLogger( TestHealer.class );

	private final List<Transformer> selectors;
	private final List<Predicate<Element>> predicates;
	private final String origSelector;

	public PredicateBuilder( final List<Transformer> selectors, final String origSelector ) {
		this.selectors = selectors;
		this.origSelector = origSelector;
		predicates = new LinkedList<>();
	}

	public Optional<Predicate<Element>> build() {
		String selector = origSelector;
		boolean matched = true;
		while ( !selector.isEmpty() && matched ) {
			matched = false;
			for ( final Transformer function : selectors ) {
				final Selector cssSelector = function.transform( selector );
				if ( cssSelector.matches() ) {
					predicates.add( cssSelector.predicate() );
					selector = cssSelector.remainingSelector();
					matched = cssSelector.matches();
				}
			}
		}

		if ( !selector.isEmpty() ) {
			logger.warn(
					"Unbreakable tests are not implemented for all CSS selectors. Please report your chosen selector ('{}') at https://github.com/retest/recheck-web/issues.",
					selector );
			return Optional.empty();

		}
		return Optional.of( predicates.stream().reduce( Predicate::and ).orElse( e -> true ) );
	}

}
